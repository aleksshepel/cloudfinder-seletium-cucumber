package stepDefinitions;

import backupObjects.BackupObject;
import backupObjects.Contact;
import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import pages.*;
import utils.Params;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StepDefections {

    private WebDriver driver;
    private LoginPage loginPage;
    //private LoginFailurePage loginFailurePage;
    private DashboardPage dashboardPage;
    private SearchPage searchPage;
    private ReportsPage reportsPage;
    private BackupObject backupObject;
    private static final Logger LOG = Logger.getLogger(StepDefections.class);

    @Before
    public void openBrowser () throws Exception {
        switch(Params.BROWSER.toLowerCase()){
            case "chrome":
                WebDriverManager.chromedriver().setup();
                driver = new ChromeDriver();
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
                break;
            default:
                throw new Exception("Can not work with this type of browser: "+Params.BROWSER);
        }
        driver.manage().window().maximize();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
    }

    @After
    public void closeBrowser (Scenario scenario){
        if(scenario.isFailed()){
            try {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.embed(screenshot, "image/png");
            } catch (WebDriverException e) {
                LOG.error(e.getMessage());
            }
        }

        driver.quit();
    }

    //Common steps
    @Given("I am on the Login page")
    public void go_to_Login_page (){
        driver.get(Params.MAIN_URL);
        loginPage = new LoginPage(driver);
        Assert.assertTrue(loginPage.isPageLoaded(), "The Login page hasn't been loaded");
    }

    @When("I sign in as \"([^\"]*)\" with \"([^\"]*)\"")
    public void sign_in(String email, String password){
        dashboardPage = loginPage.login(email, password);
    }
    @Then("I should be on the Dashboard page")
    public void should_be_on_the_dashboard_page (){
        Assert.assertTrue(dashboardPage.isPageLoaded(), "The Dashboard page hasn't been loaded");
    }

    @Given("A contact is backed up with parameters:")
    public void contact_is_backed_up (Map<String, String> contactData) {
        backupObject = new Contact(contactData.get("name"), contactData.get("email"),
                contactData.get("phone"), contactData.get("updated"), contactData.get("address"),
                contactData.get("category"), contactData.get("calendar"), contactData.get("account"));

        contactData.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    @When ("I enter a \"([^\"]*)\" in the Search field and hit Enter")
    public void enter_searchTag_in_the_search_field (String arg){
        searchPage = dashboardPage.searchForBackupObjects(arg);
    }

    @Then("I should be on the Search page")
    public void should_be_on_the_search_page (){
        Assert.assertTrue(searchPage.isPageLoaded(), "The Search page hasn't been loaded");
    }

    @And("I should see the contact info displayed in a search results")
    public void should_see_the_contact_info_in_a_search_results (){
        Assert.assertFalse(searchPage.isSearchResultEmpty());
    }

    @And("Contact info should contain a \"([^\"]*)\"")
    public void contact_info_should_contain_a_search_tag (String arg) {
        Assert.assertTrue(searchPage.isSearchResultsContainAnyMatch(arg));
    }

    @When("I click on the search result")
    public void click_on_the_search_result () {
        searchPage.viewDetailsOfASearchResult(1);
    }

    @Then("An additional info is displayed")
    public void additional_info_is_displayed () {
        Assert.assertTrue(searchPage.isMainDetailsCorrect(backupObject),
                "Additional info doesn't match backupObject parameters");
    }

    @When("I click on view file button")
    public void click_on_view_file_button () {
        searchPage.clickOnButtonViewFile();
    }

    @Then("A detailed view info is displayed")
    public void detailed_view_info_is_displayed () {
        Assert.assertTrue(searchPage.isDetViewCorrect(backupObject),
                "Detailed view info is displayed incorrectly");
    }

    //Restore a contact
    @When("I select the search result by checking the box left to it")
    public void select_search_result_by_checking_box_left (){
       searchPage.selectSearchResultByCheckingBox(1);
    }

    @And ("select \"([^\"]*)\" in Action above the search results")
    public void select_value_in_action_above_search_results (String action){
        searchPage.selectValueInDropdownAction(action);
    }

    @And("select {string} in the Restore to user combobox")
    public void select_account_in_restore_to_user_combobox(String account) {
        searchPage.selectUserInRestoreToUserCombobox(account);
    }

    @And("click Proceed button")
    public void click_proceed_button() {
        searchPage.clickOnProceedButton();
    }

    @And("confirm restoring")
    public void confirm_restoring(String account) {
        searchPage.confirmRestore(account);
    }

    @Then ("The restore task should be scheduled and will be processed after some time")
    public void restore_task_should_be_scheduled (){
        //TODO
    }

    @When ("I click on Reports tab")
    public void click_on_reports_tab (){
        reportsPage = searchPage.navigateToReportsPage();
    }

    @Then ("I should be on the Reports page")
    public void should_be_on_Reports_page (){
        Assert.assertTrue(reportsPage.isPageLoaded(),
                "The Reports page hasn't been loaded");
    }

    @And ("I should be able to track progress of the restore on the page by account")
    public void should_be_able_to_track_progress (String account){
        reportsPage.clickOnRestoreTab();
        Assert.assertTrue((reportsPage.getRestoreProgressResultsNumber(account) > 0),
                "There is no restore progress result");
        Assert.assertEquals(reportsPage.getLastRestoreProgressStatus(account), "In progress");
    }


}
