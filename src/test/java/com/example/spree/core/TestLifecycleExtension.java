package com.example.spree.core;

import org.junit.jupiter.api.extension.*;

public class TestLifecycleExtension implements BeforeEachCallback, AfterEachCallback {
  private final BaseTest test;
  private AllureFailureExtension allureFailure;

  public TestLifecycleExtension(BaseTest test) {
    this.test = test;
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    
  }

  @Override
  public void afterEach(ExtensionContext context) {
    
  }

  public AllureFailureExtension allureFailure() {
    if (allureFailure == null) {
      allureFailure = new AllureFailureExtension(test.getPage());
    }
    return allureFailure;
  }
}
