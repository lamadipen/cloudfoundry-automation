package com.cf.auto.controller;

import com.cf.auto.model.Pool;
import com.cf.auto.model.Service;
import com.cf.auto.model.ServicePlan;
import com.cf.auto.service.ICloudFoundryService;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * Created by dipen on 12/29/2017.
 */
@RestController
@RequestMapping("cf/")
public class CloudFoundryController {
    @Autowired
    private ICloudFoundryService cloudFoundryService;

    @RequestMapping(value = "deploy", method = RequestMethod.POST)
    public String generateCloudService(@RequestBody List<Service> services){
        services.stream().forEach((service) -> {
            try {
                createService(service);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        });
        return "success";
    }

    private void createService(Service service) throws IOException, ParseException {
        String serviceName = service.getServiceName();
        String parameters = service.getParameters();
        ServicePlan servicePlan = service.getServicePlan();
        String planName = servicePlan.getPlanName();
        String serviceLabel = servicePlan.getServiceLabel().getLableName();

        cloudFoundryService.setPoolUrl(service.getPool().getPoolURL());
        cloudFoundryService.setUaaURL(service.getPool().getPoolUaaURL());
        cloudFoundryService.deleteServiceIfExist(service.getServiceName());

        String result = "";
        switch(serviceLabel.toLowerCase()){
            case "external dependency":
                result = cloudFoundryService.createExternalDependency(serviceName,"external-dependency-servcie",planName,parameters);
                break;
            case "config server":
                result = cloudFoundryService.createExternalDependency(serviceName,"p-config-server",planName,parameters);
                break;
            case "user provided service":
                result = cloudFoundryService.createUserProvidedService(serviceName,parameters);
                break;
            case "circuit breaker dashboard":
                result = cloudFoundryService.createExternalDependency(serviceName,"p-circuit-breaker-dashboard",planName,parameters);
                break;
            case "dynatrace":
                result = cloudFoundryService.createExternalDependency(serviceName,"Dynatrace",planName,parameters);
                break;
            case "appdynamics":
                result = cloudFoundryService.createExternalDependency(serviceName,"Appdynamics",planName,parameters);
                break;
            case "gaia storage service":
                result = cloudFoundryService.createExternalDependency(serviceName,"object-store",planName,parameters);
                break;
            case "mariadb multi tenant":
                result = cloudFoundryService.createExternalDependency(serviceName,"p-mysql",planName,parameters);
                break;
            case "wilyintroscope":
                result = cloudFoundryService.createExternalDependency(serviceName,"WilyIntroscope",planName,parameters);
                break;
            case "service registery":
                result = cloudFoundryService.createExternalDependency(serviceName,"p-service-registery",planName,parameters);
                break;
            case "sso":
                result = cloudFoundryService.createExternalDependency(serviceName,"sso",planName,parameters);
                break;
            case "qpid":
                result = cloudFoundryService.createExternalDependency(serviceName,"qpid",planName,parameters);
                break;
            default:
                result = "No service available";
                break;
        }
    }
}
