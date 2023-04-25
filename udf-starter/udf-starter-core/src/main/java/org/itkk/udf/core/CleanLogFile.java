package org.itkk.udf.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * CleanLogFile
 */
@Component
@Slf4j
public class CleanLogFile {

    /**
     * logFile
     */
    @Value("${logging.file}")
    private String logFile;

    /**
     * 清理日志文件
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void cleanLog() {
        log.info("cleanLog begin : {}", logFile);
        File file = new File(logFile);
        if (file.exists()) {
            File dir = file.getParentFile();
            if (dir.exists() && dir.isDirectory()) {
                File[] files = dir.listFiles();
                if (ArrayUtils.isNotEmpty(files)) {
                    Arrays.stream(files).filter(f -> !logFile.equals(f.getAbsolutePath())).forEach(f -> {
                        try {
                            log.info("cleanLog delete : {}", f.getAbsolutePath());
                            Files.delete(f.toPath());
                        } catch (IOException e) {
                            log.warn("msg:", e);
                        }
                    });
                }
            }
        }
        log.info("cleanLog end : {}", logFile);
    }

}
