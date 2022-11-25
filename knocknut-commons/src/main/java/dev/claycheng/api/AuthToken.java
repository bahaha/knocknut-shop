package dev.claycheng.api;

import java.util.Map;
import lombok.Data;

@Data
public class AuthToken {

  private String access_token;
  private String token_type;
  private String refresh_token;
  private Integer expires_in;
  private String scope;
  private Map<String, String> additionalInfo;
}
