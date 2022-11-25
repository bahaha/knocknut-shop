package dev.claycheng.knocknut.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "knocknut.auth.jwt")
public class JwtProps {

  private boolean enabled;
  private String algo = "hashing";
  private String secret;
}
