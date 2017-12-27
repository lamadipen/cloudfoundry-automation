package com.cf.auto.controller;

import com.cf.auto.model.ServicePlan;
import com.cf.auto.service.IServicePlanService;
import com.cf.auto.service.impl.ServicePlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
@RestController
@RequestMapping("cf/")
public class ServicePlanController {
    @Autowired
    private IServicePlanService servicePlanService;

    @RequestMapping(value = "plan", method = RequestMethod.POST)
    public ServicePlan savePlan(@RequestBody ServicePlan servicePlan) {
        return servicePlanService.saveServicePlan(servicePlan);
    }

    @RequestMapping(value = "plan", method = RequestMethod.PUT)
    public  String updatePlan(){
        return "";
    }


    @RequestMapping(value = "plan", method = RequestMethod.GET)
    public List<ServicePlan> getPlans(){
        return servicePlanService.getServicePlans();
    }

    @RequestMapping(value = "plan/{id}", method = RequestMethod.GET)
    public ServicePlan getPlanById(@PathVariable Long id){

        return servicePlanService.getServicePlanById(id);
    }

    @RequestMapping(value = "plan", method = RequestMethod.DELETE)
    public String deletePlan(@RequestParam Long id){
        servicePlanService.deleteServicePlan(id);
        return "success";
    }
}
