package dev.claycheng.knocknut.props;

import com.google.common.collect.Sets;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;

@Data
@ConfigurationProperties("knocknut.auth.gateway")
public class PublicUrlsProperties {

  private LinkedHashSet<String> publicUrls;

  public boolean isPublicPath(String path) {
    var pathMatcher = new AntPathMatcher();
    return getPublicUrls().stream().anyMatch(publicUrl -> pathMatcher.match(publicUrl, path));
  }

  public Set<String> getPublicUrls() {
    return Optional.ofNullable(publicUrls).orElse(Sets.newLinkedHashSet());
  }
}
