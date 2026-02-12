package com.example.spree.core;

import com.microsoft.playwright.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;

public abstract class BaseTest {
  protected Playwright playwright;
  protected Browser browser;
  protected BrowserContext context;
  protected Page page;

  @RegisterExtension
  TestLifecycleExtension lifecycle = new TestLifecycleExtension(this);

  protected String baseUrl() {
    return System.getProperty("baseUrl", "https://demo.spreecommerce.org");
  }

  protected boolean headless() {
    return Boolean.parseBoolean(System.getProperty("headless", "true"));
  }

  @BeforeEach
  void setUp() {
    playwright = Playwright.create();
    browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(headless()));
    context = browser.newContext(
      new Browser.NewContextOptions()
          .setBaseURL(baseUrl())
          .setViewportSize(1440, 900)
    );
    page = context.newPage();
  }

  @AfterEach
  void tearDown() {
    if (context != null) context.close();
    if (browser != null) browser.close();
    if (playwright != null) playwright.close();
  }

  protected void screenshot(String name) {
  try {
    Path dir = Paths.get("target", "screenshots");
    Files.createDirectories(dir);

    page.screenshot(new com.microsoft.playwright.Page.ScreenshotOptions()
      .setPath(dir.resolve(name + ".png"))
      .setFullPage(true)
    );
  } catch (Exception e) {
    System.out.println("Screenshot failed: " + e.getMessage());
  }
}

  Page getPage() { return page; }
}
