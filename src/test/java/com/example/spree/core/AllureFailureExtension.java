package com.example.spree.core;

import com.example.spree.utils.AllureAttachments;
import com.microsoft.playwright.Page;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

import java.util.Optional;

public class AllureFailureExtension implements TestWatcher {

  private final Page page;

  public AllureFailureExtension(Page page) {
    this.page = page;
  }

  @Override
  public void testFailed(ExtensionContext context, Throwable cause) {
    try {
      // Screenshot
      byte[] shot = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
      AllureAttachments.attachScreenshot("Failure Screenshot", shot);

      // URL (handy)
      AllureAttachments.attachText("URL", page.url());

      // Error
      AllureAttachments.attachText("Error", String.valueOf(cause));
    } catch (Exception ignored) {
      // keep tests from failing due to reporting
    }
  }

  @Override
  public void testSuccessful(ExtensionContext context) {

  }

  @Override
  public void testDisabled(ExtensionContext context, Optional<String> reason) {
    
  }
}
