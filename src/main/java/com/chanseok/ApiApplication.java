package com.chanseok;

import com.chanseok.loader.factory.ClassLoaderFactory;
import com.chanseok.watcher.ClassFileWatcher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        ClassLoaderFactory.loadClasses();
        createClassWatch();
        SpringApplication.run(ApiApplication.class, args);
    }

    public static void createClassWatch() {
        ClassFileWatcher classFileWatcher = new ClassFileWatcher();
        new Thread(() -> {
            try {
                classFileWatcher.watch();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
