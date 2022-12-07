package dev.claycheng.knocknut.props;

import com.google.common.io.Resources;
import java.net.URL;
import java.util.Optional;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "knocknut.auth.jwt")
public class JwtProps {

  private boolean enabled = false;
  private String algo = "hashing";
  private String secret;

  private String rsaPrivateKeyPem;
  private String rsaPublicKeyPem;

  public String getRsaPublicKeyPem() {
    return Optional.ofNullable(rsaPublicKeyPem).orElse(getRsaPrivateKeyPem() + ".pub");
  }

  public URL getRsaPrivateKeyPemFromResource() {
    return Resources.getResource(getRsaPrivateKeyPem());
  }

  public URL getRsaPublicKeyPemFromResource() {
    return Resources.getResource(getRsaPublicKeyPem());
  }
}
