package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Params;

public class LoginFailurePage extends PageObject {
    private static final String PAGE_TITLE = "Cloudfinder — Login failure";
    private static String URL = Params.MAIN_URL + "/#/login/loginViaCloudfinderUserFail";
    private static Logger LOG = Logger.getLogger(LoginFailurePage.class);


    public LoginFailurePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@class = 'alert alert-danger ng-binding']")
    private WebElement errorNotification;

    @Override
    public boolean isPageLoaded() {
        if(isElementDisplayed(errorNotification)
                && getCurrentUrl().equals(URL)
                && getCurrentTitle().equals(PAGE_TITLE)){
            LOG.info(this.getClass().getName() + " is loaded");
            return true;
        } else {
            LOG.error(this.getClass().getName() + " is not loaded properly. Current url: " + getCurrentUrl()
                    + " and Current title: " + getCurrentTitle());
            return false;
        }
    }
}
