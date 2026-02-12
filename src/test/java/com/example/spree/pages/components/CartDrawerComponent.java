package com.example.spree.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CartDrawerComponent {

  private final Page page;

  public CartDrawerComponent(Page page) {
    this.page = page;
  }

  public Locator overlay() {
    return page.locator("#cart-pane");
  }

  public Locator drawer() {
    return page.locator("#slideover-cart");
  }

  public Locator title() {
    return drawer().getByText(Pattern.compile("^cart$", Pattern.CASE_INSENSITIVE));
  }

  public Locator lineItemsList() {
    return drawer().locator("#line-items");
  }

  public Locator lineItemByName(String name) {
    return drawer().locator("#line-items a.font-semibold.text-text",
        new Locator.LocatorOptions().setHasText(name))
        .first();
  }

  public Locator closeButton() {
    return drawer().getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions()
    .setName(Pattern.compile("close sidebar", Pattern.CASE_INSENSITIVE))
    );
  }

  public void waitForOpen() {
    assertThat(overlay()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(drawer()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(title()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
  }

  public void waitForItemsLoaded() {
    waitForOpen();
    assertThat(lineItemsList()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    Locator items = lineItemsList().locator("li.cart-line-item");

    long timeout = System.currentTimeMillis() + 15_000;
    while (System.currentTimeMillis() < timeout) {
      if (items.count() > 0) return;
      page.waitForTimeout(200);
    }

    throw new RuntimeException("At least one cart line item should appear");
  }

  public void waitForProduct(String name) {
    waitForItemsLoaded();
    assertThat(lineItemByName(name)).isVisible(
        new LocatorAssertions.IsVisibleOptions().setTimeout(15_000)
    );
  }

  public void close() {
    if (closeButton().count() > 0) {
      closeButton().click(new Locator.ClickOptions().setForce(true));
      assertThat(overlay()).isHidden(new LocatorAssertions.IsHiddenOptions().setTimeout(15_000));
    }
  }
}
