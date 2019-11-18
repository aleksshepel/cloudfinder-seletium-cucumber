package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.Params;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A parent object class
 * Contains declaration of WebDriver instance, WebDriverWait instance
 */

public abstract class PageObject {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private static Logger LOG = Logger.getLogger(PageObject.class);
    private static int ATTEMPTS = 5;


    public PageObject(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Params.TIMEOUT_WAIT);
        PageFactory.initElements(this.driver, this);
    }

    protected abstract boolean isPageLoaded();

    /**
     * Common method to get current url of an appropriate pageobject class
     * @return - current url
     */
    protected String getCurrentUrl(){
        return driver.getCurrentUrl();
    }

    /**
     * Common method to get current title of an appropriate pageobject class
     * @return - current title
     */
    protected String getCurrentTitle(){
        return driver.getTitle();
    }

    /**
     * Base method for waiting  webelement visibility
     * @param webElement
     * @return - webelement
     */

    protected WebElement waitUntilVisible(WebElement webElement){
        return new WebDriverWait(driver, Params.TIMEOUT_WAIT)
                .until(ExpectedConditions.visibilityOf(webElement));
    }

    /**
     * Base method for waiting visibility of a webelement located
     * @param locator
     * @return - webelement
     */

    protected WebElement waitUntilVisible(By locator){
        return new WebDriverWait(driver, Params.TIMEOUT_WAIT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Base method for waiting visibility of all webelements
     * @param webElements
     * @return - List of webelements
     */

    protected List<WebElement> waitUntilAllVisible(List<WebElement> webElements){
        return new WebDriverWait(driver, Params.TIMEOUT_WAIT)
                .until(ExpectedConditions.visibilityOfAllElements(webElements));
    }

    /**
     * Returns boolean depending the fact whether url contains or not partial text
     * @param partialUrl - a char sequence of
     * @return - boolean
     */

    protected boolean isUrlContains(String partialUrl){
        WebDriverWait wait = new WebDriverWait(driver, Params.TIMEOUT_WAIT);
        try{
            return wait.until(ExpectedConditions.urlContains(partialUrl));
        } catch(TimeoutException e){
            return false;
        }
    }

    /**
     * Wait for visibility of a WebElement, sends keys in it and press Tab
     */
    protected void enterTextIn(WebElement element, String value) {
        try {
            waitUntilVisible(element).sendKeys(value+Keys.TAB);
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            ATTEMPTS--;
            if (ATTEMPTS > 0){
                enterTextIn(element, value);
            }
            else{
                LOG.error("Cannot interact with an element, because: " + e.getMessage());
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception ex) {
            LOG.error("Cannot interact with an element: " + ex.getMessage());
            ex.printStackTrace();
        }
        LOG.info("Text ['"+value+"'] is entered in the field");
    }

    /**
     * Wait for visibility of a WebElement located, sends keys in it and press Tab
     */
    protected void enterTextIn(By locator, String value) {
        try {
            waitUntilVisible(locator).sendKeys(value+Keys.TAB);
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            ATTEMPTS--;
            if (ATTEMPTS > 0){
                enterTextIn(locator, value);
            }
            else{
                LOG.error("Cannot interact with an element, because: " + e.getMessage());
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception ex) {
            LOG.error("Cannot interact with an element: " + ex.getMessage());
            ex.printStackTrace();
        }
        LOG.info("Text ['"+value+"'] is entered in the field");
    }

    /**
     * Wait for visibility of a WebElement sends keys in it and press Enter
     */
    protected void enterTextInAndPressEnter(WebElement element, String value) {
        try {
            waitUntilVisible(element).clear();
            element.sendKeys(value+Keys.ENTER);
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            ATTEMPTS--;
            if (ATTEMPTS > 0){
                enterTextIn(element, value);
            }
            else{
                LOG.error("Cannot interact with an element, because: " + e.getMessage());
                throw new AssertionError(e.getMessage());
            }
        } catch (Exception ex) {
            LOG.error("Cannot interact with an element: " + ex.getMessage());
            ex.printStackTrace();
        }
        LOG.info("Text ['"+value+"'] is entered in the field");
    }

    /**
     * Wait for visibility of a WebElement and click on it
     */

    protected void clickOn(WebElement element, String elementName) {
        try {
            waitUntilVisible(element).click();
            LOG.info(String.format("Element %s clicked", elementName));
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            ATTEMPTS--;
            if (ATTEMPTS > 0){
                clickOn(element, elementName);
            }
            else{
                LOG.error("Cannot interact with an element, because: " + e.getMessage());
                throw new AssertionError("Exception has been thrown: " + e.getMessage());
            }
        } catch (Exception ex) {
            LOG.error("Cannot interact with an element: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    /**
     * Wait for visibility of a WebElement and click on it
     */

    protected void clickOn(By locator) {
        try {
            waitUntilVisible(locator).click();
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            ATTEMPTS--;
            if (ATTEMPTS > 0){
                clickOn(locator);
            }
            else{
                LOG.error("Cannot interact with an element, because: " + e.getCause());
                throw new AssertionError("Exception has been thrown: " + e.getMessage());
            }
        } catch (Exception ex) {
            LOG.error("Cannot interact with an element: " + ex.getMessage());
            ex.printStackTrace();
        }
        LOG.info("Element with locator ["+locator+"] clicked");
    }

    protected WebElement find(By locator) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (StaleElementReferenceException | ElementClickInterceptedException e) {
            System.out.println("StaleElementReferenceException: " + e);
            ATTEMPTS--;
            if (ATTEMPTS > 0)
                find(locator);
            else{
                LOG.error("Cannot interact with an element, because: " + e.getCause());
                throw new AssertionError("Exception has been thrown: " + e.getMessage());
            }
        } catch (Exception ex) {
            LOG.error("Cannot interact with an element: " + ex.getMessage());
            ex.printStackTrace();
        }
        return driver.findElement(locator);
    }

    protected void 	scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }

    protected boolean isElementDisplayed(WebElement element){
        try {
            wait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }


}
