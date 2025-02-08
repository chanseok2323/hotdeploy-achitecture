package com.chanseok.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

public class CustomClassLoader extends ClassLoader {
    private static final Logger logger = LoggerFactory.getLogger(CustomClassLoader.class);
    private Map<String, String> classFilePathMap;

    public CustomClassLoader() {
        super(ClassLoader.getSystemClassLoader());
        this.classFilePathMap = new LinkedHashMap<>();
    }

    /**
     * 해당 클래스를 찾은 후 Class<> 변환
     * @param name
     *          The <a href="#binary-name">binary name</a> of the class
     *
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        logger.info("findClassName = {}", name);

        if (!classFilePathMap.containsKey(name)) {
            return super.findClass(name);
        }

        try {
            byte[] classData = loadClassData(classFilePathMap.get(name));
            return defineClass(name, classData, 0, classData.length);
        } catch (IOException e) {
            throw new ClassNotFoundException("Could not load class " + name, e);
        }
    }

    /**
     * 클래스 파일 로드
     * @param name
     *          The <a href="#binary-name">binary name</a> of the class
     *
     * @param resolve
     *          If {@code true} then resolve the class
     *
     * @return
     * @throws ClassNotFoundException
     */
    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        synchronized (getClassLoadingLock(name)) {
            // 이미 로드 된 클래스인지 확인
            Class<?> loadedClass = findLoadedClass(name);
            if (loadedClass == null) {
                try {
                    // 직접 findClass 호출
                    loadedClass = findClass(name);
                } catch (ClassNotFoundException e) {
                    // 부모 클래스로더에게 로드 요청
                    loadedClass = getParent().loadClass(name);
                }
            }
            if (resolve) {
                resolveClass(loadedClass);
            }
            return loadedClass;
        }
    }

    private byte[] loadClassData(String filePath) throws IOException {
        try (InputStream inputStream = new FileInputStream(filePath);
             ByteArrayOutputStream byteStream = new ByteArrayOutputStream()) {
            int nextValue;
            while ((nextValue = inputStream.read()) != -1) {
                byteStream.write(nextValue);
            }
            return byteStream.toByteArray();
        }
    }

    public void addClassFilePath(String className, String classFilePath) {
        classFilePathMap.put(className, classFilePath);
    }
}
