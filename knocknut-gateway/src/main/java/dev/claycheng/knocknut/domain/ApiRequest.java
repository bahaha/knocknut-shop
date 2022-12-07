package dev.claycheng.knocknut.domain;

import java.net.URI;
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

  public boolean isPublic() {
    return publicPathDecisionMaker.test(requestUri.getPath());
  }
}
