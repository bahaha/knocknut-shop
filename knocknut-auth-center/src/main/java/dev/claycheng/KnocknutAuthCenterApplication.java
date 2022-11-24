package dev.claycheng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class KnocknutAuthCenterApplication {

  public static void main(String[] args) {
    SpringApplication.run(KnocknutAuthCenterApplication.class, args);
  }
}
