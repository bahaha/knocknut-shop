package dev.claycheng.knocknut.config;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import dev.claycheng.knocknut.domain.AuthUserTokenEnhancer;
import dev.claycheng.knocknut.props.JwtProps;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = JwtProps.class)
@RequiredArgsConstructor
public class JwtTokenStoreConfig {
  private final JwtProps jwtProps;

  @ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "algo", havingValue = "hashing")
  @Bean
  public JwtAccessTokenConverter hashingSignature() {
    var converter = new JwtAccessTokenConverter();
    converter.setSigningKey(jwtProps.getSecret());
    return converter;
  }

  @ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "algo", havingValue = "rsa")
  @Bean
  public JwtAccessTokenConverter rsaSignature() throws IOException {
    var converter = new JwtAccessTokenConverter();
    converter.setKeyPair(
        new KeyPair(
            readRsaPublicKey(jwtProps.getRsaPublicKeyPemFromResource()),
            readRsaPrivateKey(jwtProps.getRsaPrivateKeyPemFromResource())));

    return converter;
  }

  private PrivateKey readRsaPrivateKey(URL resource) throws IOException {
    @Cleanup
    var keyReader =
        Resources.asCharSource(resource, StandardCharsets.US_ASCII).openBufferedStream();
    var pemParser = new PEMParser(keyReader);
    var pemObject = pemParser.readObject();
    if (!(pemObject instanceof PEMKeyPair)) {
      throw new IOException(
          "unsupported rsa key pair found. only support with non-encrypted pem file.");
    }
    var keyPair = ((PEMKeyPair) pemObject);
    return new JcaPEMKeyConverter().getPrivateKey(keyPair.getPrivateKeyInfo());
  }

  public PublicKey readRsaPublicKey(URL resource) throws IOException {
    @Cleanup
    var keyReader =
        Resources.asCharSource(resource, StandardCharsets.US_ASCII).openBufferedStream();
    var pemParser = new PEMParser(keyReader);
    var pemObject = pemParser.readObject();
    return new JcaPEMKeyConverter().getPublicKey((SubjectPublicKeyInfo) pemObject);
  }

  @Bean
  @Primary
  public TokenStore jwtToken(JwtAccessTokenConverter accessTokenConverter) {
    return new JwtTokenStore(accessTokenConverter);
  }

  @Bean
  public TokenEnhancerChain enhancerChain(JwtAccessTokenConverter tokenConverter) {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(
        Lists.newArrayList(new AuthUserTokenEnhancer(), tokenConverter));
    return tokenEnhancerChain;
  }
}
