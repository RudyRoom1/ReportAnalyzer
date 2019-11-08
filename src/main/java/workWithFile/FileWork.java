package workWithFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class FileWork {

    private static String readingFileToString(String pathToFile) {
        String line = "";
        try (BufferedReader buffer = new BufferedReader(new FileReader(pathToFile))) {
            while (buffer.readLine() != null) {
                line = String.join("", buffer.lines().collect(Collectors.joining()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }

    public static ArrayList<String> readFileToList(String pathToFile) {
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String sCurrentLine;

            while ((sCurrentLine = br.readLine()) != null) {
                list.add(sCurrentLine);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeToFile(String element, String pathToDirectory) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(pathToDirectory));
            writer.write(element);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("The failedSku file was created");
    }

    public static void creatingFileMap(File directory, Map<String, String> fileNameContent) {
        for (final File fileEntry : Objects.requireNonNull(directory.listFiles())) {
            if (fileEntry.isDirectory()) {
                creatingFileMap(fileEntry, fileNameContent);
            } else {
                fileNameContent.put(fileEntry.getPath(), FileWork.readingFileToString(fileEntry.getAbsolutePath()));
            }
        }
    }

    public static List<String> readingDataFromFile(String pathToFailingSku) {
        return FileWork.readFileToList(pathToFailingSku);
    }

    public static List<String> readingDataFromFile(String pathToFailingSku, String pathToConstantlyFailingSku) {
        List<String> failingSku = FileWork.readFileToList(pathToFailingSku);
        List<String> constantlyFailingSku = FileWork.readFileToList(pathToConstantlyFailingSku);
        failingSku.removeAll(constantlyFailingSku);
        return failingSku;
    }

    public static String readProperty(String property) {
        String result = "";
        String propFileName = "config.properties";
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try (InputStream inputStream = loader.getResourceAsStream(propFileName)) {
            final Properties prop = new Properties();
            if (inputStream != null) {
                prop.load(inputStream);
            } else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
            result = prop.getProperty(property);
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
        if (result.equals("")){
            System.out.println(String.format("\n%s is't set! Set %s in \"config.properties\" file!!\n",property,property));
        }
        return result;
    }

    public static String showDifference(List<String> list1, List<String> list2) {
        System.out.println(list1.removeAll(list2));
        return String.join("\n", list1);
    }

    public static void makeDir(String name) {
        File directory = new File(name);
        if (directory.exists() && directory.isFile()) {
            System.out.println("The dir with name could not be" +
                    " created as it is a normal file");
        } else {
            if (!directory.exists()) {
                directory.mkdirs();
            }
        }
    }
}
