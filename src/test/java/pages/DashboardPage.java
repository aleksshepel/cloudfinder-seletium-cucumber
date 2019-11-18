package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Params;

public class DashboardPage extends HomePage {
    private static final String PAGE_TITLE = "Cloudfinder — Dashboard";
    private static String URL = Params.MAIN_URL + "/#/dashboard";
    private static Logger LOG = Logger.getLogger(DashboardPage.class);

    public DashboardPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//*[text() = 'Recent activity reports']")
    private WebElement recentActivityReports;



    @Override
    public boolean isPageLoaded() {
        if( isElementDisplayed(recentActivityReports)
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
