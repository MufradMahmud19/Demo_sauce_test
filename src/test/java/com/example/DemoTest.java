package com.example;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class SauceDemoTest {

    WebDriver browser;
    WebDriverWait waitFor;

    @BeforeEach
    public void setUp() {
        browser = new ChromeDriver();
        waitFor = new WebDriverWait(browser, Duration.ofSeconds(20));
        browser.get("https://www.saucedemo.com/");
    }

    @Test
    public void lockedOutUserTest() {
        performLogin("locked_out_user", "secret_sauce");
        String actualError = getLoginErrorMessage();
        String expectedError = "Epic sadface: Sorry, this user has been locked out.";

        Assertions.assertEquals(expectedError, actualError, "Locked out user test failed!");
        System.out.println("Locked out user test passed!");
    }

    @Test
    public void standardUserAddToCartTest() {
        performLogin("standard_user", "secret_sauce");
        resetApplicationState();

        addItemsToShoppingCart(3);
        openCartPage();
        int cartItemCount = getCartItemsCount();

        Assertions.assertEquals(3, cartItemCount, "Error: Expected 3 items in the cart.");

        proceedToCheckout("John", "Doe", "12345");
        String displayedTotalPrice = retrieveTotalPrice();
        System.out.println("Total Price: " + displayedTotalPrice);

        finalizeCheckout();
        verifyOrderCompletion();
        System.out.println("Standard user add-to-cart test passed!");
    }

    @Test
    public void performanceUserSortingTest() {
        performLogin("performance_glitch_user", "secret_sauce");
        resetApplicationState();

        sortItemsBy("za");
        addItemsToShoppingCart(1);
        
        openCartPage();
        String cartProductName = getFirstItemInCartName();
        System.out.println("Product in the cart: " + cartProductName);

        proceedToCheckout("John", "Doe", "12345");
        String checkoutPageProductName = getItemNameDuringCheckout();

        Assertions.assertEquals(cartProductName, checkoutPageProductName, "Error: Product name mismatch!");

        String displayedTotalPrice = retrieveTotalPrice();
        System.out.println("Total Price: " + displayedTotalPrice);

        finalizeCheckout();
        verifyOrderCompletion();
        System.out.println("Performance user sorting test passed!");
    }

    private void performLogin(String usernameInput, String passwordInput) {
        browser.findElement(By.id("user-name")).sendKeys(usernameInput);
        browser.findElement(By.id("password")).sendKeys(passwordInput);
        browser.findElement(By.id("login-button")).click();
    }

    private void resetApplicationState() {
        waitFor.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn"))).click();
        waitFor.until(ExpectedConditions.elementToBeClickable(By.id("reset_sidebar_link"))).click();
    }

    private void addItemsToShoppingCart(int numberOfItems) {
        List<WebElement> addToCartButtons = browser.findElements(By.className("btn_inventory"));
        for (int i = 0; i < numberOfItems && i < addToCartButtons.size(); i++) {
            addToCartButtons.get(i).click();
        }
    }

    private void openCartPage() {
        browser.findElement(By.className("shopping_cart_link")).click();
    }

    private int getCartItemsCount() {
        return browser.findElements(By.className("inventory_item_name")).size();
    }

    private void proceedToCheckout(String firstName, String lastName, String zipCode) {
        browser.findElement(By.id("checkout")).click();
        browser.findElement(By.id("first-name")).sendKeys(firstName);
        browser.findElement(By.id("last-name")).sendKeys(lastName);
        browser.findElement(By.id("postal-code")).sendKeys(zipCode);
        browser.findElement(By.id("continue")).click();
    }

    private String retrieveTotalPrice() {
        return browser.findElement(By.className("summary_total_label")).getText();
    }

    private void finalizeCheckout() {
        browser.findElement(By.id("finish")).click();
    }

    private void verifyOrderCompletion() {
        WebElement orderSuccessMessage = waitFor.until(ExpectedConditions.visibilityOfElementLocated(By.className("complete-header")));
        Assertions.assertTrue(orderSuccessMessage.getText().toLowerCase().contains("thank you for your order"),
                "Test Failed: Order completion message not found.");
    }

    private String getLoginErrorMessage() {
        return browser.findElement(By.cssSelector("h3[data-test='error']")).getText();
    }

    private void sortItemsBy(String sortOrder) {
        WebElement sortingDropdown = waitFor.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
        sortingDropdown.click();
        WebElement selectedOption = waitFor.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[@value='" + sortOrder + "']")));
        selectedOption.click();
    }

    private String getFirstItemInCartName() {
        return waitFor.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name"))).getText();
    }

    private String getItemNameDuringCheckout() {
        return waitFor.until(ExpectedConditions.visibilityOfElementLocated(By.className("inventory_item_name"))).getText();
    }

    @AfterEach
    public void tearDown() {
        browser.quit();
    }
}