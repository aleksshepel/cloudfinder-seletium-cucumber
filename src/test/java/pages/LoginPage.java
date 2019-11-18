package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Params;

public class LoginPage extends PageObject {
    private static final String PAGE_TITLE = "Cloudfinder — Login";
    private String URL = Params.MAIN_URL + "/#/login";
    private static Logger LOG = Logger.getLogger(LoginPage.class);

    public LoginPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(id = "username")
    private WebElement usernameInput;
    @FindBy(id = "password")
    private WebElement passwordInput;
    @FindBy(xpath = "//*[@type='submit']")
    private WebElement loginButton;

    @Override
    public boolean isPageLoaded() {
        if(isElementDisplayed(loginButton)
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

    /**
     * User login username/password.
     *
     * @param username - character sequence with user email for login
     * @param password - character sequence with user password for login
     * @param <T> -
     * @return - returns an appropriate PageObject depending on current url
     */
    public <T extends PageObject> T login(String username, String password) {
        enterTextIn(usernameInput, username);
        enterTextIn(passwordInput, password);
        clickOn(loginButton, "loginButton");
        if (isUrlContains("#/dashboard")) {
            LOG.info("User has been logged in successfully");
            return (T) new DashboardPage(driver);
        }
        else if (isUrlContains("#/login/loginViaCloudfinderUserFail")) {
            LOG.info("Use got navigated to the Login failure page");
            return (T) new LoginFailurePage(driver);
        }
        else {
            LOG.error("Unexpected behaviour. Fixing in needed");
            return null;
        }
    }


}
