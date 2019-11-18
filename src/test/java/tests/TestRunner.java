package tests;

import io.cucumber.testng.CucumberFeatureWrapper;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.PickleEventWrapper;
import io.cucumber.testng.TestNGCucumberRunner;
import org.testng.annotations.*;
import utils.Params;

@CucumberOptions (
        features =  "src/test/resources/features",
        glue = {"stepDefinitions"} ,
        //tags = {"@SearchContact"},
        monochrome = true,
        strict = true,
        plugin = {"pretty",
                "html:target/cucumber-reports/cucumber-pretty",
                "json:target/cucumber-reports/CucumberTestReport.json",
                "rerun:target/cucumber-reports/rerun.txt"}
        )

public class TestRunner {
    private TestNGCucumberRunner testNGCucumberRunner;

    @Parameters({"browserName","mainURL"})
    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("chrome") String browserName,
                      @Optional("https://app.cloudfinder.com") String mainURL) throws Exception {
        testNGCucumberRunner = new TestNGCucumberRunner(this.getClass());
        Params.MAIN_URL = mainURL;
        Params.BROWSER = browserName;
    }

    @Test(groups = "cucumber scenarios", description = "Runs Cucumber Scenarios", dataProvider = "scenarios")
    public void scenario(PickleEventWrapper pickleEvent, CucumberFeatureWrapper cucumberFeature) throws Throwable {
        testNGCucumberRunner.runScenario(pickleEvent.getPickleEvent());
    }

    /**
     * @return returns two dimensional array of {@link CucumberFeatureWrapper}
     *         objects.
     */
    @DataProvider
    public Object[][] scenarios() {
        return testNGCucumberRunner.provideScenarios();
    }

   @AfterClass(alwaysRun = true)
   public void tearDown(){
        testNGCucumberRunner.finish();
   }

}
