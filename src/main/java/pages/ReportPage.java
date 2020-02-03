package pages;

import Entities.Test;
import enums.TypeOfTestStatus;
import lombok.Getter;
import mapProcessing.MapProcessing;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private File pieChartImage;


    @FindBy(xpath = "//*[@class='jqplot-pie-series jqplot-data-label'][1]")
    private WebElement totalPassRateElement;

    @FindBy(id = "myframe")
    private WebElement iFrameElement;

    @FindBy(xpath = "//select[@name=\"test-results-table_length\"]")
    private WebElement rowsQuantityFieldElement;

    @FindBy(xpath = "//option[@value=\"-1\"]")
    private WebElement rowsQuantitySelectAllElement;

    @FindBy(xpath = "//span[@class='date-and-time']")
    private WebElement reportDateElement;

    private By testResultSummaryTableElement = By.xpath("//div[@id=\"test-results-tabs-1\"]//tr[./td[text()='Automated']]/td");

    @FindBy(id = "test_results_pie_chart")
    private WebElement pieChartElement;


    public ReportPage(String url) {
        this.url = url;
        gatherData();
    }

    private ReportPage gatherData() {
        driver.navigate().to(url);
        driver.switchTo().frame(iFrameElement);

        clickAllCountField();
        fillReportDate();
        fillTotalPassRate();
        fillTestResultSummary();
        fillPieChartImage();
        allEntities = getAllRows();
        fillFailedTests();
        //TODO VEEERY SLOW method
        nameOfJob = MapProcessing.getSubstring("UAT15_[A-Z_]*", url);

        fillUniqueTestFailedReasonsWithCount();
        return this;
    }

    private void fillPieChartImage() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", pieChartElement);
        pieChartImage = pieChartElement.getScreenshotAs(OutputType.FILE);
    }

    private void fillFailedTests() {
        failedTests = allEntities.stream().filter(el -> el.getSuccessFailType() != TypeOfTestStatus.SUCCESS).collect(Collectors.toList());
    }

    private void fillTestResultSummary() {
        List<String> res = driver.findElements(testResultSummaryTableElement).stream().map(el -> el.getText()).collect(Collectors.toList());
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
                testFailureReasonsWithCount.put(reason, testFailureReasonsWithCount.get(reason) +1);
            }

        });
    }

    private void fillTotalPassRate() {
        this.totalPassRate = totalPassRateElement.getText();
    }

    private void fillReportDate() {
        this.reportDate = reportDateElement.getText();
    }

    private void clickAllCountField() {
        rowsQuantityFieldElement.click();
        rowsQuantitySelectAllElement.click();
    }

    private static List<Test> getAllRows() {
        List<WebElement> allRows = driver.findElements(rowField);
        return allRows.stream().map(el -> new Test(el)).collect(Collectors.toList());
    }

    private String listOfAllScenarios() {
        List<WebElement> allRows = driver.findElements(rowField);
        List<Test> allEntries;

        allEntries = allRows.stream().map(el -> new Test(el)).collect(Collectors.toList());
        List<String> result = allEntries.stream().map(Test::getTestID).collect(Collectors.toList());
        return String.join("\n", result);
    }


}
