package dev.claycheng.knocknut.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Register the InMemoryTokenStore from Spring as the token store, the default token store if no
 * properties override.
 *
 * @author Clay Cheng
 */
@ConditionalOnProperty(
    prefix = "knocknut.auth.jwt",
    name = "enabled",
    havingValue = "false",
    matchIfMissing = true)
@Configuration
public class InMemoryTokenStore {

  @Bean
  public TokenStore inMemory() {
    return new org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore();
  }
}
