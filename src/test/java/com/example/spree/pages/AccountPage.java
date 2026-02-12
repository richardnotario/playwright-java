package com.example.spree.pages;

import com.example.spree.pages.components.FlashBannerComponent;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.PageAssertions;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AccountPage {
  private final Page page;

  public AccountPage(Page page) {
    this.page = page;
  }

  public Locator logoutButton() {
    return page.locator("form.button_to[action='/user/sign_out'] button[type='submit']")
        .filter(new Locator.FilterOptions().setHasText("Log out"));
  }

  public void logoutAndAssert() {
    assertThat(page).hasURL(Pattern.compile("/account(/|$)", Pattern.CASE_INSENSITIVE),
        new PageAssertions.HasURLOptions().setTimeout(15_000));

    assertThat(page).hasURL(Pattern.compile("/account/orders", Pattern.CASE_INSENSITIVE),
        new PageAssertions.HasURLOptions().setTimeout(15_000));

    Locator logout = logoutButton();
    assertThat(logout).isVisible(new com.microsoft.playwright.assertions.LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    logout.scrollIntoViewIfNeeded();

    page.waitForLoadState(com.microsoft.playwright.options.LoadState.DOMCONTENTLOADED);
    logout.click(new Locator.ClickOptions().setForce(true));

    FlashBannerComponent flash = new FlashBannerComponent(page);
    flash.expectNotice(Pattern.compile("signed out successfully\\.", Pattern.CASE_INSENSITIVE));

    assertThat(page).not().hasURL(Pattern.compile("/account(/|$)", Pattern.CASE_INSENSITIVE),
        new PageAssertions.HasURLOptions().setTimeout(15_000));
  }
}
