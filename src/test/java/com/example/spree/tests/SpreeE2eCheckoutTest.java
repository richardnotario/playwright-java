package com.example.spree.tests;

import com.example.spree.core.BaseTest;
import com.example.spree.pages.*;
import com.example.spree.utils.TestData;
import org.junit.jupiter.api.Test;

public class SpreeE2eCheckoutTest extends BaseTest {

  @Test
  void registers_logs_in_adds_to_cart_and_verifies_cart() {
    HomePage home = new HomePage(page);
    HeaderComponent header = new HeaderComponent(page);
    AuthPage auth = new AuthPage(page);

    String email = TestData.uniqueEmail("spree");
    String password = TestData.defaultPassword();

    home.open();
    home.assertLoaded();

    // --- Register
    header.openAccountPanel();
    auth.goToSignUpFromPanel();
    auth.signUpAndAssertSuccess(email, password);
    screenshot("01-signup-success");

    // --- Logout
    page.navigate("/account/orders");
    new AccountPage(page).logoutAndAssert();
    screenshot("02-logout-success");

    // --- Login
    header.openAccountPanel();
    auth.login(email, password);
    screenshot("03-login-success");

    // --- Shop All and Add Item to Cart
    header.goToShopAll();
    ProductsPage products = new ProductsPage(page);
    products.assertLoaded();

    products.openFirstProduct();
    ProductDetailPage pdp = new ProductDetailPage(page);

    String productName = pdp.getDisplayedProductName();
    pdp.addToCartAndWaitForMiniCart(productName);
    screenshot("04-added-to-cart");

    // --- Verify cart
    page.navigate("/cart");
    CartPage cart = new CartPage(page);
    cart.assertItemPresentByName(productName);
    screenshot("05-cart");
  }
}
