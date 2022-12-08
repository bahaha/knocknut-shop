package dev.claycheng.knocknut.domain;

import com.google.common.base.Strings;
import java.net.URI;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;

@RequiredArgsConstructor
@Data
public class ApiRequest {

  private final URI requestUri;
  private final HttpHeaders headers;
  private final Predicate<String> publicPathDecisionMaker;
  private final TokenParser tokenParser;

  public boolean isPublic() {
    return publicPathDecisionMaker.test(requestUri.getPath());
  }

  public Optional<TokenEnhancement> getTokenEnahcement() {
    String authHeader = headers.getFirst("Authorization");
    if (Strings.isNullOrEmpty(authHeader)) {
      return Optional.empty();
    }

    return Optional.ofNullable(tokenParser.parse(authHeader));
  }
}
