package com.example.spree.pages;

import com.example.spree.pages.components.FlashBannerComponent;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AuthPage {
  private final Page page;

  public AuthPage(Page page) {
    this.page = page;
  }

  private Locator loginFrame() {
    return page.locator("turbo-frame#login");
  }

  public Locator email() {
    return loginFrame().getByLabel(Pattern.compile("email", Pattern.CASE_INSENSITIVE));
  }

  public Locator password() {
    return loginFrame().getByLabel(Pattern.compile("^password$", Pattern.CASE_INSENSITIVE));
  }

  public Locator rememberMeCheckbox() {
    return loginFrame().getByLabel(Pattern.compile("remember me", Pattern.CASE_INSENSITIVE));
  }

  public Locator loginButton() {
    return loginFrame().getByRole(
        AriaRole.BUTTON,
        new Locator.GetByRoleOptions().setName(Pattern.compile("^login$", Pattern.CASE_INSENSITIVE))
    );
  }

  public Locator signUpLink() {
    return loginFrame().getByRole(
        AriaRole.LINK,
        new Locator.GetByRoleOptions().setName(Pattern.compile("sign up", Pattern.CASE_INSENSITIVE))
    );
  }

  public Locator forgotPasswordLink() {
    return loginFrame().getByRole(
        AriaRole.LINK,
        new Locator.GetByRoleOptions().setName(Pattern.compile("forgot password", Pattern.CASE_INSENSITIVE))
    );
  }

  public Locator logoutLink() {
    return page.getByRole(
        AriaRole.LINK,
        new Page.GetByRoleOptions().setName(Pattern.compile("log out|logout", Pattern.CASE_INSENSITIVE))
    ).first();
  }

  // --- Assertions
  public void assertLoginFormReady() {
    assertThat(email()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(password()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    assertThat(rememberMeCheckbox()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(loginButton()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(loginButton()).isEnabled(new LocatorAssertions.IsEnabledOptions().setTimeout(15_000));

    assertThat(signUpLink()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(forgotPasswordLink()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
  }

  public void goToSignUpFromPanel() {
    assertLoginFormReady();
    signUpLink().click();

    Locator frame = loginFrame();

    assertThat(frame.getByRole(
        AriaRole.HEADING,
        new Locator.GetByRoleOptions().setName(Pattern.compile("^sign up$", Pattern.CASE_INSENSITIVE))
    )).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    assertThat(frame.getByLabel(Pattern.compile("^email$", Pattern.CASE_INSENSITIVE)))
        .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    assertThat(frame.getByLabel(Pattern.compile("^password$", Pattern.CASE_INSENSITIVE)))
        .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    assertThat(frame.getByLabel(Pattern.compile("^password confirmation$", Pattern.CASE_INSENSITIVE)))
        .isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    assertThat(frame.getByRole(
        AriaRole.BUTTON,
        new Locator.GetByRoleOptions().setName(Pattern.compile("^sign up$", Pattern.CASE_INSENSITIVE))
    )).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
  }

  public void login(String email, String password) {
    assertLoginFormReady();
    email().fill(email);
    password().fill(password);
    loginButton().click();

    FlashBannerComponent flash = new FlashBannerComponent(page);
    flash.expectNotice(Pattern.compile("signed in successfully\\.", Pattern.CASE_INSENSITIVE));
  }

  public void assertLoggedIn() {
    assertThat(logoutLink()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
  }

  public void signUp(String email, String password) {
    Locator frame = loginFrame();

    // Ensure sign-up form visible (call goToSignUpFromPanel first)
    assertThat(frame.getByRole(
        AriaRole.HEADING,
        new Locator.GetByRoleOptions().setName(Pattern.compile("^sign up$", Pattern.CASE_INSENSITIVE))
    )).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));

    frame.getByLabel(Pattern.compile("^email$", Pattern.CASE_INSENSITIVE)).fill(email);
    frame.getByLabel(Pattern.compile("^password$", Pattern.CASE_INSENSITIVE)).fill(password);
    frame.getByLabel(Pattern.compile("^password confirmation$", Pattern.CASE_INSENSITIVE)).fill(password);

    // IMPORTANT: click the SIGN UP submit button by role+name (prevents modal trigger mistakes)
    frame.getByRole(
        AriaRole.BUTTON,
        new Locator.GetByRoleOptions().setName(Pattern.compile("^sign up$", Pattern.CASE_INSENSITIVE))
    ).click();
  }

  public void assertSignUpSuccess() {
    FlashBannerComponent flash = new FlashBannerComponent(page);
    flash.expectNotice(Pattern.compile("welcome! you have signed up successfully\\.", Pattern.CASE_INSENSITIVE));
  }

  public void signUpAndAssertSuccess(String email, String password) {
    signUp(email, password);
    assertSignUpSuccess();
  }

  public void logoutAndAssert() {
    FlashBannerComponent flash = new FlashBannerComponent(page);

    assertThat(logoutLink()).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    logoutLink().click();

    flash.expectNotice(Pattern.compile("signed out successfully\\.", Pattern.CASE_INSENSITIVE));
  }

  public void logoutIfNeeded() {
    Locator logout = page.getByRole(
        AriaRole.LINK,
        new Page.GetByRoleOptions().setName(Pattern.compile("^log out$", Pattern.CASE_INSENSITIVE))
    ).first();

    if (logout.count() == 0) return;

    assertThat(logout).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    logout.scrollIntoViewIfNeeded();
    logout.click(new Locator.ClickOptions().setForce(true));

    FlashBannerComponent flash = new FlashBannerComponent(page);
    flash.expectNotice(Pattern.compile("signed out successfully\\.", Pattern.CASE_INSENSITIVE));
  }
}
