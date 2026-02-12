package com.example.spree.utils;

import java.time.Instant;

public final class TestData {
  private TestData() {}

  public static String defaultPassword() {
    return "P@ssw0rd!234";
  }

  public static String uniqueEmail(String prefix) {
    long ts = Instant.now().toEpochMilli();
    return prefix + "+" + ts + "@example.com";
  }
}
