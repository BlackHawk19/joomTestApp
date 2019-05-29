package com.testApp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Valentin Chursin on 2019/05/29.
 */
public class FileSplitter {

    private static final int MAX_TMP_FILES = 30;
    private static final int SYSTEM_STRING_SIZE_OVERHEAD = 16 + 24 + 8 + 12;

    private static Comparator<String> stringComparator = String::compareTo;

    public static List<File> splitAndSortFiles(BufferedReader br, long dataSize, File dir)
            throws IOException {
        System.out.println("start splitting big file, dataSize : " + dataSize + ", directory : " + dir);
        List<File> files = new ArrayList<>();
        long maxMemory = getAvailableMemory();
        long allowedBlockSize = calculateBlockSize(dataSize, maxMemory);

        System.out.println("allowedBlockSize : " + allowedBlockSize);

        try {
            List<String> tmpStrs = new ArrayList<>();
            String line = "";
            int counter = 1;
            try {
                while (line != null) {
                    long currentBlockSize = 0;// in bytes
                    while ((currentBlockSize < allowedBlockSize)
                            && ((line = br.readLine()) != null)) {

                        tmpStrs.add(line);
                        currentBlockSize += getStringSize(line);
                    }
                    counter++;
                    files.add(sortStringAndSaveToFile(tmpStrs, dir, counter));
                    tmpStrs.clear();
                }
            } catch (EOFException ex) {
                System.out.println("exception : " + ex.getMessage());
                if (tmpStrs.size() > 0) {
                    counter++;
                    files.add(sortStringAndSaveToFile(tmpStrs, dir, counter));
                    tmpStrs.clear();
                }
            }
        } finally {
            br.close();
        }
        return files;
    }

    private static File sortStringAndSaveToFile(List<String> tmplist, File tmpDir, int number) throws IOException {
        tmplist.sort(stringComparator);
        File tmpFile = File.createTempFile("part", String.valueOf(number), tmpDir);
        tmpFile.deleteOnExit();
        OutputStream out = new FileOutputStream(tmpFile);

        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8")))) {
            for (String str : tmplist) {
                bw.write(str);
                bw.newLine();
            }
        }
        System.out.println("created tmp file : " + tmpFile.getName());
        return tmpFile;
    }

    private static long getStringSize(String str) {
        return (str.length() * 2) + SYSTEM_STRING_SIZE_OVERHEAD;
    }

    private static long getAvailableMemory() {
        Runtime r = Runtime.getRuntime();
        long allocatedMemory = r.totalMemory() - r.freeMemory();
        return r.maxMemory() - allocatedMemory;
    }

    private static long calculateBlockSize(long fileSize, final long maxMemory) {
        long blocksize = fileSize / MAX_TMP_FILES + (fileSize % MAX_TMP_FILES == 0 ? 0 : 1);

        if (blocksize < maxMemory / 2) {
            blocksize = maxMemory / 2;
        }
        return blocksize;
    }

}
