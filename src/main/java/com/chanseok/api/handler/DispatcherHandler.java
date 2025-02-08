package com.chanseok.api.handler;

import com.chanseok.infra.WrapperData;
import com.chanseok.loader.factory.InstanceFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DispatcherHandler {

    @PostMapping("/api")
    public Object handle(@RequestBody WrapperData param) {
        return InstanceFactory.executeMethod("com.chanseok.api.service.ApiService", param);
    }
}
