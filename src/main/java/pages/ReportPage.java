package pages;

import Entities.Test;
import drivers.Driver;
import lombok.Getter;
import mapProcessing.MapProcessing;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.PageFactory;
import workWithFile.FileWork;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
public class ReportPage {

    private static WebDriver driver;

    private List<Test> allEntities;
    private static By rowField = By.xpath("//tbody/tr[@role='row']");
    private String totalPassRate;
    private String url;
    private String nameOfJob;
    private HashMap<String, Integer> testFailureReason = new HashMap<String, Integer>();
    private HashMap<String, String> testResultSummary = new HashMap<String, String>();

    private static String TOTAL_PASS_RATE = "//*[@class='jqplot-pie-series jqplot-data-label'][1]";

    @FindBy(xpath = "//input[@name='j_username']")
    private WebElement loginField;

    @FindBy(xpath = "//input[@name='j_password']")
    private WebElement passwordField;

    @FindBy(id = "yui-gen1-button")
    private WebElement loginButton;

    @FindBy(id = "myframe")
    private WebElement iFrame;

    @FindBy(xpath = "//select[@name=\"test-results-table_length\"]")
    private WebElement rowsQuantityField;

    @FindBy(xpath = "//option[@value=\"-1\"]")
    private WebElement rowsQuantitySelectAll;

    @FindBy(xpath = "//div[@id=\"test-results-tabs-1\"]//tr[./td[text()='Automated']]/td")
    private List<WebElement> testResultSummaryTable;

    @FindBy(id = "test_results_pie_chart")
    private WebElement pieChart;


    public ReportPage(String url) {
        this.url = url;
        this.driver = Driver.getDriver();
        driver.manage().timeouts().implicitlyWait(5, SECONDS);
        driver.manage().window().maximize();
        PageFactory.initElements(driver, this);
        gatherData();
    }

    public ReportPage gatherData() {
        driver.navigate().to(url);
        makeAuthorisation(FileWork.readProperty("login", "credentials.properties"),
                FileWork.readProperty("password", "credentials.properties"));
        clickAllCountField();

        fillTotalPassRate();
        fillTestResultSummary();
        File pieChartImage = pieChart.getScreenshotAs(OutputType.FILE);

        allEntities = getAllRows();
        //VEEERY SLOW method
        nameOfJob = MapProcessing.getSubstring("UAT15_[A-Z_]*", url);
        fillOriginalTestFailedReasons();
        return this;
    }

    public void fillTestResultSummary() {
        List<String> res = testResultSummaryTable.stream().map(el -> el.getText()).collect(Collectors.toList());
        testResultSummary.put("Total", res.get(1));
        testResultSummary.put("Pass ", res.get(2));
        testResultSummary.put("Fail ", res.get(3));
        testResultSummary.put("Pending ", res.get(4));
        testResultSummary.put("Ignored ", res.get(5));
    }

    private void fillOriginalTestFailedReasons() {
        allEntities.forEach(el -> {
            if (Objects.isNull(testFailureReason.get(el.getFailureReason()))) {
                testFailureReason.put(el.getFailureReason(), 1);
            } else {
                testFailureReason.replace(el.getFailureReason(), testFailureReason.get(el.getFailureReason() + 1));
            }

        });
    }

    private void fillTotalPassRate() {
        this.totalPassRate = driver.findElement(By.xpath(TOTAL_PASS_RATE)).getText();
    }

    public void makeAuthorisation(String login, String password) {
        loginField.sendKeys(login);
        passwordField.sendKeys(password);
        loginButton.click();
    }

    public void clickAllCountField() {
        driver.switchTo().frame(iFrame);
        rowsQuantityField.click();
        rowsQuantitySelectAll.click();
//        allEntities = getAllRows();
    }

    private static List<Test> getAllRows() {
        List<WebElement> allRows = driver.findElements(rowField);
        return allRows.stream().map(el -> new Test(el)).collect(Collectors.toList());
    }

    public String listOfAllScenarios() {
        List<WebElement> allRows = driver.findElements(rowField);
        List<Test> allEntries;

        allEntries = allRows.stream().map(el -> new Test(el)).collect(Collectors.toList());
//        Driver.exitDriver();
        List<String> result = allEntries.stream().map(Test::getTestID).collect(Collectors.toList());
        return String.join("\n", result);
    }


}
