package excelReportTable;

import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.impl.CTPImpl;
import pages.ReportPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WordReportTable {
//    public static void main(String[] args) throws Exception {
//        generate();
//    }

    private static XWPFDocument document = new XWPFDocument();

    public static void generate(ReportPage page) throws Exception {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();

        File image1 = page.getPieChartImage();
        String imgFile1 = image1.getName();
        BufferedImage bimg1 = ImageIO.read(image1);

        int width1 = bimg1.getWidth()/2;
        int height1 = bimg1.getHeight()/2;


        run.addPicture(new FileInputStream(image1), XWPFDocument.PICTURE_TYPE_JPEG, "1.jpg", Units.toEMU(width1), Units.toEMU(height1));
        //create table
        XWPFTable table = document.createTable();
//        XWPFTableRow row1 = table.createRow();
//        row1.createCell().setParagraph(paragraph);


        //create first row
        XWPFTableRow tableRowOne = table.getRow(0);
        tableRowOne.getCell(0).setParagraph(paragraph);

        String tableTitles = page.getTestResultSummary().keySet().stream().collect(Collectors.joining("\t"));
        String tableValues = page.getTestResultSummary().values().stream().collect(Collectors.joining("\t"));
        String failureReasons = "";
        List<String> failuresList = new ArrayList<>();
        page.getTestFailureReasonsWithCount().keySet().stream(key -> {

            String value = page.getTestFailureReasonsWithCount().get(key);
            failureReasons=failureReasons+String.format("%s - %s\n",value,key);

        });

        String pageDataToReport = String.format("%s\n" +
                        "Test Result Summary:\n" +
                        "%s\n" +
                        "%s\n" +
                        "Analyzis Of Failures:\n" +
                        "%s",
                page.getNameOfJob(),
                tableTitles,
                tableValues,
                failureReasons
                );


        tableRowOne.addNewTableCell().setText(pageDataToReport);




//        tableRowOne.addNewTableCell().setText("col three, row one");
//
//        //create second row
//        XWPFTableRow tableRowTwo = table.createRow();
//
//        tableRowTwo.getCell(0).setText("col one, row two");
//        tableRowTwo.getCell(1).setText("col two, row two");
//        tableRowTwo.getCell(2).setText("col three, row two");
//
//        XWPFTableRow tableRowThree = table.createRow();
//        tableRowThree.getCell(1).setText("col two, row three");
//        tableRowThree.getCell(2).setText("col three, row three");
//
//

    }

    public static void write() throws Exception{
        FileOutputStream out = new FileOutputStream(new File("create_table.docx"));
        document.write(out);
        out.close();
        System.out.println("create_table.docx written successully");

    }
}

