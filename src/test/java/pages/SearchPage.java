package pages;

import backupObjects.BackupObject;
import backupObjects.Contact;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;
import utils.Params;

import java.util.List;


public class SearchPage extends HomePage {
    private static final String PAGE_TITLE = "Cloudfinder — Search";
    private static String URL = Params.MAIN_URL + "/#/search";
    private static Logger LOG = Logger.getLogger(DashboardPage.class);

    public SearchPage(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @FindBy(xpath = "//div[@ng-include = \"'views/search/search-results.html'\"]//tr[@ng-repeat='result in searchResults']")
    private List<WebElement> searchResults;

    @FindBy(xpath = "//div[@ng-include = \"'views/search/search-result-details.html'\"]")
    private WebElement searchResultDetailsContainer;

    @FindBy(xpath = "//*[@class='detail-main detview scrollme ng-scope']")
    private WebElement detViewContainer;

    @FindBy(xpath = "//input[@value = 'View file']")
    private WebElement buttonViewFile;

    @FindBy(xpath = "//div[@ng-show='!detailsView']//button[@data-toggle = 'dropdown' and contains(.,'Action')]")
    private WebElement dropdownAction;

    @FindBy(xpath = "//form[@name = 'restoreForm']")
    private WebElement restoreForm;

    @FindBy(xpath = "//div[@id='s2id_autogen7']")
    private WebElement restoreToUserCombobox;

    @FindBy(xpath = "//div[@id='select2-drop']//div[@class='select2-search']/input[@type='text']")
    private WebElement inputSelectOne;

    @FindBy(xpath = "//button[text()='Proceed']")
    private WebElement buttonProceed;


    @Override
    public boolean isPageLoaded() {
        waitUntilVisible(By.xpath("//*[@class='side-list drop']"));
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

    public boolean isSearchResultEmpty(){
        return waitUntilAllVisible(searchResults).isEmpty();
    }

    public boolean isSearchResultsContainAnyMatch(String text){
        String[] keyWords = text.split(" ");
        for(WebElement result: searchResults){
            for(String word: keyWords){
                if (result.getText().contains(word))
                    return true;
            }
        }
        return false;
    }

    public void viewDetailsOfASearchResult(int index){
        clickOn(searchResults.get(index-1), "searchResult");
    }

    public void clickOnButtonViewFile(){
        scrollToElement(buttonViewFile);
        clickOn(buttonViewFile, "buttonViewFile");
    }

    public boolean isMainDetailsCorrect(BackupObject backupObject){
        waitUntilVisible(searchResultDetailsContainer);
        boolean answer = false;
        if(backupObject instanceof Contact) {
            answer = backupObject.mainDetails().entrySet()
                    .stream()
                    .filter(e -> StringUtils.isNotEmpty(e.getValue()))
                    .allMatch(e -> StringUtils.equals(getMainDetailsByKeyName(e.getKey()), e.getValue()));
        }
        return answer;
    }

    public boolean isDetViewCorrect(BackupObject backupObject){
        waitUntilVisible(detViewContainer);
        boolean answer = false;
        if(backupObject instanceof Contact) {

            answer = backupObject.detView().entrySet()
                    .stream()
                    .filter(e -> StringUtils.isNotEmpty(e.getValue()))
                    .allMatch(e -> StringUtils.equals(getDetViewByKeyName(e.getKey()), e.getValue()));
        }
        return answer;
    }

    public void selectSearchResultByCheckingBox(int index){
        searchResults.get(index-1).findElement(By.xpath(".//input[@type = 'checkbox']")).click();
        LOG.info("The checkbox left to search result clicked");
    }

    public void selectValueInDropdownAction(String value){
        clickOn(dropdownAction, "dropdownAction");
        By by = By.xpath(String.format("//div[@ng-show = '!detailsView']//a[text() = '%s']", value));
        waitUntilVisible(by);
        clickOn(by);
    }

    public void selectUserInRestoreToUserCombobox(String user){
        waitUntilVisible(restoreForm);
        clickOn(restoreToUserCombobox, "restoreToUserCombobox");
        waitUntilVisible(inputSelectOne).sendKeys(user);
        waitUntilVisible(By.xpath("//div[@id='select2-drop']//span[text()='"+user+"']")).click();

    }

    public void clickOnProceedButton(){
        clickOn(buttonProceed, "buttonProceed");
    }

    public void confirmRestore(String user){
        waitUntilVisible(By.xpath("//h2[text()='Confirm restore']"));
        String pattern = "You are about to restore to "+user;
        Assert.assertEquals(find(By.xpath("//div[starts-with(@ng-show, 'confirmation')]/p")).getText(),
                pattern);
        driver.findElement(By.xpath("//button[starts-with(@ng-show, 'confirmation')]")).click();
        waitUntilVisible(By.xpath("//h2[text() = 'Restore started!']"));
        String pattern2 = "Your restore was successfully initiated, view your dashboard to see when its done.";
        Assert.assertEquals(find(By.xpath("//div[@class = 'modal-body' and starts-with(@ng-show, 'restoreSuccessful')]/span")).getText(),
                pattern2);
        driver.findElement(By.xpath("//button[text() = 'Close']")).click();

    }

    private String getMainDetailsByKeyName(String keyName){
        return find(By.xpath(String
                .format("//*[contains(@ng-show, '%s') and not(@class='ng-binding ng-hide')]/following-sibling:: dd[1]",
                        keyName)))
                .getText();
    }

    private String getDetViewByKeyName(String keyName){
        return find(By.xpath(String
                .format("//*[contains(@ng-show, '%s') and not(@class='ng-binding ng-hide')]//dd",
                        keyName)))
                .getText();
    }

}
