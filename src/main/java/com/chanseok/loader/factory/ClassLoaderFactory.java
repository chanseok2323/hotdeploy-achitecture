package com.chanseok.loader.factory;

import com.chanseok.loader.CustomClassLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClassLoaderFactory {
    private static final Logger logger = LoggerFactory.getLogger(ClassLoaderFactory.class);
    private static final String PROJECT_PATH = "/Users/parkchanseok/Develop/hotdeploy-achitecture/build/classes/java/main/com/chanseok/api";
    private static CustomClassLoader classLoader;

    public static void loadClasses() {
        try {
            List<String> classFiles = new ArrayList<>();
            findClassFile(new File(PROJECT_PATH), classFiles);

            classLoader = new CustomClassLoader();
            for (String classFile : classFiles) {
                String className = convertToClassName(classFile);
                classLoader.addClassFilePath(className, classFile);
                Class<?> loadedClass = classLoader.loadClass(className);
                logger.info("loadedClass = {}", loadedClass);
            }
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reloadClasses() {
        loadClasses();
    }

    private static void findClassFile(File directory, List<String> classFiles) {
        for (File file : directory.listFiles()) {
            if(file.isDirectory()) {
                findClassFile(file, classFiles);
            } else if(file.getName().endsWith(".class")){
                classFiles.add(file.getAbsolutePath());
            }
        }
    }

    private static String convertToClassName(String classFilePath) {
        String basePath = new File(PROJECT_PATH).getAbsolutePath();
        return "com.chanseok.api." + classFilePath.replace(basePath + File.separator, "")
                .replace(File.separator, ".")
                .replace(".class", "");
    }

    public static CustomClassLoader getClassLoader() {
        return classLoader;
    }

    public static String getProjectPath() {
        return PROJECT_PATH;
    }
}
