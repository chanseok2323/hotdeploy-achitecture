package com.chanseok.infra;

public class WrapperData {
    private String name;
    private String age;

    public WrapperData() {
    }

    public WrapperData(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }
}
