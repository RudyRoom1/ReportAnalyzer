package workWithBrowser;

import drivers.Driver;
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
    private static WebDriver driver = Driver.getDriver();

    public ReportPageAnalyser(String url) {
        driver.manage().timeouts().implicitlyWait(5, SECONDS);
        driver.get(url);
        driver.manage().window().maximize();
    }

//
//    @SafeVarargs
//    public final List<ReportPage> listOfReportPages(Enum<TypeOfTestStatus>... enums) {
//        List<ReportPage> allScenariosOnPage = allEntities;
//        List<ReportPage> allFailedScenarios = new ArrayList<>();
//
//        for (Enum<TypeOfTestStatus> entity : enums) {
//            List<ReportPage> allNotSuccessScenarios = allScenariosOnPage;
//            allNotSuccessScenarios = allNotSuccessScenarios.stream().filter(el -> el.getSuccessFailType() == entity).collect(Collectors.toList());
//            allFailedScenarios.addAll(allNotSuccessScenarios);
//        }
//        return allFailedScenarios;
//    }


}
