package com.everyone.crowd.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    public static void deleteFile(String parent, String path) {
        if (path != null) {
            try {
                Files.delete(Paths.get(parent, path));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
