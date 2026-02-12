package com.example.spree.pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePage {
  private final Page page;

  public HomePage(Page page) {
    this.page = page;
  }

  public void open() {
    page.navigate("/");
    assertThat(page).hasURL(java.util.regex.Pattern.compile("demo\\.spreecommerce\\.org", java.util.regex.Pattern.CASE_INSENSITIVE));
  }

  public void assertLoaded() {
    assertThat(page.locator("text=Welcome to the Spree Commerce Demo!")).isVisible();
  }
}
