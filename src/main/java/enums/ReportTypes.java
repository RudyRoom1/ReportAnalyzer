package enums;

import lombok.Getter;
import workWithFile.FileWork;

@Getter
public enum ReportTypes {

    SMOKE_REPORT_PAGE(FileWork.readProperty("smoke.report.page","config.properties")),
    MOBC_REPORT_PAGE(FileWork.readProperty("mobc.report.page","config.properties")),
    DTH_REPORT_PAGE(FileWork.readProperty("dth.report.page","config.properties")),
    DTH_SINGLE_RUN_REPORT_PAGE(FileWork.readProperty("dth.single.run.report.page","config.properties")),
    DTH_VPOS_OFF_REPORT_PAGE(FileWork.readProperty("dth.vpos.off.report.page","config.properties")),
    CNC_REPORT_PAGE(FileWork.readProperty("cnc.report.page","config.properties")),
    ISOO_REPORT_PAGE(FileWork.readProperty("isso.report.page","config.properties")),
    CIAM_REPORT_PAGE(FileWork.readProperty("ciam.report.page","config.properties"));

    private String link;

    ReportTypes(String reportPage) {
        this.link = reportPage;
    }
}
