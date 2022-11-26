package dev.claycheng.knocknut.domain;

import com.google.common.collect.ImmutableMap;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

public class AuthUserTokenEnhancer implements TokenEnhancer {

  @Override
  public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication auth) {
    var authenticatedUser = (AuthenticatedUser) auth.getPrincipal();
    ((DefaultOAuth2AccessToken) accessToken)
        .setAdditionalInformation(
            ImmutableMap.of("additionalInfo", authenticatedUser.getUserProfile()));
    return accessToken;
  }
}
