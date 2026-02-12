package com.example.spree.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HeaderComponent {
  private final Page page;

  public HeaderComponent(Page page) {
    this.page = page;
  }

  private Locator accountTrigger() {
    return page.getByLabel("Open account panel").first();
  }

  private Locator loginFrame() {
    return page.locator("turbo-frame#login");
  }

  public void openAccountPanelExpectLogin() {
    Locator trigger = accountTrigger();
  
    assertThat(trigger).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    trigger.scrollIntoViewIfNeeded();
    trigger.click(new Locator.ClickOptions().setTrial(true));
    trigger.click();
  
    Locator frame = loginFrame();
    assertThat(frame).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
  
    Locator email = frame.getByLabel(Pattern.compile("email", Pattern.CASE_INSENSITIVE));
    Locator password = frame.getByLabel(Pattern.compile("^password$", Pattern.CASE_INSENSITIVE));
  
    assertThat(email).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(password).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
  }

  /**
   * Opens the account drawer/panel without asserting login fields.
   * Useful after login for actions like "My Account" or "Logout".
   */
  public void openAccountPanel() {
    Locator trigger = accountTrigger();
    assertThat(trigger).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    trigger.scrollIntoViewIfNeeded();
    trigger.click(new Locator.ClickOptions().setTrial(true));
    trigger.click();
  }

  /**
   * Navigates to "Shop All" (/products). Keeps selectors simple and stable.
   */
  public void goToShopAll() {
    page.locator("a[href='/products'], a[href*='/products']").first().click();
    assertThat(page).hasURL(Pattern.compile("/products", Pattern.CASE_INSENSITIVE));
  }

  /**
   * Convenience: click "My Account" link after opening account panel.
   * Works when user is logged in and the link exists in the drawer.
   */
  public void goToMyAccount() {
    openAccountPanel();

    Locator myAccountLink = page.getByRole(
        AriaRole.LINK,
        new Page.GetByRoleOptions().setName(Pattern.compile("my account", Pattern.CASE_INSENSITIVE))
    ).first();

    assertThat(myAccountLink).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    myAccountLink.click();
    assertThat(page).hasURL(Pattern.compile("/account", Pattern.CASE_INSENSITIVE));
  }
}
