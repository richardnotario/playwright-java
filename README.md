# Spree Demo -- Java Playwright Automation Framework

## 📌 Overview

This project is a Java-based end-to-end automation framework built
using:

-   **Playwright (Java)**
-   **JUnit 5**
-   **Maven**
-   **Allure Reporting**
-   **GitHub Actions (CI/CD)**

It automates a complete user journey on:

> https://demo.spreecommerce.org/

The framework is structured using a Page Object Model (POM) design and
mirrors a TypeScript Playwright implementation for parity and
cross-language consistency.

------------------------------------------------------------------------

## 🎯 Covered Test Flow

The automated scenario includes:

1.  User Registration
2.  Logout
3.  Login
4.  Navigate to Products
5.  Select Product (with variant handling)
6.  Add to Cart (with mini-cart synchronization)
7.  Validate Product in Cart

The framework handles:

-   Variant dropdown selection (size-aware)
-   Turbo frame account panel interactions
-   Drawer-based mini cart synchronization
-   Case-insensitive cart validation
-   Regex-safe product matching
-   Robust wait strategies (no hard sleeps)

------------------------------------------------------------------------

## 🏗 Framework Architecture

    src
     ├── main
     │   └── java
     │       └── com.example.spree
     │           ├── pages
     │           ├── pages.components
     │           └── utils
     └── test
         └── java
             └── com.example.spree.tests

------------------------------------------------------------------------

## ⚙️ Tech Stack

  Tool                Purpose
  ------------------- -------------------------------
  Playwright (Java)   UI automation engine
  JUnit 5             Test execution framework
  Maven               Dependency & build management
  Allure              Test reporting
  GitHub Actions      Continuous Integration

------------------------------------------------------------------------

## 🚀 Running Tests Locally

### Install Dependencies

``` bash
mvn clean install
```

### Run Tests

``` bash
mvn test
```

------------------------------------------------------------------------

## 📊 Allure Reporting

After test execution:

``` bash
allure serve target/allure-results
```

------------------------------------------------------------------------

## 🔁 Continuous Integration

Tests run automatically via GitHub Actions and publish an Allure report
to GitHub Pages.

------------------------------------------------------------------------

## 👨‍💻 Author

Built as a structured automation demonstration project showcasing
Playwright Java framework design and CI/CD integration.
