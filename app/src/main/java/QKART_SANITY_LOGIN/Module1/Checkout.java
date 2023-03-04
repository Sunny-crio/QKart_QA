package QKART_SANITY_LOGIN.Module1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */

             WebElement addnewaddressElement = driver.findElement(By.xpath(" //button[text()='Add new address']"));
             addnewaddressElement.click();
             Thread.sleep(1000);
             WebElement enteraddress = driver.findElement(By.xpath("//textarea[@placeholder='Enter your complete address']"));
             enteraddress.sendKeys(addresString);
             Thread.sleep(1000);
             WebElement addaddressbuttonElement = driver.findElement(By.xpath("//button[text()='Add']"));
             addaddressbuttonElement.click();

            return false;

        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */
           List<WebElement> selectaddressElements = driver.findElements(By.xpath("//div[@class='address-item not-selected MuiBox-root css-0']/div[1]/p"));

           for(int i=0; i < selectaddressElements.size();i++){
             WebElement selectaddressElement = selectaddressElements.get(i);
             String actualaddressText = selectaddressElement.getText();
             if(actualaddressText.equals(addressToSelect)){
                selectaddressElement.click();
                break;
             }
           }
             
            System.out.println("Unable to find the given address");
            return false;
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }

    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it

            WebElement placeorderelement = driver.findElement(By.xpath("//*[text()='PLACE ORDER']"));
        placeorderelement.click();
      
            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {

        boolean status = false;
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 08: MILESTONE 7

            WebElement InsufficientBalanceMessage = driver.findElement(By.xpath("//*[text()='You do not have enough balance in your wallet for this purchase']"));

          

            if(InsufficientBalanceMessage.isDisplayed()){

                String actualmessage = InsufficientBalanceMessage.getText();
                String expectedmessage = "You do not have enough balance in your wallet for this purchase";
                if(actualmessage.equals(expectedmessage)){
                    status = true;
                }
                
            }
            return status;
        
           
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
