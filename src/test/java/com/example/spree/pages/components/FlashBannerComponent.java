package com.example.spree.pages.components;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;

import java.util.regex.Pattern;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class FlashBannerComponent {
  private final Page page;

  public FlashBannerComponent(Page page) {
    this.page = page;
  }

  public Locator noticeMessage() {
    return page.locator(".alert-notice .flash-message");
  }

  public Locator errorMessage() {
    return page.locator(".alert-error .flash-message");
  }

  public void expectNotice(String text) {
    Locator msg = noticeMessage();
    assertThat(msg).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(msg).hasText(text, new LocatorAssertions.HasTextOptions().setTimeout(15_000));
  }

  public void expectNotice(Pattern regex) {
    Locator msg = noticeMessage();
    assertThat(msg).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(15_000));
    assertThat(msg).hasText(regex, new LocatorAssertions.HasTextOptions().setTimeout(15_000));
  }
}
