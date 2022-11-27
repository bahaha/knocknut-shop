package dev.claycheng.knocknut.repository;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Load user-specific data from somewhere.
 *
 * <p>Spring would take this user manager to verify user credentials with the {@link
 * DaoAuthenticationProvider} of the security filter chain. At most scenario, we implement our
 * {@link UserDetailsService} and the user-specific domain object {@link User} to fit the
 * requirements.
 *
 * @author Clay Cheng
 */
public interface KnocknutUserManager extends UserDetailsService {}
