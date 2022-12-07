package dev.claycheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class KnocknutGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(KnocknutGatewayApplication.class, args);
  }
}
