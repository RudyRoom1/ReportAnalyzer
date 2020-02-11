package pages;

import Entities.Test;
import enums.TypeOfTestStatus;
import lombok.Getter;
import mapProcessing.MapProcessing;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

@Getter
public class ReportPage extends AbstractPage {
    private final By testSummaryElement = By.xpath("//div[./h4]");
    private List<Test> allEntities;
    private static By rowField = By.xpath("//tbody/tr[@role='row']");
    private String totalPassRate;
    private String url;
    private String nameOfJob;
    private Map<String, Integer> testFailureReasonsWithCount = new TreeMap<String, Integer>();
    private Map<String, String> testResultSummary = new TreeMap<>();
    private String reportDate;
    private List<Test> failedTests;
    private File pieChartImage;
    private File testSummaryTablePic;


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
        driver.get(url);
        driver.switchTo().frame(iFrameElement);


        System.out.println(driver.getWindowHandles());

        new WebDriverWait(driver, 10).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));

        clickAllCountField();
        fillReportDate();
        fillTotalPassRate();
        fillTestResultSummary();
        fillPieChartImage();
        allEntities = getAllRows();
        fillFailedTests();
        nameOfJob = MapProcessing.getSubstring("UAT15_[A-Z_]*", url);

        fillUniqueTestFailedReasonsWithCount();
        return this;
    }

    private void fillPieChartImage() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", pieChartElement);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        pieChartImage = pieChartElement.getScreenshotAs(OutputType.FILE);
    }

    private void fillFailedTests() {
        failedTests = allEntities.stream().filter(el -> el.getSuccessFailType() != TypeOfTestStatus.SUCCESS).collect(Collectors.toList());
    }

    private void fillTestResultSummary() {
        testSummaryTablePic = driver.findElement(testSummaryElement).getScreenshotAs(OutputType.FILE);
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
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//wait.until(ExpectedConditions.presenceOfNestedElementLocatedBy(rowsQuantityFieldElement,By.xpath("")));
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
