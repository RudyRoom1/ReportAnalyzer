package Entities;

import drivers.Driver;
import enums.TypeOfTestStatus;
import lombok.Getter;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import pages.AbstractPage;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
public class Test extends AbstractPage {
    private final WebDriver driver;

    private Enum successFailType = TypeOfTestStatus.PENDING;
    private String testID;
    private String testName;
    private String failureReason;
    private WebElement mainElement;
    private static String TEST_NAME = ".//td/a[@class='ellipsis']";
    private static String TEST_FAILURE_STEP = "//tr[@class='test-ERROR'][1]";
    private static String SUCCESS_FAIL_TYPE = ".//td/img";

    @FindBy(xpath = "//tr[@class=\"test-ERROR\"]")
    private WebElement failedTestExampleElement;

    @FindBy(xpath = "//tr[@class=\"test-ERROR\"]//span[@class=\"nested-group-step\"]")
    private WebElement failedTestExampleStepElement;

    public Test(WebElement element) {
        this.driver = Driver.getDriver();
        mainElement = element;
        fillAllFields();
    }

    private void fillAllFields() {
        fillSuccessFailType();
        fillTestID();
        if (successFailType != TypeOfTestStatus.SUCCESS) {
            fillFailureReason();
        }
    }

    private String getSkuTableName() {
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
        String bigString = "";
        if (driver.findElements(By.xpath("//div[@class='story-title']")).size() == 2) {
            bigString = driver.findElement(By.xpath(TEST_FAILURE_STEP)).getText();
        } else {
            failedTestExampleElement.click();
            bigString = failedTestExampleStepElement.getText();
        }
        System.out.println("scenario step with sku table failed: " + bigString);
        Pattern pattern = Pattern.compile("examples.*.table");
        Matcher matcher = pattern.matcher(bigString);
        matcher.find();
        String result = matcher.group(0);

        driver.close();
        driver.switchTo().window(tabs.get(0)).switchTo().frame(0);
        return result;
    }

    private String fillFailureReason() {
        String failureReasonBig = mainElement.findElement(By.xpath(TEST_NAME)).getAttribute("title");
        String result = null;
        if (successFailType == TypeOfTestStatus.ERROR || successFailType == TypeOfTestStatus.FAILURE) {
            Pattern pattern = Pattern.compile("(\\b\\w+Exception\\b|\\b\\w+Failure\\b|\\b\\w+Error\\b)");
            Matcher matcher = pattern.matcher(failureReasonBig);
            if (matcher.find()) {
                result = matcher.group(1);

                switch (result) {
                    case "HttpRequestException":
                        result += "\n" + failureReasonBig.split("REQUEST ===(.*?) HTTP")[0];
                        break;
                    case "SkuSelectorException":
                        result += "\n" + getSkuTableName();
                        break;
                    case "NoSuchElementException":
//                        result += "\n" + failureReasonBig.split("Exception: (?!Timed).*")[0];
                        break;
                    case "TimeoutException":
                    default:
                        result += "\n" + failureReasonBig.split("Exception: (.*)")[0];
                        break;
                }

            } else {
                System.out.println(String.format("%s: failure reason CAN'T BE FOUND!!!! ", testID));
            }
        } else {
            result = failureReasonBig;
        }
        failureReason = !result.isEmpty() ? result : String.format("%s: failure reason CAN'T BE FOUND!!!! ", testID);
        return result;
    }

    private void fillTestID() {
        String text = mainElement.findElement(By.xpath(TEST_NAME)).getText();

        Pattern pattern = Pattern.compile("([a-z,A-Z]*-[0-9-]{1,4}|PRECONDITIONS)(.*)");
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


}
