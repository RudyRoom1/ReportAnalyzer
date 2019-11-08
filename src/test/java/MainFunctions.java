import enums.ReportPages;
import excelReportTable.ExcelReportTable;
import mapProcessing.MapProcessing;
import pages.ReportPage;
import workWithBrowser.ReportPageAnalyser;
import workWithFile.FileWork;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class MainFunctions {
    private static List<String> listOfSku;
    private static boolean isAuthorise = false;

    private static String pathToFailedSkus = FileWork.readProperty("failed.skus");
    private static String pathToExcelTable = FileWork.readProperty("excel.file");

    private static List<ReportPage> listFailedReportPages(String reportLink) {
        ReportPageAnalyser reportPage = new ReportPageAnalyser(reportLink);

        if (!isAuthorise) {
            reportPage.makeAuthorisation(FileWork.readProperty("login"),
                    FileWork.readProperty("password"));
            isAuthorise = true;
        }
        reportPage.clickAllCountField();
        return reportPage.allNotSuccessReportPages();
    }

    static void createExcelTableWithFailedSku(ReportPages... reportLink) {
        String failedSkus = "";

        ExcelReportTable table = new ExcelReportTable("Regression");
        if (reportLink.length != 0) {
            for (ReportPages entity : reportLink) {
                String nameOfMavenJob = "";
                String entityValue = entity.getReportPage();
                List<ReportPage> listReportPages = listFailedReportPages(entityValue);
                nameOfMavenJob = MapProcessing.getSubstring("UAT15_[A-Z_]*", entityValue);
                table.createHeader(nameOfMavenJob, listReportPages.get(0));
                table.setDataToTable(listReportPages);
            }
        } else {
            System.out.println("Enter report link");
        }
        FileWork.makeDir(".\\Results\\FailedSkuExcelTable");
        FileWork.makeDir(".\\Results\\FailedSkuFiles");
        table.writeToXLSXFile("Regression", pathToExcelTable);
        FileWork.writeToFile(failedSkus, pathToFailedSkus);
    }


    static void createSuit(ReportPages... reportLink) {
        Map<String, String> result = new HashMap<>();
        Map<String, List<String>> pathScenarioMap;
        List<String> listOfScenariosID = new ArrayList<>();

        if (reportLink.length != 0) {
            for (ReportPages entity : reportLink) {
                String entityValue = entity.getReportPage();
                List<ReportPage> listReportPages = listFailedReportPages(entityValue);
                listOfScenariosID.addAll(listReportPages.stream().map(ReportPage::getTestID).collect(Collectors.toList()));
            }
        } else {
            System.out.println("Enter report link");
        }
        FileWork.creatingFileMap(new File(FileWork.readProperty("path.folder").trim()), result);
        pathScenarioMap = MapProcessing.pathKeys("stories.*", "Scenario: *[A-Z\\d-]*", result);
        Map<String, List<String>> filteringMapOfList = MapProcessing.filteringMapOfList(pathScenarioMap, listOfScenariosID);

//        Separator: <#> - for local run; <,> - for Jenkins run
        String suiteData = MapProcessing.concatKeyAndValueWithSeparate(filteringMapOfList, ",");
        FileWork.makeDir(".\\Results\\FailedSkuSuite");
        FileWork.writeToFile(suiteData, FileWork.readProperty("suite.file"));
    }
}