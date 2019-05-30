package com.testApp;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * Created by Valentin Chursin on 2019/05/29.
 */
public class FileGenerator {

    private static final String STRINGS = "stringQuantity_";
    private static final String LENGTH = "maxLength_";

    File generateSourceFile(String path, int stringQuantity, int maxLength) throws IOException {
        File file = new File(path + STRINGS + stringQuantity + "_" + LENGTH + maxLength);
        FileWriter fw = new FileWriter(file);

        int i = 0;
        while (i < stringQuantity - 1) {
            String s = RandomStringUtils.randomAlphanumeric(1, maxLength);
            fw.write(s);
            fw.write("\n");
            i++;
        }
        System.out.println("File successfully created, file size: " + file.getTotalSpace());
        return file;
    }
}
