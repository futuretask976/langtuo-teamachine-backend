package com.langtuo.teamachine.mqtt.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
public class IOUtils {
    public static boolean removeFile(File root) {
        if (root == null || !root.exists()) {
            return true;
        }

        boolean result = false;
        if (root.isFile()) {
            result = root.delete();
        } else {
            File[] files = root.listFiles();
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                result = removeFile(file);
            }
            if (result) {
                result = root.delete();
            }
        }
        return result;
    }

    public static boolean writeStrToFile(String outputCont, File outputFile) {
        if (StringUtils.isBlank(outputCont) || outputCont == null) {
            return false;
        }

        boolean removed = removeFile(outputFile.getParentFile());
        if (!removed) {
            return false;
        }

        boolean maked = outputFile.getParentFile().mkdirs();
        if (!maked) {
            return false;
        }

        try {
            if (outputFile.exists()) {
                boolean deleted = outputFile.delete();
                if (!deleted) {
                    return false;
                }
            } else {
                boolean created = outputFile.createNewFile();
                if (!created) {
                    return false;
                }
            }
        } catch (IOException e) {
            log.error("create file error: " + e.getMessage(), e);
            return false;
        }

        FileWriter writer = null;
        try {
            writer = new FileWriter(outputFile);
            writer.write(outputCont);
            writer.flush();
            return true;
        } catch (IOException e) {
            log.error("write file error: " + e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    log.error("close writer error: " + e.getMessage(), e);
                }
            }
        }
        return false;
    }
}
