package dev.claycheng.knocknut.domain;

@FunctionalInterface
public interface TokenParser {

  TokenEnhancement parse(String header);
}
