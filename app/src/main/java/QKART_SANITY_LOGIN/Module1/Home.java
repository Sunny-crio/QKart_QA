package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logoutbutton = driver.findElement(By.xpath("//button[text()='Logout']"));
           
            logoutbutton.click();

            // Wait for Logout to Complete
            Thread.sleep(1000);

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search box
            WebElement searchboxElement = this.driver.findElementByXPath(
                    "//div[@class='MuiFormControl-root MuiTextField-root search-desktop css-i44wyl']/div/input");

            searchboxElement.clear();
            searchboxElement.sendKeys(product);

            Thread.sleep(3000);



            return true;
        } catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {};
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver.findElements(By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']"));


            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false

            WebElement noproductsfoundElement = driver.findElement(By.xpath("//h4[text()=' No products found ']"));
            if(noproductsfoundElement.isDisplayed()){
                String ExpectedText = " No products found ";
                String ActualText= noproductsfoundElement.getText();
                if(ActualText.equals(ExpectedText)){
                    status = true;
                }
            }
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through each product on the page to find the WebElement corresponding to the
             * matching productName
             * 
             * Click on the "ADD TO CART" button for that element
             * 
             * Return true if these operations succeeds
             */
        List<WebElement> productNameWebElements = driver.findElements(By.xpath("//p[@class='MuiTypography-root MuiTypography-body1 css-yg30e6']"));

        List<WebElement> addToCartButtonElements = driver.findElements(By.xpath(" //button[text()='Add to cart']"));
        // for (WebElement productname : productNameWebElements){
        //     String productText = productname.getText();
        //     if(productText.equals(productName)){
        //      return true;
        //     }
        // }

        for ( int i =0; i< productNameWebElements.size(); i++){
            WebElement productnameWebElement = productNameWebElements.get(i);
            String actualproductname = productnameWebElement.getText();
            if(actualproductname.equals(productName)){

                WebElement addtoCartButtonElement= addToCartButtonElements.get(i);
                addtoCartButtonElement.click();

            }

        }




            System.out.println("Unable to find the given product");
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    
    }
    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
            driver.findElement(By.xpath(" //button[text()='Checkout']")).click();
            Thread.sleep(3000);
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

            // Find the item on the cart with the matching productName


            // Increment or decrement the quantity of the matching product until the current
            // quantity is reached (Note: Keep a look out when then input quantity is 0,
            // here we need to remove the item completely from the cart)

List<WebElement> productNameElements = driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']/div[1]"));

List<WebElement> CurrentQuantityElements = driver.findElements(By.xpath("//div[@data-testid='item-qty']"));

List<WebElement> plusButtonElements = driver.findElements(By.xpath("//*[@data-testid='AddOutlinedIcon']"));

List<WebElement> minusButtonElements = driver.findElements(By.xpath("//*[@data-testid='RemoveOutlinedIcon']"));

for ( int i=0; i<productNameElements.size();i++){
    WebElement productNameElement = productNameElements.get(i);
    String actualProductName = productNameElement.getText();

    if(actualProductName.equals(productName)){

        while(true){

        WebElement CurrentQuantityElement = CurrentQuantityElements.get(i);
        String CurrentQuantityString = CurrentQuantityElement.getText();
        int currentQuantity= Integer.parseInt(CurrentQuantityString);
        //getText() always returns string
        if(currentQuantity<quantity){
            WebElement plusButtonElement=plusButtonElements.get(i);
            plusButtonElement.click();
            Thread.sleep(1000);
        }
        else if(currentQuantity>quantity){
            WebElement minusButtonElement = minusButtonElements.get(i);
            minusButtonElement.click();
            Thread.sleep(1000);
        }
        if ( currentQuantity==quantity){
            break;
        }
      

    }}
}

       return false;
            
           
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println("exception occurred when updating cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6

            // Get all the cart items as an array of webelements

           List<WebElement> CartContentsElements = driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']"));


            List<WebElement> ProductNameElements = driver.findElements(By.xpath("//div[@class='MuiBox-root css-1gjj37g']/div[1]"));

       
            // Iterate through expectedCartContents and check if item with matching product

            for (int i=0; i<expectedCartContents.size();i++){
                WebElement CartContentElement = CartContentsElements.get(i);
                WebElement ProductNameElement = ProductNameElements.get(i);
                String actualProductNameText= ProductNameElement.getText(); 
                String ExpectedCartcontent= expectedCartContents.get(i).toString();
                System.out.println(ExpectedCartcontent);
               
            if(actualProductNameText.equalsIgnoreCase(ExpectedCartcontent)){
                return true;
            }
            else{
                System.out.println(ExpectedCartcontent);
            }
            }
            // name is present in the cart


            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
