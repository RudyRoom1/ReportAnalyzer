import enums.ReportTypes;

public class CreateExcelTabl {

    public static void main(String[] args) {
        MainFunctions.createExcelTableWithFailedSku(
//                ReportTypes.MOBC_REPORT_PAGE
//                ReportTypes.DTH_REPORT_PAGE,
//                ReportTypes.DTH_SINGLE_RUN_REPORT_PAGE,
                ReportTypes.DTH_VPOS_OFF
//                ReportTypes.CNC_REPORT_PAGE,
//                ReportTypes.CIAM_REPORT_PAGE,
//                ReportTypes.ISOO_REPORT_PAGE,
//                ReportTypes.SMOKE_REPORT_PAGE
        );
    }
}
