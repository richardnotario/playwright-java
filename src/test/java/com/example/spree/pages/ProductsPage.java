package com.example.spree.pages;

import com.microsoft.playwright.Page;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductsPage {
  private final Page page;

  public ProductsPage(Page page) {
    this.page = page;
  }

  public void assertLoaded() {
    assertThat(page).hasURL(java.util.regex.Pattern.compile("/products", java.util.regex.Pattern.CASE_INSENSITIVE));
  }

  public void openFirstProduct() {
    assertLoaded();
    // product links contain /products/<slug>
    page.locator("a[href*='/products/']").nth(0).click();
    assertThat(page).hasURL(java.util.regex.Pattern.compile("/products/", java.util.regex.Pattern.CASE_INSENSITIVE));
  }
}
