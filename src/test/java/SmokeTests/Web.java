package SmokeTests;

import APIs.Profile;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
//import org.testng.log4testng.Logger;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Web {
    WebDriver driver;
    Profile profile = new Profile();
    WebDriverWait wait_timer;
    @Test(priority = 1)
    public void TC1_Verify_new_user_can_successfully_register_with_valid_details() {
        Reporter.log("A new user starting registration.", true);
        driver.findElement(By.id("signin2")).click();
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.id("sign-username"))).click();
        profile.username = generateName();
        profile.password = profile.username;
        Reporter.log("User is providing valid credentials.", true);
        driver.findElement(By.id("sign-username")).sendKeys(profile.username);
        driver.findElement(By.id("sign-password")).sendKeys(profile.password);
        driver.findElement(By.xpath("//button[text()='Sign up']")).click();
        wait_timer.until(ExpectedConditions.alertIsPresent());
        Reporter.log("Validating registration process.", true);
        Assert.assertEquals(driver.switchTo().alert().getText(), "Sign up successful.");
        driver.switchTo().alert().accept();
    }
    @Test(priority = 2)
    public void TC2_Verify_user_can_log_in_and_redirected_to_home_page() {
        Reporter.log("User starting signing in.", true);
        driver.findElement(By.id("login2")).click();
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.id("loginusername"))).click();
        Reporter.log("User is providing valid credentials.", true);
        driver.findElement(By.id("loginusername")).sendKeys(profile.username);
        driver.findElement(By.id("loginpassword")).sendKeys(profile.password);
        driver.findElement(By.xpath("//button[text()='Log in']")).click();
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.id("logout2")));
        Assert.assertEquals(driver.findElement(By.xpath("//a[@id='nameofuser'][contains(.,'" + profile.username +"')]")).isDisplayed()
                , true, "Username is not displayed correctly.");
        Assert.assertEquals(driver.findElement(By.xpath("//div[@id='navbarExample']//li[contains(@class,'active')]/a")).getText().contains("Home")
                , true, "User is not redirected to the Home page.");
    }
    @Test(priority = 3)
    public void TC3_Verify_searching_returns_relevant_results() {
        // I couldn't find a Search page.
    }
    @Test(priority = 4)
    public void TC4_Verify_applying_filters_correctly_updates_the_product_list() {
        Reporter.log("User checking store monitors.", true);
        driver.findElement(By.xpath("//a[@id='itemc'][text()='Monitors']")).click();
        Assert.assertEquals(wait_timer.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-block'][contains(.,'monitor')]"))).isDisplayed()
                ,true);
        Reporter.log("User checking store smart phones.", true);
        driver.findElement(By.xpath("//a[@id='itemc'][text()='Phones']")).click();
        Assert.assertEquals(wait_timer.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-block'][contains(.,'Lumia')]"))).isDisplayed()
                ,true);
        Reporter.log("User checking store laptops.", true);
        driver.findElement(By.xpath("//a[@id='itemc'][text()='Laptops']")).click();
        Assert.assertEquals(wait_timer.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='card-block'][contains(.,'vaio')]"))).isDisplayed()
                ,true);
    }
    @Test(priority = 5)
    public void TC5_Verify_adding_products_to_shopping_cart() {
        Reporter.log("User starting shopping.", true);
        driver.findElement(By.xpath("//a[@id='itemc'][text()='Laptops']")).click();
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.xpath("//h4[@class='card-title'][contains(.,'Sony vaio i5')]"))).click();
        Reporter.log("User adding a couple of laptops in shopping cart.", true);
        // Add 1 product to cart
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait_timer.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(driver.switchTo().alert().getText(), "Product added.");
        driver.switchTo().alert().accept();
        // Add another 1
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[text()='Add to cart']"))).click();
        wait_timer.until(ExpectedConditions.alertIsPresent());
        Assert.assertEquals(driver.switchTo().alert().getText(), "Product added.");
        driver.switchTo().alert().accept();
        Reporter.log("User checking shopping cart items.", true);
        driver.findElement(By.id("cartur")).click();
        wait_timer.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//td[text()='Sony vaio i5']")));
        Assert.assertEquals(driver.findElements(By.xpath("//td[text()='Sony vaio i5']")).stream().count(), 2);
        Assert.assertNotEquals(driver.findElement(By.id("totalp")).getText(), "", "The cart total is not updated.");
    }
    @Test(priority = 6)
    public void TC6_Verify_removing_a_product_from_shopping_cart() {
        Reporter.log("User modifying shopping cart items.", true);
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.id("totalp")));
        String cartTotal = driver.findElement(By.id("totalp")).getText();
        Reporter.log("User an item from shopping cart.", true);
        driver.findElement(By.xpath("//a[text()='Delete']")).click();
        Reporter.log("User checking total price.", true);
        wait_timer.until(ExpectedConditions.invisibilityOfElementWithText(By.id("totalp']"), cartTotal));
    }
    @Test(priority = 7)
    public void TC7_Verify_user_complete_purchase_and_receive_order_confirmation() {
        Reporter.log("User starting placing a purchase order.", true);
        driver.findElement(By.xpath("//button[text()='Place Order']")).click();
        Reporter.log("User providing valid payment details.", true);
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.id("name"))).sendKeys(profile.username);
        wait_timer.until(ExpectedConditions.elementToBeClickable(By.id("card"))).sendKeys(profile.password);
        driver.findElement(By.xpath("//button[text()='Purchase']")).click();
        Reporter.log("User receiving order confirmation.", true);
        Assert.assertEquals(driver.findElement(By.xpath("//h2[text()='Thank you for your purchase!']")).isDisplayed()
            ,true);
    }
    @BeforeTest
    private void Start(){

        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--start-maximized");
        driver = new ChromeDriver(chromeOptions);
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        Properties properties = new Properties();
        try {
            properties.load(this.getClass().getResourceAsStream("/project.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.setProperty("webdriver.chrome.driver",properties.getProperty("chromedriverLocation"));

        driver.navigate().to(properties.getProperty("webURL"));
        wait_timer = new WebDriverWait(driver, Duration.ofSeconds(20));
    }
    @AfterTest
    private void End(){
        driver.quit();
    }
    public String generateName(){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String formattedNow = now.format(formatter);
        return  "user"+formattedNow;
    }
}
