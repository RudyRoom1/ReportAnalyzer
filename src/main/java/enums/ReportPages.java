package enums;

import lombok.Getter;
import workWithFile.FileWork;

@Getter
public enum ReportPages {

    SMOKE_REPORT_PAGE(FileWork.readProperty("smoke.report.page")),
    MOBC_REPORT_PAGE(FileWork.readProperty("mobc.report.page")),
    DTH_REPORT_PAGE(FileWork.readProperty("dth.report.page")),
    DTH_SINGLE_RUN_REPORT_PAGE(FileWork.readProperty("dth.single.run.report.page")),
    DTH_VPOS_OFF_REPORT_PAGE(FileWork.readProperty("dth.vpos.off.report.page")),
    CNC_REPORT_PAGE(FileWork.readProperty("cnc.report.page")),
    ISSO_REPORT_PAGE(FileWork.readProperty("isso.report.page")),
    CIAM_REPORT_PAGE(FileWork.readProperty("ciam.report.page"));

    private String reportPage;

    ReportPages(String reportPage) {
        this.reportPage = reportPage;
    }
}
