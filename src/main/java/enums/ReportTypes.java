package enums;

import lombok.Getter;
import workWithFile.FileWork;

@Getter
public enum ReportTypes {

    SMOKE(FileWork.readProperty("smoke.report.page","config.properties")),
    MOBC(FileWork.readProperty("mobc.report.page","config.properties")),
    DTH(FileWork.readProperty("dth.report.page","config.properties")),
    DTH_SINGLE_RUN(FileWork.readProperty("dth.single.run.report.page","config.properties")),
    DTH_VPOS_OFF(FileWork.readProperty("dth.vpos.off.report.page","config.properties")),
    CNC(FileWork.readProperty("cnc.report.page","config.properties")),
    ISOO(FileWork.readProperty("isso.report.page","config.properties")),
    CIAM(FileWork.readProperty("ciam.report.page","config.properties"));

    private String link;

    ReportTypes(String reportPage) {
        this.link = reportPage;
    }
}
