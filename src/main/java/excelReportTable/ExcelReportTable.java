package excelReportTable;

import lombok.Data;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pages.ReportPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Data
public class ExcelReportTable {

    private XSSFWorkbook workbook = new XSSFWorkbook();
    private Sheet sheet;
    CellStyle style = workbook.createCellStyle();
    CellStyle headerStyle;
    XSSFFont font;


    static int rowNum = 0;
    Row row;
    Cell cell;

    public ExcelReportTable(String nameOfSheet) {
        sheet = workbook.createSheet(nameOfSheet);
    }

    public void createHeader(String nameOfJob, ReportPage reportPage) {

        headerStyle = workbook.createCellStyle();
        setCellStyle(headerStyle);
        sheet.setColumnWidth(0, 12000);
        sheet.setColumnWidth(1, 12000);
        sheet.setColumnWidth(2, 12000);
        sheet.setColumnWidth(3, 12000);
        Row headerFirstLine = sheet.createRow(rowNum);
        rowNum++;
        Row headerSecondLine = sheet.createRow(rowNum);
        Cell headerCell = headerFirstLine.createCell(0);
        headerCell.setCellValue(String.format("%s - %s", nameOfJob, reportPage.getTotalPassRate()));
        headerCell.setCellStyle(headerStyle);
        headerCell = headerFirstLine.createCell(1);
        headerCell.setCellStyle(headerStyle);
        headerCell = headerFirstLine.createCell(2);
        headerCell.setCellStyle(headerStyle);
        headerCell = headerFirstLine.createCell(3);
        headerCell.setCellStyle(headerStyle);

        //        Second row of header
        headerCell = headerSecondLine.createCell(0);
        headerCell.setCellStyle(headerStyle);

        headerCell = headerSecondLine.createCell(1);
        headerCell.setCellValue("Scenarios");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerSecondLine.createCell(2);
        headerCell.setCellValue("Error");
        headerCell.setCellStyle(headerStyle);

        headerCell = headerSecondLine.createCell(3);
        headerCell.setCellValue("Status");
        headerCell.setCellStyle(headerStyle);

        System.out.println("Header was created");
    }

    public void setDataToTable(List<ReportPage> page) {

        style.setWrapText(true);
        for (ReportPage entity : page) {
            rowNum++;
            row = sheet.createRow(rowNum);
            cell = row.createCell(1);
            cell.setCellStyle(style);
            cell.setCellValue(entity.getTestID());
            cell = row.createCell(2);
            cell.setCellStyle(style);
            cell.setCellValue(entity.getFailureReason());
        }
        rowNum++;
        System.out.println("Data was set");
    }

    private void setCellStyle(CellStyle headersStyle) {

        headersStyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        headersStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headersStyle.setBorderBottom(BorderStyle.THIN);
        headersStyle.setBorderLeft(BorderStyle.THIN);
        headersStyle.setBorderRight(BorderStyle.THIN);
        headersStyle.setBorderTop(BorderStyle.THIN);
        headersStyle.setFont(font);
    }

    private XSSFFont setFont() {

        font = workbook.createFont();
        font.setFontName("Cambria");
        font.setFontHeightInPoints((short) 11);
        return font;
    }

    //        File create
    public void writeToXLSXFile(String nameOfFile, String pathToFoulder) {

        File currDir = new File(pathToFoulder);
        String path = currDir.getAbsolutePath();
        String fileLocation = String.format("%s\\%s_%s%s", path, nameOfFile, LocalDate.now().toString(), ".xlsx");
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(fileLocation);
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e) {
            System.out.println("Close opened table");
            e.printStackTrace();
        }
        System.out.println("Excel file was created");

    }
}
