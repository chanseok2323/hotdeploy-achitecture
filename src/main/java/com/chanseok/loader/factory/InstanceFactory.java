package com.chanseok.loader.factory;

import com.chanseok.infra.WrapperData;
import com.chanseok.loader.CustomClassLoader;

public class InstanceFactory {


    public static Object newInstance(String className) {
        try {
            CustomClassLoader classLoader = ClassLoaderFactory.getClassLoader();
            if (classLoader == null) {
                throw new IllegalStateException("Class loader not initialized!");
            }

            Class<?> findClazz = classLoader.loadClass(className);
            return findClazz.getDeclaredConstructor().newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static WrapperData executeMethod(String className, WrapperData wrapperData) {
        Object o = newInstance(className);
        WrapperData data;
        try {
            return (WrapperData) o.getClass().getMethod("execute", WrapperData.class).invoke(o, wrapperData);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
