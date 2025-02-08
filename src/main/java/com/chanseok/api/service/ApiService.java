package com.chanseok.api.service;

import com.chanseok.infra.WrapperData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ApiService {
    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    public WrapperData execute(WrapperData wrapperData) {
        logger.info("wrapperData = {}", wrapperData);
        logger.info("wrapperData.getName() = {}", wrapperData.getName());
        logger.info("wrapperData.getAge() = {}", wrapperData.getAge());
        return new WrapperData();
    }

}
