package pages;

import enums.TypeOfTestStatus;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class ReportPage {
    private WebDriver driver;

    static List<ReportPage> allEntries;
    private static By rowField = By.xpath("//tbody/tr[@role='row']");
    private List<ReportPage> listOfReportPages;
    private Enum successFailType = TypeOfTestStatus.PENDING;
    private String testID;
    private String testName;
    private String totalPassRate;
    private String failureReason = "";
    private String skuTableName;
    private WebElement mainElement;
    private static String SUCCESS_FAIL_TYPE = ".//td/img";
    private static String TEST_NAME = ".//td/a[@class='ellipsis']";
    private static String TEST_FAILURE_STEP = "//tr[@class='test-ERROR'][1]";
    private static String TOTAL_PASS_RATE = "//*[@class='jqplot-pie-series jqplot-data-label'][1]";

    public String getTestID() {
        return testID;
    }

    public ReportPage() {
    }

    public ReportPage(WebElement element, WebDriver driver) {
        this.driver = driver;
        mainElement = element;
        fillAllFields();
    }

    private void fillAllFields() {
        fillSuccessFailType();
        fillTestID();
        fillTotalPassRate();
        if (successFailType != TypeOfTestStatus.SUCCESS) {
            if (!Objects.isNull(fillFailureReason()) && fillFailureReason().equals("SkuSelectorException")) {
                fillSkuTableName();
            }
        }
    }

    private void fillTotalPassRate() {
        this.totalPassRate = mainElement.findElement(By.xpath(TOTAL_PASS_RATE)).getText();
    }

    private void fillSkuTableName() {
        String result = "";
        String errorStepText;

        Actions actions = new Actions(driver);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", mainElement);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        actions.keyDown(Keys.CONTROL).click(mainElement.findElement(By.xpath(".//a"))).keyUp(Keys.CONTROL).build().perform();

        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));

        if (driver.findElements(By.xpath("//div[@class='story-title']")).size() == 2) {
            errorStepText = driver.findElement(By.xpath(TEST_FAILURE_STEP)).getText();
        }

        driver.close();
        driver.switchTo().window(tabs.get(0)).switchTo().frame(0);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        skuTableName = result;
    }

    private void fillTestID() {
        String text = mainElement.findElement(By.xpath(TEST_NAME)).getText();

        Pattern pattern = Pattern.compile("([A-Z]*-[0-9-]{1,4}|PRECONDITIONS)(.*)");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            testID = matcher.group(1);
            testName = matcher.group(2);
        } else {
            System.out.println(String.format("CAN'T FIND TEST ID OR TEST NAME in %s", text));
        }
    }

    private void fillSuccessFailType() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String type = mainElement.findElement(By.xpath(SUCCESS_FAIL_TYPE)).getAttribute("title");

        switch (type) {
            case "SUCCESS":
                successFailType = TypeOfTestStatus.SUCCESS;
                break;
            case "PENDING":
                successFailType = TypeOfTestStatus.PENDING;
                break;
            case "ERROR":
                successFailType = TypeOfTestStatus.ERROR;
                break;
            case "FAIL":
                successFailType = TypeOfTestStatus.FAIL;
                break;
            case "FAILURE":
                successFailType = TypeOfTestStatus.FAILURE;
                break;
            case "IGNORED":
                successFailType = TypeOfTestStatus.IGNORED;
                break;
            default:
                try {
                    throw new Exception(String.format("NO SUCH TYPE: %s", type));
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private String fillFailureReason() {
        String failureReasonBig = mainElement.findElement(By.xpath(TEST_NAME)).getAttribute("title");
        String result = null;
        if (successFailType == TypeOfTestStatus.ERROR || successFailType == TypeOfTestStatus.FAILURE) {
            Pattern pattern = Pattern.compile("(\\b\\w+Exception\\b|\\b\\w+Failure\\b|\\b\\w+Error\\b)");
            Matcher matcher = pattern.matcher(failureReasonBig);
            if (matcher.find()) {
                result = matcher.group(1);
            } else {
                System.out.println(String.format("%s: failure reason CAN'T BE FOUND!!!! ", testID));
            }
        } else if (successFailType == TypeOfTestStatus.FAIL) {
            result = failureReasonBig;
        }
        failureReason = result;
        return result;
    }


}
