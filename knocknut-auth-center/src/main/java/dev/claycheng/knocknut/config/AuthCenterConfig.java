package dev.claycheng.knocknut.config;

import dev.claycheng.knocknut.repository.KnocknutUserManager;
import javax.annotation.Nullable;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.InMemoryClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * Publish authorization endpoints like /oauth/token from {@link TokenEndpoint} for client
 * applications by tagging the `{@link EnableAuthorizationServer}` annotation.
 *
 * <p>Configuration extends {@link AuthorizationServerConfigurerAdapter} which provides some useful
 * customizations for the authorization progress. We usually override these configurations for
 * common application as following:
 *
 * <p>- configure({@link AuthorizationServerEndpointsConfigurer}): Configure the props and enhanced
 * functionality of the endpoints, {@link TokenStore} represents where we store the token, could be
 * in-memory, jdbc or jwt token store. {@link TokenEnhancer} defines how the token would be
 * enhanced. Check if the request from the end-user is valid by overriding the {@link
 * UserDetailsService} {@link AuthenticationManager} to support the **password** grant type of the
 * OAuth 2.0.
 *
 * <p>- configure({@link AuthorizationServerSecurityConfigurer}): To protect those endpoints access
 * from anyone, always limit the anonymous clients access
 *
 * @author Clay Cheng
 */
@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthCenterConfig extends AuthorizationServerConfigurerAdapter {

  private final AuthenticationManager authenticationManager;
  private final TokenStore tokenStore;
  private final DataSource dataSource;
  @Nullable private final TokenEnhancerChain tokenEnhancerChain;
  private final KnocknutUserManager userDetailsService;

  @Bean
  public ClientDetailsService clientDetails() {
    return new JdbcClientDetailsService(dataSource);
  }

  /**
   * the {@link ClientDetailsService} describe how we store the trust client details. Spring provide
   * {@link InMemoryClientDetailsService} and {@link JdbcClientDetailsService} implementations out
   * of the box. Just only run some SQL scripts to create the tables defined by Spring, and we could
   * persist all the client details in the database for the nice horizontal scaling. The SQL scripts
   * mentioned above would automatically migrate with Flyway, check the file:
   * resources/db/user/migration/1/V1.0__init.sql for more information.
   *
   * @param clients the client details configurer
   * @throws Exception
   */
  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients.withClientDetails(clientDetails());
  }

  /**
   * Adapt the Jwt token store if the `knocknut.auth.jwt.enabled` property is true or the in-memory
   * implementation as default, check {@link InMemoryTokenStore} and {@link JwtTokenStoreConfig} for
   * the properties' configuration. By registering the tokenEnhancer with the jwt token store, the
   * access token would follow the <a href="https://jwt.io/">JWT</a> schema syntax. Ask Spring check
   * the user credentials validity by setting the customized {@link UserDetailsService}. To support
   * the grant type **password** of OAuth, we set the {@link AuthenticationManager} from the
   * WebSecurity ({@link WebSecurityConfig#authenticationManagerBean()}
   *
   * @param endpoints the endpoints configurer
   */
  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .tokenStore(tokenStore)
        .tokenEnhancer(tokenEnhancerChain)
        .userDetailsService(userDetailsService)
        .authenticationManager(authenticationManager);
  }

  /**
   * Only the authenticated and trust clients could ask for token checking and accessing.
   *
   * @param security a fluent configurer for security features
   */
  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) {
    security
        .checkTokenAccess("isAuthenticated()")
        .tokenKeyAccess("isAuthenticated()")
        .allowFormAuthenticationForClients();
  }
}
