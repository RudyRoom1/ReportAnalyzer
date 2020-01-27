import drivers.Driver;
import enums.Reports;
import enums.TypeOfTestStatus;
import excelReportTable.ExcelReportTable;
import mapProcessing.MapProcessing;
import pages.ReportPage;
import workWithBrowser.ReportPageAnalyser;
import workWithFile.FileWork;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

class MainFunctions {
    private static List<String> listOfSku;
    private static boolean isAuthorise = false;

    private static String pathToFailedSkus = FileWork.readProperty("failed.skus", "config.properties");
    private static String pathToExcelTable = FileWork.readProperty("excel.file", "config.properties");

    public static void createReportForBiWeekly(Reports... reportLinks) {
        if (reportLinks.length == 0) {
            reportLinks = Reports.values();
        }
//        List<ReportPage> listReportPages = new ArrayList<>();
        ExcelReportTable table = new ExcelReportTable("Regression");
        for (Reports link : reportLinks) {
            ReportPage page = new ReportPage(link.getLink());
            table.createHeader(page);
            table.setDataToTable(page);
        }
        Driver.exitDriver();
        FileWork.makeDir(".\\Results\\AAAAAAAAAAA");
        FileWork.makeDir(".\\Results\\FailedSkuFiles");
        table.writeToXLSXFile("Regression", pathToExcelTable);
//        FileWork.writeToFile(failedSkus, pathToFailedSkus);

    }
//
    static void createExcelTableWithFailedSku(Reports... reportLink) {
//        String failedSkus = "";
//
//        ExcelReportTable table = new ExcelReportTable("Regression");
//        if (reportLink.length != 0) {
//            for (Reports entity : reportLink) {
//                String nameOfMavenJob = "";
//                String entityValue = entity.getLink();
//                List<ReportPage> listReportPages = listFailedReportPages(entityValue);
//                nameOfMavenJob = MapProcessing.getSubstring("UAT15_[A-Z_]*", entityValue);
//                table.createHeader(listReportPages.get(0));
//                table.setDataToTable(listReportPages);
//            }
//        } else {
//            System.out.println("Enter report link");
//        }
//        Driver.exitDriver();
//        FileWork.makeDir(".\\Results\\FailedSkuExcelTable");
//        FileWork.makeDir(".\\Results\\FailedSkuFiles");
//        table.writeToXLSXFile("Regression", pathToExcelTable);
//        FileWork.writeToFile(failedSkus, pathToFailedSkus);
    }
//
//    private static List<ReportPage> listFailedReportPages(String reportLink) {
//
//        ReportPageAnalyser reportPage = new ReportPageAnalyser(reportLink);
//
//        if (!isAuthorise) {
//            reportPageAnalyser.makeAuthorisation(FileWork.readProperty("login", "credentials.properties"),
//                    FileWork.readProperty("password", "credentials.properties"));
//            isAuthorise = true;
//        }
//        reportPage.clickAllCountField();
//        return allNotSuccessReportPages();
//    }
//
//    public static List<ReportPage> allNotSuccessReportPages() {
//        List<ReportPage> allScenariosOnPage = allEntries;
//        allScenariosOnPage.removeIf(el -> el.getSuccessFailType() == TypeOfTestStatus.SUCCESS);
//        return allScenariosOnPage;
//    }
//
    static void createSuit(Reports... reportLink) {
//        Map<String, String> result = new HashMap<>();
//        Map<String, List<String>> pathScenarioMap;
//        List<String> listOfScenariosID = new ArrayList<>();
//
//        if (reportLink.length != 0) {
//            for (Reports entity : reportLink) {
//                String entityValue = entity.getLink();
//                List<ReportPage> listReportPages = listFailedReportPages(entityValue);
//                listOfScenariosID.addAll(listReportPages.stream().map(ReportPage::getTestID).collect(Collectors.toList()));
//            }
//        } else {
//            System.out.println("Enter report link");
//        }
//        FileWork.creatingFileMap(new File(FileWork.readProperty("path.folder", "config.properties").trim()), result);
//        pathScenarioMap = MapProcessing.pathKeys("stories.*", "Scenario: *[A-Z\\d-]*", result);
//        Map<String, List<String>> filteringMapOfList = MapProcessing.filteringMapOfList(pathScenarioMap, listOfScenariosID);
//
////        Separator: <#> - for local run; <,> - for Jenkins run
//        String suiteData = MapProcessing.concatKeyAndValueWithSeparate(filteringMapOfList, ",");
//        FileWork.makeDir(".\\Results\\FailedSkuSuite");
//        FileWork.writeToFile(suiteData, FileWork.readProperty("suite.file", "config.properties"));
    }
}