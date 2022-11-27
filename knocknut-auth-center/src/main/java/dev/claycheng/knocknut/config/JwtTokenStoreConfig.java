package dev.claycheng.knocknut.config;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import dev.claycheng.knocknut.domain.AuthUserTokenEnhancer;
import dev.claycheng.knocknut.props.JwtProps;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
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

/**
 * With the property `knocknut.auth.jwt.enabled` set to **true**, use the {@link JwtTokenStore} as
 * the token persistence strategy. While the property value of `knocknut.auth.jwt.algo` is
 * **hashing**, we would transform the access token to the jwt format with the HS256 algorithm which
 * having the leak risk of the secret. In general, the asymmetric encryption **RSA** would be the
 * suitable algorithm to secure the token we issued. At the resource server, we could just share the
 * public key of the key pair which is more secure than sharing the whole secret take from the
 * hashing algorithm.
 *
 * @author Clay Cheng
 */
@ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "enabled", havingValue = "true")
@Configuration
@EnableConfigurationProperties(value = JwtProps.class)
@RequiredArgsConstructor
public class JwtTokenStoreConfig {
  private final JwtProps jwtProps;

  /**
   * Take the `knocknut.auth.jwt.secret` as the secret to verify the signature of token only when
   * `knocknut.auth.jwt.algo` set to **hashing**. With hashing-based jwt, usually share the secret
   * between the resource servers for token validity but have the risk of secret leaking.
   *
   * @return Hashing-based JWS token converter with secret from `knocknut.auth.jwt.secret`
   */
  @ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "algo", havingValue = "hashing")
  @Bean
  public JwtAccessTokenConverter hashingSignature() {
    var converter = new JwtAccessTokenConverter();
    converter.setSigningKey(jwtProps.getSecret());
    return converter;
  }

  /**
   * Encryption-based jwt token, encrypt the user authorization data and profile data with RSA256
   * algorithm with higher security level. The resource servers could save the public key of the key
   * pair, or just request the authorization server to get the public key for the jwt token
   * validity. Support non-encrypted PEM key pair only now, check the implementation {@link
   * #readRsaKeyPairByPrivateKeyPem(URL)}.
   *
   * @return Encryption-based JWS token converter with the RSA private pem key file from resource
   *     path `knocknut.auth.jwt.rsa-private-key-pem`.
   * @throws IOException exception while reading and parsing the private key with pem format.
   */
  @ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "algo", havingValue = "rsa")
  @Bean
  public JwtAccessTokenConverter rsaSignature() throws IOException {
    var converter = new JwtAccessTokenConverter();
    converter.setKeyPair(loadRsaKeyPairByPrivateKeyPem(jwtProps.getRsaPrivateKeyPemFromResource()));

    return converter;
  }

  /**
   * Read the private key with pem format via <a
   * href="https://www.bouncycastle.org/java.html">Bouncy Castle</a>
   */
  private KeyPair loadRsaKeyPairByPrivateKeyPem(URL resource) throws IOException {
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
    var pemConverter = new JcaPEMKeyConverter();

    return new KeyPair(
        pemConverter.getPublicKey(keyPair.getPublicKeyInfo()),
        pemConverter.getPrivateKey(keyPair.getPrivateKeyInfo()));
  }

  /**
   * Return the jwt token converter which transform the access token to follow the jwt format, and
   * take the algorithm from property `knocknut.auth.jwt.algo` as the strategy to use a
   * hashing-based converter or the encryption-based converter.
   *
   * @param accessTokenConverter injected from spring context base on the property value of
   *     `knocknut.auth.jwt.algo`
   * @return the hashing-based token converter or the encryption-based converter
   */
  @Bean
  @Primary
  public TokenStore jwtToken(JwtAccessTokenConverter accessTokenConverter) {
    return new JwtTokenStore(accessTokenConverter);
  }

  /**
   * Put the knocknut user profile into the token with `additionalInfo` key for the resource server
   * usage.
   *
   * @param tokenConverter injected from spring context base on the property value of *
   *     `knocknut.auth.jwt.algo`
   * @return the enhancer chain for with jwt format support
   */
  @Bean
  public TokenEnhancerChain enhancerChain(JwtAccessTokenConverter tokenConverter) {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(
        Lists.newArrayList(new AuthUserTokenEnhancer(), tokenConverter));
    return tokenEnhancerChain;
  }
}
