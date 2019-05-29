package com.testApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Valentin Chursin on 2019/05/29.
 */
public class Sort {


    public static void externalSortFiles(List<File> files, String outputFileName)
            throws IOException {

        List<BufferedReader> brReaders = new ArrayList<>();
        TreeMap<String, BufferedReader> map = new TreeMap<>();

        File f = new File(outputFileName);
        if (f.exists()) {
            f.delete();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName, true))) {
            try {
                for (File file : files) {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    brReaders.add(br);
                    String line = br.readLine();
                    if (line != null) {
                        map.put(line, br);
                    }
                }

                while (!map.isEmpty()) {
                    Map.Entry<String, BufferedReader> nextElement = map.pollFirstEntry();

                    bw.write(nextElement.getKey());
                    bw.write("\r\n");

                    String nextLine = nextElement.getValue().readLine();
                    if (StringUtils.isNotBlank(nextLine)) {
                        map.put(nextLine, nextElement.getValue());
                    }
                }
            } finally {
                for (BufferedReader br : brReaders) {
                    br.close();
                }
            }
        }
    }


}
