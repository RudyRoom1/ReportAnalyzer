import enums.ReportPages;

public class CreateExcelTabl {

    public static void main(String[] args) {
//        MainFunctions.createExcelTableWithFailedSku(ReportPages.MOBC_REPORT_PAGE, ReportPages.DTH_REPORT_PAGE, ReportPages.DTH_SINGLE_RUN_REPORT_PAGE,
//                ReportPages.DTH_VPOS_OFF_REPORT_PAGE, ReportPages.CNC_REPORT_PAGE, ReportPages.DTH_SINGLE_RUN_REPORT_PAGE, ReportPages.ISSO_REPORT_PAGE);
        MainFunctions.createExcelTableWithFailedSku(ReportPages.CIAM_REPORT_PAGE,ReportPages.ISSO_REPORT_PAGE);
    }
}
