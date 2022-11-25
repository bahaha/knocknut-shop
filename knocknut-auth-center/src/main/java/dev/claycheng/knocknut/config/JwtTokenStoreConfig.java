package dev.claycheng.knocknut.config;

import com.google.common.collect.Lists;
import dev.claycheng.knocknut.props.JwtProps;
import lombok.RequiredArgsConstructor;
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

  @Bean
  @Primary
  public TokenStore jwtToken(JwtAccessTokenConverter accessTokenConverter) {
    return new JwtTokenStore(accessTokenConverter);
  }

  @Bean
  public TokenEnhancerChain enhancerChain(JwtAccessTokenConverter tokenConverter) {
    TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
    tokenEnhancerChain.setTokenEnhancers(Lists.newArrayList(tokenConverter));
    return tokenEnhancerChain;
  }
}
