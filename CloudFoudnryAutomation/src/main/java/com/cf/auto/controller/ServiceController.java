package com.cf.auto.controller;

import com.cf.auto.model.Service;
import com.cf.auto.service.IServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
public class ServiceController {
    @Autowired
    private IServiceService serviceService;

    @RequestMapping(value = "service", method = RequestMethod.POST)
    public Service saveService(Service service){
        return serviceService.saveService(service);
    }
    @RequestMapping(value = "service", method = RequestMethod.PUT)
    public  Service updateService(Service service){
        return serviceService.updateService(service);
    }

    @RequestMapping(value = "service/{id}", method = RequestMethod.GET)
    public Service getServiceById(@PathVariable Long id){
        return serviceService.getServiceById(id);
    }

    @RequestMapping(value = "service", method = RequestMethod.GET)
    public List<Service> getServices(){
        return serviceService.getServices();
    }

    @RequestMapping(value = "service/{id}", method = RequestMethod.DELETE)
    public String deleteService(@PathVariable long id){
        serviceService.deleteService(id);
        return "success";
    }
}
