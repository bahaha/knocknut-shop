package dev.claycheng.knocknut.props;

import com.google.common.io.Resources;
import dev.claycheng.common.exception.base.BadConfigurationException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.Optional;
import lombok.Cleanup;
import lombok.Data;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "knocknut.auth.jwt")
public class JwtProps {

  private boolean enabled = false;
  private String algo = "hashing";
  private String secret;
  private String rsaPublicKeyPem;
  private String tokenType;

  public String getTokenType() {
    return Optional.ofNullable(tokenType).orElse("bearer");
  }

  public URL getRsaPublicKeyPemFromResource() {
    return Resources.getResource(getRsaPublicKeyPem());
  }

  public JwtParser getTokenParser() {
    var signingKey = algo.equals("hashing")
        ? Keys.hmacShaKeyFor(getSecret().getBytes(StandardCharsets.UTF_8))
        : readRsaPublicKey(getRsaPublicKeyPemFromResource());

    return Jwts.parserBuilder()
        .setSigningKey(signingKey)
        .build();
  }

  private PublicKey readRsaPublicKey(URL resource) {
    try {
      @Cleanup
      var keyReader =
          Resources.asCharSource(resource, StandardCharsets.US_ASCII).openBufferedStream();
      var pemParser = new PEMParser(keyReader);
      var pemObject = pemParser.readObject();
      return new JcaPEMKeyConverter().getPublicKey((SubjectPublicKeyInfo) pemObject);
    } catch (IOException ex) {
      throw new BadConfigurationException("gateway", "no signing key, check the public key please");
    }
  }
}
