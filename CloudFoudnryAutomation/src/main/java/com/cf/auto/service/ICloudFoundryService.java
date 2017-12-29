package com.cf.auto.service;

import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Created by dipen on 12/27/2017.
 */
public interface ICloudFoundryService {

    public void setUaaURL(String uaaURL);
    public void setPoolUrl(String poolUrl);

    public String checkServiceByName(String serviceName) throws IOException;
    public void deleteServiceIfExist(String serviceName) throws IOException;
    public String createUserProvidedService(String serviceName, String parameters) throws IOException, ParseException;
    public String createConfigServer(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createExternalDependency(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createCircuitBreaker(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createDynatrace(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createAppdynamics(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createGaiaStorageService(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createMariaDbMultiTenant(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createWilyIntroscope(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createServiceRegister(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createSSO(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;
    public String createQPid(String serviceName, String serviceLabel, String servicePlan, String parameters) throws IOException, ParseException;

}
