package dev.claycheng.knocknut.config;

import dev.claycheng.knocknut.repository.KnocknutUserManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

/**
 * The auth center provide the oauth endpoints from {@link TokenEndpoint}, check the user
 * credentials validity by the customize implementation {@link KnocknutUserManager}.
 *
 * @author Clay Cheng
 */
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  private final KnocknutUserManager userDetailsService;

  /**
   * Return a response with HTTP status code 403 on authentication failure, otherwise, bypass all
   * the request with request uri pattern `/oauth/**`, let the {@link AuthCenterConfig} take the
   * rest to do the authorization. With token-based strategy, csrf token would be disabled always.
   *
   * @param http
   * @throws Exception
   */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.httpBasic()
        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
        .and()
        .authorizeRequests()
        .antMatchers("/oauth/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .logout()
        .permitAll()
        .and()
        .csrf()
        .disable();
  }

  /**
   * Check the user credentials validity from the end-user with the {@link BCryptPasswordEncoder}
   * passwordEncoder
   *
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }
}
