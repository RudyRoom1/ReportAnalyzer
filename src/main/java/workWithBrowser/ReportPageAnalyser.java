package workWithBrowser;

import drivers.ChromeWebDrivers;
import enums.TypeOfTestStatus;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import pages.ReportPage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.concurrent.TimeUnit.SECONDS;

@Getter
public class ReportPageAnalyser {

    static List<ReportPage> allEntries;

    private static WebDriver driver = ChromeWebDrivers.getDriver();

    @FindBy(xpath = "//input[@name='j_username']")
    private WebElement loginField;

    @FindBy(xpath = "//input[@name='j_password']")
    private WebElement passwordField;

    @FindBy(id = "yui-gen1-button")
    private WebElement loginButton;

    @FindBy(id = "myframe")
    private WebElement iFrame;

    @FindBy(xpath = "//select[@name=\"test-results-table_length\"]")
    private WebElement rowsQuntityField;

    @FindBy(xpath = "//option[@value=\"-1\"]")
    private WebElement rowsQuantitySelectAll;

    private static By rowField = By.xpath("//tbody/tr[@role='row']");

    public ReportPageAnalyser() {
    }

    public ReportPageAnalyser(String url) {
        driver.manage().timeouts().implicitlyWait(5, SECONDS);
        driver.get(url);
        driver.manage().window().maximize();
        PageFactory.initElements(driver, this);
    }

    public void makeAuthorisation(String login, String password) {
        getLoginField().sendKeys(login);
        getPasswordField().sendKeys(password);
        getLoginButton().click();
    }

    public void clickAllCountField() {
        driver.switchTo().frame(getIFrame());
        getRowsQuntityField().click();
        getRowsQuantitySelectAll().click();
        allEntries = getAllRows();
    }

    private static List<ReportPage> getAllRows() {
        List<WebElement> allRows = driver.findElements(rowField);
        return allRows.stream().map(el -> new ReportPage(el, driver)).collect(Collectors.toList());
    }

    public final List<ReportPage> allNotSuccessReportPages() {
        List<ReportPage> allScenariosOnPage = allEntries;
        allScenariosOnPage.removeIf(el -> el.getSuccessFailType() == TypeOfTestStatus.SUCCESS);
        return allScenariosOnPage;
    }

    @SafeVarargs
    public final List<ReportPage> listOfReportPages(Enum<TypeOfTestStatus>... enums) {
        List<ReportPage> allScenariosOnPage = allEntries;
        List<ReportPage> allFailedScenarios = new ArrayList<>();

        for (Enum<TypeOfTestStatus> entity : enums) {
            List<ReportPage> allNotSuccessScenarios = allScenariosOnPage;
            allNotSuccessScenarios = allNotSuccessScenarios.stream().filter(el -> el.getSuccessFailType() == entity).collect(Collectors.toList());
            allFailedScenarios.addAll(allNotSuccessScenarios);
        }
        return allFailedScenarios;
    }

    public String listOfAllScenarios() {
        List<WebElement> allRows = driver.findElements(rowField);
        List<ReportPage> allEntries;

        allEntries = allRows.stream().map(el -> new ReportPage(el, driver)).collect(Collectors.toList());
        ChromeWebDrivers.exitDriver();
        List<String> result = allEntries.stream().map(ReportPage::getTestID).collect(Collectors.toList());
        return String.join("\n", result);
    }
}
