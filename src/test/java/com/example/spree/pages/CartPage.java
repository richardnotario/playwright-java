package com.example.spree.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartPage {

  private final Page page;

  public CartPage(Page page) {
    this.page = page;
  }

  public static String escapeRegex(String input) {
    return input.replaceAll("[.*+?^${}()|\\[\\]\\\\]", "\\\\$0");
  }

  public Locator lineItems() {
    return page.locator("#line-items li.cart-line-item");
  }

  public Locator lineItemByName(String name) {
    String safe = escapeRegex(name.trim());
    Pattern re = Pattern.compile(safe, Pattern.CASE_INSENSITIVE);
  
    return page.locator("#line-items li.cart-line-item")
        .filter(new Locator.FilterOptions()
            .setHas(
                page.locator("a",
                    new Page.LocatorOptions().setHasText(re)
                )
            )
        )
        .first();
  }

  public void assertItemPresentByName(String name) {
    Locator item = lineItemByName(name);

    try {
      assertThat(item).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    } catch (Exception e) {
      System.out.println("[debug] cart titles: " +
          page.locator("#line-items a.font-semibold.text-text").allInnerTexts());
      throw e;
    }
  }

  public void assertQuantityForProduct(String name, int expected) {
    Locator qty = lineItemByName(name).locator("input[aria-label='Quantity']");
    assertThat(qty).isVisible();
    assertThat(qty).hasValue(String.valueOf(expected));
  }

  public void assertPriceVisibleForProduct(String name) {
    Locator priceBlock = lineItemByName(name)
        .locator("div.mb-2.text-sm").first();

    assertThat(priceBlock).isVisible();
    assertThat(priceBlock).containsText(Pattern.compile("\\$\\d+"));
  }
}
