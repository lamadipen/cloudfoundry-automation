package com.cf.auto.controller;

import com.cf.auto.model.ServiceLabel;
import com.cf.auto.service.IServiceLabelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
public class ServiceLabelController {
    private IServiceLabelService serviceLabelService;

    @RequestMapping(value = "plan", method = RequestMethod.POST)
    public ServiceLabel savePlan(@RequestBody ServiceLabel serviceLabel) {
        return serviceLabelService.saveServiceLabel(serviceLabel);
    }

    @RequestMapping(value = "plan", method = RequestMethod.PUT)
    public  String updatePlan(){
        return "";
    }


    @RequestMapping(value = "plan", method = RequestMethod.GET)
    public List<ServiceLabel> getPlans(){
        return serviceLabelService.getServiceLabels();
    }

    @RequestMapping(value = "plan/{id}", method = RequestMethod.GET)
    public ServiceLabel getPlanById(@PathVariable Long id){

        return serviceLabelService.getServiceLabelById(id);
    }

    @RequestMapping(value = "plan/{id}", method = RequestMethod.DELETE)
    public String deletePlan(@PathVariable Long id){
        serviceLabelService.deleteServiceLabel(id);
        return "success";
    }
}
