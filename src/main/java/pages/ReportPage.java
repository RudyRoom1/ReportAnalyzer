package pages;

import Entities.Test;
import drivers.Driver;
import enums.TypeOfTestStatus;
import lombok.Getter;
import mapProcessing.MapProcessing;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import workWithFile.FileWork;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
public class ReportPage extends AbstractPage {

    private List<Test> allEntities;
    private static By rowField = By.xpath("//tbody/tr[@role='row']");
    private String totalPassRate;
    private String url;
    private String nameOfJob;
    private HashMap<String, Integer> testFailureReasonsWithCount = new HashMap<String, Integer>();
    private HashMap<String, String> testResultSummary = new HashMap<String, String>();
    private String reportDate;
    private List<Test> failedTests;

    @FindBy(xpath = "//*[@class='jqplot-pie-series jqplot-data-label'][1]")
    private WebElement totalPassRateElement;

    @FindBy(id = "myframe")
    private WebElement iFrame;

    @FindBy(xpath = "//select[@name=\"test-results-table_length\"]")
    private WebElement rowsQuantityField;

    @FindBy(xpath = "//option[@value=\"-1\"]")
    private WebElement rowsQuantitySelectAll;

    @FindBy(xpath = "//span[@class='date-and-time']")
    private WebElement reportDateElement;

    private By testResultSummaryTable = By.xpath("//div[@id=\"test-results-tabs-1\"]//tr[./td[text()='Automated']]/td");

    @FindBy(id = "test_results_pie_chart")
    private WebElement pieChart;




    public ReportPage(String url) {
        this.url = url;
        gatherData();
    }

    public ReportPage gatherData() {
        driver.navigate().to(url);
        driver.switchTo().frame(iFrame);


        clickAllCountField();
        fillReportDate();
        fillTotalPassRate();
        fillTestResultSummary();

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", pieChart);

        File pieChartImage = pieChart.getScreenshotAs(OutputType.FILE);

        allEntities = getAllRows();
        fillFailedTests();
        //TODO VEEERY SLOW method
        nameOfJob = MapProcessing.getSubstring("UAT15_[A-Z_]*", url);

        fillUniqueTestFailedReasonsWithCount();
        return this;
    }

    public void fillFailedTests() {
        failedTests = getAllEntities().stream().filter(el -> el.getSuccessFailType() != TypeOfTestStatus.SUCCESS).collect(Collectors.toList());
    }

    public void fillTestResultSummary() {
        List<String> res = driver.findElements(testResultSummaryTable).stream().map(el -> el.getText()).collect(Collectors.toList());
        testResultSummary.put("Total", res.get(1));
        testResultSummary.put("Pass ", res.get(2));
        testResultSummary.put("Fail ", res.get(3));
        testResultSummary.put("Pending ", res.get(4));
        testResultSummary.put("Ignored ", res.get(5));
    }

    private void fillUniqueTestFailedReasonsWithCount() {
        allEntities.stream().filter(el -> !Objects.isNull(el.getFailureReason())).forEach(el -> {
            String reason = el.getFailureReason();

            if (Objects.isNull(testFailureReasonsWithCount.get(reason))) {
                testFailureReasonsWithCount.put(reason, 1);
            } else {
                testFailureReasonsWithCount.replace(reason, testFailureReasonsWithCount.get(reason + 1));
            }

        });
    }

    private void fillTotalPassRate() {
        this.totalPassRate = totalPassRateElement.getText();
    }

    private void fillReportDate() {
        this.reportDate = reportDateElement.getText();
    }

    public void clickAllCountField() {
        rowsQuantityField.click();
        rowsQuantitySelectAll.click();
    }

    private static List<Test> getAllRows() {
        List<WebElement> allRows = driver.findElements(rowField);
        return allRows.stream().map(el -> new Test(el)).collect(Collectors.toList());
    }

    public String listOfAllScenarios() {
        List<WebElement> allRows = driver.findElements(rowField);
        List<Test> allEntries;

        allEntries = allRows.stream().map(el -> new Test(el)).collect(Collectors.toList());
        List<String> result = allEntries.stream().map(Test::getTestID).collect(Collectors.toList());
        return String.join("\n", result);
    }


}
