package com.example.spree.utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public final class AllureAttachments {
  private AllureAttachments() {}

  public static void attachScreenshot(String name, byte[] pngBytes) {
    if (pngBytes == null) return;
    Allure.addAttachment(name, "image/png", new ByteArrayInputStream(pngBytes), ".png");
  }

  public static void attachText(String name, String content) {
    if (content == null) content = "";
    Allure.addAttachment(name, "text/plain", new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)), ".txt");
  }

  @Attachment(value = "{name}", type = "application/zip", fileExtension = ".zip")
  public static byte[] attachZip(String name, byte[] zipBytes) {
    return zipBytes;
  }
}
