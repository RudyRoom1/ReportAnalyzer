package excelReportTable;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import pages.ReportPage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class WordReportTable {

    private static XWPFDocument document = new XWPFDocument();

    public static void generate(ReportPage page) throws Exception {
        XWPFTable table = document.createTable(1,2);

        XWPFTableRow tableRowOne = table.getRow(0);

        addPictureToCell(page.getPieChartImage(), tableRowOne.getCell(0));
        List<String> listOffailureReasons = new ArrayList();
        page.getTestFailureReasonsWithCount().forEach((k, v) -> listOffailureReasons.add(String.format("%s\t%s\t", v, k)));
        listOffailureReasons.sort(Comparator.comparingInt(o1 -> Integer.parseInt(((String) o1).split("\t")[0])).reversed());

        addHeaderToCell(page,tableRowOne.getCell(1));
        addPictureToCell(page.getTestSummaryTablePic(), tableRowOne.getCell(1));
        XWPFRun runOfTextCell = tableRowOne.getCell(1).addParagraph().createRun();

        runOfTextCell.setText("Analysis:");
        for (String line : listOffailureReasons) {
            runOfTextCell.setText(line);
            runOfTextCell.addBreak();
        }
    }

    public static void addPictureToCell(File image, XWPFTableCell cell) throws IOException, InvalidFormatException {
        XWPFParagraph paragraph = cell.addParagraph();
        XWPFRun run = paragraph.createRun();


        File image1 = image;
        BufferedImage bimg1 = ImageIO.read(image1);
        int width1 = bimg1.getWidth() / 2;
        int height1 = bimg1.getHeight() / 2;
        run.addPicture(new FileInputStream(image1), XWPFDocument.PICTURE_TYPE_JPEG, "1.jpg", Units.toEMU(width1), Units.toEMU(height1));
    }

    public static void addHeaderToCell(ReportPage page , XWPFTableCell cell) throws IOException, InvalidFormatException {
        XWPFParagraph paragraph = cell.addParagraph();
        XWPFRun run = paragraph.createRun();
        run.setText(page.getNameOfJob());
        run.addBreak();

    }

    public static void write(String filePath) throws Exception {

        String timeNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd_HH-mm"));

        FileOutputStream out = new FileOutputStream(new File(filePath +"Bi-Weekly_"+ timeNow+ ".docx"));
        document.write(out);
        out.close();
        System.out.println("create_table.docx written successfully");

    }
}

