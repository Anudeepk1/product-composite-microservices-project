package com.library.common_service.utils.core.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class ServiceUtil {
    private String port;

    private String serviceAddress = null;

    @Autowired
    public ServiceUtil(@Value("${server.port}") String port){
        this.port = port;
    }

    public String getServiceAddress(){
        if(serviceAddress == null){
            serviceAddress = findHostName() + "/" + findHostAddress() + ":" + port;
        }
        return serviceAddress;
    }

    private String findHostAddress() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return localHost.getHostAddress();
    }

    private String findHostName() {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return localHost.getHostName();
    }
}
