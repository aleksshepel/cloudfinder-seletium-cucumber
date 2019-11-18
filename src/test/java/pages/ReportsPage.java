package pages;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utils.Params;

public class ReportsPage extends HomePage {
    private static final String PAGE_TITLE = "Cloudfinder — Status Reports";
    private static String URL = Params.MAIN_URL + "/#/status-reports";
    private static Logger LOG = Logger.getLogger(DashboardPage.class);

    public ReportsPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@ng-show= 'loadingMore']")
    private WebElement progressBar;

    @Override
    public boolean isPageLoaded() {
        waitUntilVisible(By.xpath("//a[text()='Backup']"));
        if(getCurrentUrl().startsWith(URL)
                && getCurrentTitle().equals(PAGE_TITLE)){
            LOG.info(this.getClass().getName() + " is loaded");
            return true;
        } else {
            LOG.error(this.getClass().getName() + " is not loaded properly. Current url: " + getCurrentUrl()
                    + " and Current title: " + getCurrentTitle());
            return false;
        }
    }

    public void clickOnRestoreTab(){
        clickOn(By.xpath("//a[text()='Restore']"));
    }

    public int getRestoreProgressResultsNumber(String account){
        return driver.findElements(By
                    .xpath("//table[@class='table table-condensed']//tr[contains(.,'"+account+"')]"))
                    .size();
    }

    public String getLastRestoreProgressStatus(String account){
        return driver.findElements(By
                .xpath("//table[@class='table table-condensed']//tr[contains(.,'"+account+"')]"))
                .get(0)
                .findElement(By.xpath(".//span[contains(@class, 'badge label')]"))
                .getText();
    }
}
