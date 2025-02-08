package com.chanseok.watcher;

import com.chanseok.loader.factory.ClassLoaderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

public class ClassFileWatcher {
    private static final Logger logger = LoggerFactory.getLogger(ClassFileWatcher.class);

    private static Path directoryToWatch;

    public ClassFileWatcher() {
        directoryToWatch = Paths.get(ClassLoaderFactory.getProjectPath());
    }

    public ClassFileWatcher(String directory) {
        directoryToWatch = Paths.get(directory);
    }

    @Async
    public void watch() {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            directoryToWatch.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        logger.info("File modified: {}", event.context());
                        ClassLoaderFactory.reloadClasses();
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
