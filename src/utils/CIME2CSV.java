package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CIME2CSV {
    private String PROVINCE = "";
    private String OUTPUT_DIR = "files/";
    private final String FILE_SUFFIX = ".csv";
    private final String LINE_BREAK = "\r\n";
    private FileReader reader = null;
    private FileWriter writer = null;
    private BufferedReader bufferedReader = null;

    public CIME2CSV(String file, String province, String output_dir) {
        PROVINCE = province;
        OUTPUT_DIR = output_dir;
        try {
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

    }

    public void splitCIME2CSV() {
        if (!(new File(OUTPUT_DIR).isDirectory())) {
            new File(OUTPUT_DIR).mkdir();
        }

        try {
            String line = bufferedReader.readLine();
            String className = "UNKNOWN_CLASS";
            boolean isInClass = false;
            while (line != null) {
                if (isClassStartLine(line)) {
                    className = getClassName(line);
                    if (className != null) {
                        isInClass = true;
                        writer = new FileWriter(getCSVPath(className));
                    }
                    System.out.println("Found class: " + className);
                } else if (isInClass) {
                    if (isClassEndLine(line, className)) {
                        writer.close();
                        writer = null;
                        isInClass = false;
                    } else {
                        writer.append(prepareLine2Write(line + LINE_BREAK));
                    }
                }

                line = bufferedReader.readLine();
            }
            closeReaderFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeReaderFiles() {
        try {
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getClassName(String startLine) {
        int nameStart = startLine.indexOf('<') + 1;
        int nameEnd = startLine.indexOf(':');
        if (nameStart >= nameEnd) {
            System.out.println("Error: Class name cannot be parsed from [" + startLine + "]");
            return null;
        }

        return startLine.substring(nameStart, nameEnd);
    }

    private boolean isClassStartLine(String line) {
        return (line.indexOf('<') != -1 && line.indexOf('/') == -1 && line.contains("::" + PROVINCE + ">"));
    }

    private boolean isClassEndLine(String line, String className) {
        return (line.contains(className) && line.indexOf('<') != -1 && line.indexOf('/') != -1 && line.contains("::"
                + PROVINCE + ">"));
    }

    private String getCSVPath(String className) {
        return OUTPUT_DIR + className + FILE_SUFFIX;
    }

    private String removeLeadingPoundAndSpaces(String line) {
        if (line.contains("#  ")) {
            return line.substring("#  ".length());
        } else {
            return line;
        }
    }

    private String changeDelimeterFromTwoSpacesToComma(String line) {
        return line.replace("  ", ",");
    }

    private String fixMIRDName(String line) {
        return line.replace("mRID name", "mRID  name");
    }

    private String removeLeadingAT(String line) {
        if (line.indexOf('@') == 0) {
            return line.substring(1);
        } else {
            return line;
        }
    }

    private String removeCommentsSignforIndex(String line) {
        if (line.contains("//ÐòºÅ")) {
            return line.replace("//ÐòºÅ", "ÐòºÅ");
        } else {
            return line;
        }
    }

    private String prepareLine2Write(String line) {
        String str = removeLeadingPoundAndSpaces(line);
        str = removeLeadingAT(str);
        str = fixMIRDName(str);
        str = removeCommentsSignforIndex(str);

        return changeDelimeterFromTwoSpacesToComma(str);
    }

}
