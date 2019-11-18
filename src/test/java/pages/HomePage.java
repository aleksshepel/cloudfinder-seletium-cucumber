package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public abstract class HomePage extends PageObject {

    public HomePage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//input[@type='search']")
    private WebElement searchInput;

    @FindBy(xpath = "//a[@href= '#/reports']")
    private WebElement reportsTab;


    @Override
    public boolean isPageLoaded() {
        return false;
    }

    public SearchPage searchForBackupObjects(String tag){
        enterTextInAndPressEnter(searchInput, tag);
        return new SearchPage(driver);
    }

    public ReportsPage navigateToReportsPage(){
        clickOn(reportsTab, "reportsTab");
        return new ReportsPage(driver);
    }


}
