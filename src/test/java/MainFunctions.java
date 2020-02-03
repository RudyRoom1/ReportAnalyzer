import Entities.Test;
import drivers.Driver;
import enums.ReportTypes;
import enums.TypeOfTestStatus;
import excelReportTable.ExcelReportTable;
import excelReportTable.WordReportTable;
import lombok.SneakyThrows;
import pages.JenkinsPage;
import pages.ReportPage;
import workWithFile.FileWork;

import java.util.*;
import java.util.stream.Collectors;

class MainFunctions {
    private static boolean isAuthorise = false;

    private static String pathToFailedSkus = FileWork.readProperty("failed.skus", "config.properties");
    private static String pathToExcelTable = FileWork.readProperty("excel.file", "config.properties");

    @SneakyThrows
    public static void createReportForBiWeekly(ReportTypes... reportLinks) {
        if (reportLinks.length == 0) {
            reportLinks = ReportTypes.values();
        }
        makeAuthorization();
        WordReportTable table = new WordReportTable();
//        ExcelReportTable table = new ExcelReportTable("Regression");
        for (ReportTypes link : reportLinks) {
            ReportPage page = new ReportPage(link.getLink());
            WordReportTable.generate(page);
            WordReportTable.write();
//            table.createHeader(page);
        }
        Driver.exitDriver();
        FileWork.makeDir(".\\Results\\AAAAAAAAAAA");
        FileWork.makeDir(".\\Results\\FailedSkuFiles");
//        table.writeToXLSXFile("Regression", pathToExcelTable);

    }

    static void createExcelTableWithFailedSku(ReportTypes... reportLink) {
        String failedSkus = "";

        ExcelReportTable table = new ExcelReportTable("Regression");

        if (reportLink.length != 0) {
            makeAuthorization();

            for (ReportTypes entity : reportLink) {
                String link = entity.getLink();
                ReportPage page = new ReportPage(link);
                table.createHeader(page);
                table.setDataToTable(page.getFailedTests());
            }
        } else {
            System.out.println("Enter report link");
        }
        Driver.exitDriver();
        FileWork.makeDir(".\\Results\\FailedSkuExcelTable");
        FileWork.makeDir(".\\Results\\FailedSkuFiles");
        table.writeToXLSXFile("Regression", pathToExcelTable);
        FileWork.writeToFile(failedSkus, pathToFailedSkus);
    }

    private static void makeAuthorization() {
        JenkinsPage jenk = new JenkinsPage();
        jenk.makeAuthorisation(FileWork.readProperty("login", "credentials.properties"),
                FileWork.readProperty("password", "credentials.properties"));
    }

    static void createSuit(ReportTypes... reportLink) {
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