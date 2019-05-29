package com.testApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class App {

    public static void main(String[] args) throws IOException {
        String outputDir = "/home/valentin/Documents/tmp/";
        String outputFile = null;
        int stringQuantity = 100;
        int maxLength = 40;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-dir":
                    outputDir = args[i + 1];
                    i++;
                    break;
                case "-file":
                    outputFile = args[i + 1];
                    i++;
                    break;
                case "-count":
                    if (args[i + 1] == null || !StringUtils.isNumeric(args[i + 1])) {
                        System.out.println("count argument must be number");
                        return;
                    }
                    stringQuantity = Integer.valueOf(args[i + 1]);
                    i++;
                    break;
                case "-length":
                    if (args[i + 1] == null || !StringUtils.isNumeric(args[i + 1])) {
                        System.out.println("length argument must be number");
                        return;
                    }
                    maxLength = Integer.valueOf(args[i + 1]);
                    i++;
                    break;
            }
        }

        if (outputFile == null) {
            outputFile = "/result_" + stringQuantity + "_" + maxLength + ".txt";
        }

        FileGenerator fileGenerator = new FileGenerator();
        File sourceFile = fileGenerator.generateSourceFile(outputDir, stringQuantity, maxLength);

        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile), Charset.forName("UTF-8")));

        List<File> tmpFiles = FileSplitter.splitAndSortFiles(br, sourceFile.length(), new File(outputDir));

        Sort.externalSortFiles(tmpFiles, outputDir + outputFile);

    }

}
