package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

import org.testng.Assert;
import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;


public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(priority = 1, description = "Verify registration happens correctly", groups = {"Sanity"})
    @Parameters({"username", "password"})
    public void TestCase01(@Optional("testUser") String username,
            @Optional("abc@123") String password) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");


        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(username, password, true);
        // if (!status) {
        // logStatus("TestCase 1", "Test Case Fail. User Registration Fail", "FAIL");
        // logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" :
        // "FAIL");


        // // Return False as the test case Fails
        // return false;
        // } else {
        // logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "PASS");
        // }
        Assert.assertTrue(status, "User Registration Failed");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;


        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, password);
        // logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        // if (!status) {
        // logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" :
        // "FAIL");
        // return false;

        Assert.assertTrue(status, "User Registration failed");



        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();
        // logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" :
        // "FAIL");


        // return status;

        Assert.assertTrue(status, "logout failed");
    }

    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s",
                String.valueOf(java.time.LocalDateTime.now()), type, message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType,
                    description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    @Test(priority = 2, description = "Verify re-registering an already registered user fails",
            groups = {"Sanity"})
    @Parameters({"username", "password"})
    public void TestCase02(@Optional("testUser") String username,
            @Optional("abc@123") String password) throws InterruptedException {
        Boolean status;
        logStatus("Start Testcase",
                "Test Case 2: Verify User Registration with an existing username ", "DONE");

        takeScreenshot(driver, "Testcase02", "testSuccess");
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        // if (!status) {
        // logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" :
        // "FAIL");
        // return false;
        Assert.assertTrue(status, "User Registraion failed");



        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;


        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);


        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        // logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" :
        // "PASS");
        // return !status;
        Assert.assertFalse(status, "User Registration done");

    }

    @Test(priority = 3, description = "Verify the functionality of search text box",
            groups = {"Sanity"})
    @Parameters({"product"})
    public void TestCase03(String product) throws InterruptedException {
        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;


        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();


        // Search for the "yonex" product
        status = homePage.searchForProduct(product);
        // if (!status) {
        // logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
        // return false;
        Assert.assertTrue(status, "Unable to search for given product");



        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();


        // Verify the search results are available
        // if (searchResults.size() == 0) {
        // logStatus("TestCase 3", "Test Case Failure. There were no results for the given search
        // string", "FAIL");
        // return false;
        // }



        Assert.assertTrue(searchResults.size() > 0, "No results for the given search");

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);


            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            // if (!elementText.toUpperCase().contains("YONEX")) {
            // logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values:
            // " + elementText,
            // "FAIL");
            // return false;

            // }
            Assert.assertTrue(elementText.toUpperCase().contains(product),
                    "searched product not contains Yonex");
        }


        logStatus("Step Success", "Successfully validated the search results ", "PASS");


        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        // if (status) {
        // logStatus("TestCase 3", "Test Case Pass. Invalid keyword returned results", "Pass");
        // return false;
        // }
        Assert.assertTrue(status, "Products found");


        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        // if (searchResults.size() == 0) {
        // if (homePage.isNoResultFound()) {
        // logStatus("Step Success", "Successfully validated that no products found message is
        // displayed", "PASS");
        // }
        // logStatus("TestCase 3", "Test Case PASS. Verified that no search results were found for
        // the given text",
        // "PASS");
        // } else {
        // logStatus("TestCase 3", "Test Case Fail. Expected: no results , actual: Results were
        // available", "FAIL");
        // return false;
        // }


        // return true;

        Assert.assertTrue(searchResults.size() == 0, "No products found");
    }
    // @Test(priority = 4 ,description = "Verify the existence of size chart for certain items and
    // validate contents of size chart ",groups="Regression")

    // @Parameters({"Product1"})
    @Test(priority = 4,
            description = "Verify the existence of size chart for certain items and validate contents of size chart",
            groups = {"Regression"})
    @Parameters({"ProductName"})
    public void TestCase04(String ProductName) throws InterruptedException {
        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct(ProductName);
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            // if (result.verifySizeChartExists()) {
            // logStatus("Step Success", "Successfully validated presence of Size Chart Link",
            // "PASS");

            Assert.assertTrue(result.verifySizeChartExists(),
                    "Test Case Fail. Failure to open Size Chart");
            // Verify if size dropdown exists
            status = result.verifyExistenceofSizeDropdown(driver);

            Assert.assertTrue(status, "Validated presence of drop down Failed");
            logStatus("Step Success", "Validated presence of drop down", status ? "PASS" : "FAIL");

            // Open the size chart
            Assert.assertTrue(result.openSizechart(),
                    "Test Case Fail. Size Chart Link does not exist");
            // if (result.openSizechart()) {
            // // Verify if the size chart contents matches the expected values
            // if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody,
            // driver)) {
            // logStatus("Step Success", "Successfully validated contents of Size Chart Link",
            // "PASS");
            // } else {
            // logStatus("Step Failure", "Failure while validating contents of Size Chart Link",
            // "FAIL");
            // status = false;
            // }
            Assert.assertTrue(result.validateSizeChartContents(expectedTableHeaders,
                    expectedTableBody, driver),
                    "Failure while validating contents of Size Chart Link");
            // Close the size chart modal
            status = result.closeSizeChart(driver);
            Assert.assertTrue(status, "Failed to close the driver");
            // } else {
            // logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
            // // return false;
            // }

            // } else {
            // logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
            // return false;
        }

        logStatus("TestCase 4", "End Test Case: Validated Size Chart Details",
                status ? "PASS" : "FAIL");
        // return status;
    }

    @Test(priority = 5,
            description = "Verify that a new user can add multiple products in to the cart and Checkout",
            groups = {"Sanity"})
    @Parameters({"Product1", "Product2", "address"})
    public void TestCase05(String Product1, String Product2, String address)
            throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");


        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();


        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");
        // }
        Assert.assertTrue(status, "User Registration has failed");


        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;


        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();


        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" :
        // "FAIL");
        // }
        Assert.assertTrue(status, "User login has failed");


        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();


        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct(Product1);
        Assert.assertTrue(status, "Product-YONEX has been found");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct(Product2);
        Assert.assertTrue(status, "Product-Tan has been found");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");


        // Click on the checkout button
        homePage.clickCheckout();


        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);


        // Place the order
        checkoutPage.placeOrder();


        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));


        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        Assert.assertTrue(status, "url ends with thanks");
        // Go to the home page
        homePage.navigateToHome();


        // Log out the user
        homePage.PerformLogout();


        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ",
                status ? "PASS" : "FAIL");

    }

    @Test(priority = 6, description = "Verify that the contents of the cart can be edited",
            groups = {"Regression"})
    @Parameters({"Product1TC06", "Product2TC06"})
    public void TestCase06(String Product1TC06, String Product2TC06) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);


        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("Step Failure", "User Perform Register Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ?
        // "PASS" : "FAIL");
        // return false;
        // }

        Assert.assertTrue(status, "User registration failed");
        lastGeneratedUserName = registration.lastGeneratedUsername;


        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ?
        // "PASS" : "FAIL");
        // return false;
        // }
        Assert.assertTrue(status, "User login failed");


        homePage.navigateToHome();
        status = homePage.searchForProduct(Product1TC06);
        Assert.assertTrue(status, "Product-Xtend has been found");
        homePage.addProductToCart("Xtend Smart Watch");


        status = homePage.searchForProduct(Product2TC06);
        Assert.assertTrue(status, "Product-Yarine has been found");
        homePage.addProductToCart("Yarine Floor Lamp");


        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);


        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);


        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);


        homePage.clickCheckout();


        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");


        checkoutPage.placeOrder();


        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(
                    ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());

        }


        status = driver.getCurrentUrl().endsWith("/thanks");
        Assert.assertTrue(status, "url ends with thanks");


        homePage.navigateToHome();
        homePage.PerformLogout();


        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ",
                status ? "PASS" : "FAIL");

    }


    /*
     * Verify that the cart contents are persisted after logout
     */
    @Test(priority = 7,
            description = "Verify that the contents made to the cart are saved against the user's login details",
            groups = {"Regression"})
    @Parameters({"Product1TC07", "Product2TC07"})
    public void TestCase07(String Product1TC07, String Product2TC07) throws InterruptedException {
        Boolean status = false;
        List<String> expectedResult =
                Arrays.asList("Stylecon 9 Seater RHS Sofa Set ", "Xtend Smart Watch");


        logStatus("Start TestCase",
                "Test Case 7: Verify that cart contents are persisted after logout", "DONE");


        Register registration = new Register(driver);
        Login login = new Login(driver);
        Home homePage = new Home(driver);


        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 7: Verify that cart contents are persited after
        // logout: ",
        // status ? "PASS" : "FAIL");
        // return false;
        // }
        Assert.assertTrue(status, "User Register has Failed");
        lastGeneratedUserName = registration.lastGeneratedUsername;


        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase", "Test Case 7: Verify that cart contents are persited after
        // logout: ",
        // status ? "PASS" : "FAIL");
        // return false;
        // }
        Assert.assertTrue(status, "User Login has Failed");


        homePage.navigateToHome();
        status = homePage.searchForProduct(Product1TC07);

        Assert.assertTrue(status, "Product-Stylecon has been found");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");


        status = homePage.searchForProduct(Product2TC07);
        Assert.assertTrue(status, "Product-Xtend has been found");
        homePage.addProductToCart("Xtend Smart Watch");


        homePage.PerformLogout();


        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        Assert.assertTrue(status, "User Login Success after logout");

        status = homePage.verifyCartContents(expectedResult);

        Assert.assertTrue(status, "Cart contents verification failed");


        // logStatus("End TestCase", "Test Case 7: Verify that cart contents are persisted after
        // logout: ",
        // status ? "PASS" : "FAIL");


        homePage.PerformLogout();

    }

    @Test(priority = 8,
            description = "Verify that insufficient balance error is thrown when the wallet balance is not enough",
            groups = {"Sanity"})
    @Parameters({"ProductTC08", "Quantity"})
    public void TestCase08(String ProductTC08, int Quantity) throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase",
                "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");


        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase",
        // "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is
        // not enough: ",
        // status ? "PASS" : "FAIL");
        // return false;
        Assert.assertTrue(status, "User Perform Registration Failed");

        lastGeneratedUserName = registration.lastGeneratedUsername;


        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // logStatus("End TestCase",
        // "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is
        // not enough: ",
        // status ? "PASS" : "FAIL");
        // return false;



        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("ProductTC08");
        Assert.assertTrue(status, "Product-Stylecon has not been found");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set ");


        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", Quantity);


        homePage.clickCheckout();


        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");


        checkoutPage.placeOrder();
        Thread.sleep(3000);


        status = checkoutPage.verifyInsufficientBalanceMessage();
        Assert.assertFalse(status,
                "insufficient balance error is thrown when the wallet balance is enough");

        // logStatus("End TestCase",
        // "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is
        // not enough: ",
        // status ? "PASS" : "FAIL");



    }

    @Test(priority = 10,
            description = "Verify that a product added to a cart is available when a new tab is added",
            dependsOnMethods = {"TestCase10"}, groups = {"Regression"})
    public void TestCase09() throws InterruptedException {
        Boolean status = false;


        logStatus("Start TestCase",
                "Test Case 9: Verify that product added to cart is available when a new tab is opened",
                "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase09");


        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("TestCase 9",
        // "Test Case Failure. Verify that product added to cart is available when a new tab is
        // opened",
        // "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase09");
        // }

        Assert.assertTrue(status, "User Registration Failed");
        lastGeneratedUserName = registration.lastGeneratedUsername;


        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase9");
        // logStatus("End TestCase",
        // "Test Case 9: Verify that product added to cart is available when a new tab is opened",
        // status ? "PASS" : "FAIL");
        // }

        Assert.assertTrue(status, "User Perform login failed");

        Home homePage = new Home(driver);
        homePage.navigateToHome();


        status = homePage.searchForProduct("YONEX");
        Assert.assertTrue(status, "Product-Yonex related search has not been found");

        homePage.addProductToCart("YONEX Smash Badminton Racquet");


        String currentURL = driver.getCurrentUrl();


        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);


        driver.get(currentURL);
        Thread.sleep(2000);


        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);
        Assert.assertTrue(status, "Cart contents of the product has not been returned");


        driver.close();


        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);


        logStatus("End TestCase",
                "Test Case 9: Verify that product added to cart is available when a new tab is opened",
                status ? "PASS" : "FAIL");
        takeScreenshot(driver, "EndTestCase", "TestCase09");



    }


    @Test(priority = 9,
            description = "Verify that a product added to a cart is available when a new tab is added",
            groups = {"Regression"})
    public void TestCase10() throws InterruptedException {
        Boolean status = false;


        logStatus("Start TestCase",
                "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
                "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase10");


        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("TestCase 10",
        // "Test Case Failure. Verify that the Privacy Policy, About Us are displayed correctly ",
        // "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase10");
        // }

        Assert.assertTrue(status, "User registration failed");
        lastGeneratedUserName = registration.lastGeneratedUsername;


        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase10");
        // logStatus("End TestCase",
        // "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        // status ? "PASS" : "FAIL");
        // }
        Assert.assertTrue(status, "User login failed");

        Home homePage = new Home(driver);
        homePage.navigateToHome();


        String basePageURL = driver.getCurrentUrl();


        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);


        // if (!status) {
        // logStatus("Step Failure", "Verifying new tab opened has Privacy Policy page heading
        // failed, status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase10");
        // logStatus("End TestCase",
        // "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        // status ? "PASS" : "FAIL");
        // }
        Assert.assertTrue(status,
                "Verifying new tab opened has Privacy Policy page heading failed");

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
        // if (!status) {
        // logStatus("Step Failure", "Verifying new tab opened has Privacy Policy page heading
        // failed", status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase10");
        // logStatus("End TestCase",
        // "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        // status ? "PASS" : "FAIL");
        // }

        Assert.assertTrue(status,
                "Verifying new tab opened has Privacy Policy page heading failed");
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();


        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        // if (!status) {
        // logStatus("Step Failure", "Verifying new tab opened has Terms Of Service page heading
        // failed", status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase10");
        // logStatus("End TestCase",
        // "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
        // status ? "PASS" : "FAIL");
        // }

        Assert.assertTrue(status,
                "Verifying new tab opened has Terms Of Service page heading failed");
        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);


        logStatus("End TestCase",
                "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly ",
                "PASS");
        takeScreenshot(driver, "EndTestCase", "TestCase10");



    }

    @Test(priority = 11, description = "Verify that the contact us dialog works fine",
            groups = {"Regression"})
    @Parameters({"ContactusUserNameTC11", "ContactUsEmailTC11", "QueryContentTC11"})
    public void TestCase11(String ContactusUserNameTC11, String ContactUsEmailTC11,
            String QueryContentTC11) throws InterruptedException {
        logStatus("Start TestCase",
                "Test Case 11: Verify that contact us option is working correctly ", "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase11");


        Home homePage = new Home(driver);
        homePage.navigateToHome();


        driver.findElement(By.xpath("//*[text()='Contact us']")).click();


        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        // name.sendKeys("crio user");
        name.sendKeys(ContactusUserNameTC11);
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        // email.sendKeys("criouser@gmail.com");
        email.sendKeys(ContactUsEmailTC11);

        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        // message.sendKeys("Testing the contact us page");
        message.sendKeys(QueryContentTC11);


        WebElement contactUs = driver.findElement(By.xpath(
                "/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));


        contactUs.click();


        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(contactUs));


        logStatus("End TestCase",
                "Test Case 11: Verify that contact us option is working correctly ", "PASS");


        takeScreenshot(driver, "EndTestCase", "TestCase11");



    }

    @Test(priority = 12,
            description = "Ensure that the Advertisement Links on the QKART page are clickable",
            groups = {"Sanity"})
    @Parameters({"ProductTC12", "AddressTC12"})
    public void TestCase12(String ProductTC12, String AddressTC12) throws InterruptedException {
        Boolean status = false;
        logStatus("Start TestCase",
                "Test Case 12: Ensure that the links on the QKART advertisement are clickable",
                "DONE");
        takeScreenshot(driver, "StartTestCase", "TestCase12");


        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        // if (!status) {
        // logStatus("TestCase 12",
        // "Test Case Failure. Ensure that the links on the QKART advertisement are clickable",
        // "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase12");
        // }
        Assert.assertTrue(status, "User registration failed");
        lastGeneratedUserName = registration.lastGeneratedUsername;


        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // if (!status) {
        // logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
        // takeScreenshot(driver, "Failure", "TestCase 12");
        // logStatus("End TestCase",
        // "Test Case 12: Ensure that the links on the QKART advertisement are clickable",
        // status ? "PASS" : "FAIL");
        // }

        Assert.assertTrue(status, "User perform login failed");
        Home homePage = new Home(driver);
        homePage.navigateToHome();


        // status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct(ProductTC12);
        Assert.assertTrue(status, "Product - YONEX related search has not been found");

        // homePage.addProductToCart("YONEX Smash Badminton Racquet");
        homePage.addProductToCart(ProductTC12);
        // homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.changeProductQuantityinCart(ProductTC12, 1);
        homePage.clickCheckout();


        Checkout checkoutPage = new Checkout(driver);
        // checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        // checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.addNewAddress(AddressTC12);
        checkoutPage.selectAddress(AddressTC12);
        checkoutPage.placeOrder();
        Thread.sleep(3000);


        String currentURL = driver.getCurrentUrl();


        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));


        status = Advertisements.size() == 3;
        logStatus("Step ", "Verify that 3 Advertisements are available", status ? "PASS" : "FAIL");


        WebElement Advertisement1 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();


        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step ", "Verify that Advertisement 1 is clickable ", status ? "PASS" : "FAIL");


        driver.get(currentURL);
        Thread.sleep(3000);


        WebElement Advertisement2 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();


        status = !driver.getCurrentUrl().equals(currentURL);
        logStatus("Step ", "Verify that Advertisement 2 is clickable ", status ? "PASS" : "FAIL");


        logStatus("End TestCase",
                "Test Case 12:  Ensure that the links on the QKART advertisement are clickable",
                status ? "PASS" : "FAIL");

    }

}


