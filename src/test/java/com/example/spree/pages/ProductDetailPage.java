package com.example.spree.pages;

import com.example.spree.pages.components.CartDrawerComponent;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;

import java.util.List;
import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ProductDetailPage {

  private final Page page;

  public ProductDetailPage(Page page) {
    this.page = page;
  }

  public Locator addToCartButton() {
    return page.getByRole(com.microsoft.playwright.options.AriaRole.BUTTON,
        new Page.GetByRoleOptions().setName(Pattern.compile("add to cart", Pattern.CASE_INSENSITIVE)));
  }

  public Locator productTitle() {
    return page.getByRole(com.microsoft.playwright.options.AriaRole.HEADING).first();
  }

  private Locator sizeFieldset() {
    return page.locator("fieldset[data-option-id] [role='group'][aria-label='Size']").first();
  }

  private Locator sizeDropdownButton() {
    return sizeFieldset().locator("button[data-dropdown-target='button']").first();
  }

  private Locator sizeDropdownMenu() {
    return sizeFieldset().locator("[data-dropdown-target='menu'][role='menu']").first();
  }

  private Locator sizeOptions() {
    return sizeFieldset().locator("label[role='menuitem']");
  }

  public void selectSizeIfPresent(List<String> preferred) {
    if (sizeFieldset().count() == 0) return;

    Locator btn = sizeDropdownButton();
    assertThat(btn).hasCount(1, new LocatorAssertions.HasCountOptions().setTimeout(15_000));

    Locator menu = sizeDropdownMenu();
    if (!menu.isVisible()) {
      btn.scrollIntoViewIfNeeded();
      btn.click(new Locator.ClickOptions().setForce(true));
    }

    assertThat(menu).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    for (String size : preferred) {
      Locator opt = sizeOptions()
          .filter(new Locator.FilterOptions()
              .setHas(page.locator("p", new Page.LocatorOptions().setHasText(size))))
          .first();

      if (opt.count() > 0) {
        opt.click(new Locator.ClickOptions().setForce(true));
        assertThat(btn).not().containsText(Pattern.compile("please choose", Pattern.CASE_INSENSITIVE));
        return;
      }

      Locator optByText = sizeOptions()
          .filter(new Locator.FilterOptions()
              .setHasText(Pattern.compile("^" + size + "$", Pattern.CASE_INSENSITIVE)))
          .first();

      if (optByText.count() > 0) {
        optByText.click(new Locator.ClickOptions().setForce(true));
        assertThat(btn).not().containsText(Pattern.compile("please choose", Pattern.CASE_INSENSITIVE));
        return;
      }
    }

    Locator first = sizeOptions().first();
    assertThat(first).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    first.click(new Locator.ClickOptions().setForce(true));
    assertThat(btn).not().containsText(Pattern.compile("please choose", Pattern.CASE_INSENSITIVE));
  }

  public void addToCartAndWaitForMiniCart(String productName) {
    selectSizeIfPresent(List.of("S", "M", "L"));

    Locator btn = addToCartButton();
    assertThat(btn).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(btn).isEnabled();

    btn.click();

    CartDrawerComponent drawer = new CartDrawerComponent(page);
    drawer.waitForProduct(productName);
    drawer.close();
  }

  public String getDisplayedProductName() {
    assertThat(productTitle()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    return productTitle().innerText().trim();
  }
}
