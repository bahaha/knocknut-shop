package dev.claycheng.knocknut.domain;

import com.google.common.collect.Maps;
import dev.claycheng.api.enums.MemberStatus;
import dev.claycheng.base.IntEnum;
import dev.claycheng.knocknut.props.JwtProps;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(prefix = "knocknut.auth.jwt", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
@EnableConfigurationProperties(value = JwtProps.class)
@Component
public class JwtTokenParser {

  private final JwtProps jwtProps;

  @Bean
  public TokenParser jwtParser() {
    return header -> {
      var token = extractToken(header);
      var parser = jwtProps.getTokenParser();
      var claims = parser.parseClaimsJws(token).getBody();
      var memberProfile = Optional.ofNullable(claims.get("additionalInfo", Map.class))
          .orElse(Maps.newHashMap());
      var memberStatus =
          IntEnum.from(MemberStatus.class,
              Integer.parseInt(memberProfile.get("status").toString()));

      var enhancementBuilder = TokenEnhancement.builder();
      enhancementBuilder
          .memberId(memberProfile.get("memberId").toString())
          .username(memberProfile.get("username").toString())
          .nickname(memberProfile.get("nickname").toString())
          .email(memberProfile.get("email").toString())
          .avatar(memberProfile.get("avatar").toString());

      memberStatus.ifPresent(enhancementBuilder::status);
      return enhancementBuilder.build();
    };
  }

  private String extractToken(String authorizationHeader) {
    var tokenMetadataPos = jwtProps.getTokenType().length() + 1;
    return authorizationHeader.substring(tokenMetadataPos);
  }
}
