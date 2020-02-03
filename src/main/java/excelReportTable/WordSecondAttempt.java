package excelReportTable;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class WordSecondAttempt {
    public static void main(String[] args) throws InvalidFormatException, IOException {
        File image1 = new File("207.jpg");
        File image2 = new File("207.jpg");

        addImagesToWordDocument(image1, image2);
    }

    public static void addImagesToWordDocument(File imageFile1, File imageFile2)
            throws IOException, InvalidFormatException {
        XWPFDocument doc = new XWPFDocument();
        XWPFParagraph paragraph = doc.createParagraph();
        XWPFRun run = paragraph.createRun();
        BufferedImage bimg1 = ImageIO.read(imageFile1);
        int width1 = bimg1.getWidth();
        int height1 = bimg1.getHeight();
        BufferedImage bimg2 = ImageIO.read(imageFile2);
        int width2 = bimg2.getWidth();
        int height2 = bimg2.getHeight();
        String imgFile1 = imageFile1.getName();
        String imgFile2 = imageFile2.getName();
        int imgFormat1 = getImageFormat(imgFile1);
        int imgFormat2 = getImageFormat(imgFile2);
        String p1 = "Sample Paragraph Post. This is a sample Paragraph post. Sample Paragraph text is being cut and pasted again and again. This is a sample Paragraph post. peru-duellmans-poison-dart-frog.";
        String p2 = "Sample Paragraph Post. This is a sample Paragraph post. Sample Paragraph text is being cut and pasted again and again. This is a sample Paragraph post. peru-duellmans-poison-dart-frog.";
        run.setText(p1);
//        run.addBreak();
        run.addPicture(new FileInputStream(imageFile1), imgFormat1, imgFile1, Units.toEMU(width1), Units.toEMU(height1));
        // page break
        // run.addBreak(BreakType.PAGE);
        // line break
        run.addBreak();
        run.setText(p2);
        run.addBreak();
        run.addPicture(new FileInputStream(imageFile2), imgFormat2, imgFile2, Units.toEMU(width2), Units.toEMU(height2));
        FileOutputStream out = new FileOutputStream("word_images.docx");
        doc.write(out);
        out.close();
        doc.close();
    }

    private static int getImageFormat(String imgFileName) {
        int format;
        if (imgFileName.endsWith(".emf"))
            format = XWPFDocument.PICTURE_TYPE_EMF;
        else if (imgFileName.endsWith(".wmf"))
            format = XWPFDocument.PICTURE_TYPE_WMF;
        else if (imgFileName.endsWith(".pict"))
            format = XWPFDocument.PICTURE_TYPE_PICT;
        else if (imgFileName.endsWith(".jpeg") || imgFileName.endsWith(".jpg"))
            format = XWPFDocument.PICTURE_TYPE_JPEG;
        else if (imgFileName.endsWith(".png"))
            format = XWPFDocument.PICTURE_TYPE_PNG;
        else if (imgFileName.endsWith(".dib"))
            format = XWPFDocument.PICTURE_TYPE_DIB;
        else if (imgFileName.endsWith(".gif"))
            format = XWPFDocument.PICTURE_TYPE_GIF;
        else if (imgFileName.endsWith(".tiff"))
            format = XWPFDocument.PICTURE_TYPE_TIFF;
        else if (imgFileName.endsWith(".eps"))
            format = XWPFDocument.PICTURE_TYPE_EPS;
        else if (imgFileName.endsWith(".bmp"))
            format = XWPFDocument.PICTURE_TYPE_BMP;
        else if (imgFileName.endsWith(".wpg"))
            format = XWPFDocument.PICTURE_TYPE_WPG;
        else {
            return 0;
        }
        return format;
    }
}


