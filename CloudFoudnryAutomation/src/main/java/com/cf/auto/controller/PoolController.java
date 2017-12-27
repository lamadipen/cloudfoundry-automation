package com.cf.auto.controller;

import com.cf.auto.model.Pool;
import com.cf.auto.model.ServicePlan;
import com.cf.auto.service.IPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
@RestController
@RequestMapping("cf/")
public class PoolController {
    @Autowired
    private IPoolService poolService;

    @RequestMapping(value = "pool", method = RequestMethod.POST)
    public Pool savePool(Pool pool){
        return poolService.savePool(pool);
    }
    @RequestMapping(value = "pool", method = RequestMethod.PUT)
    public  Pool updatePool(Pool pool){
        return poolService.updatePool(pool);
    }

    @RequestMapping(value = "pool/{id}", method = RequestMethod.GET)
    public Pool getPoolById(@PathVariable Long id){
        return poolService.getPoolByID(id);
    }

    @RequestMapping(value = "pool", method = RequestMethod.GET)
    public List<Pool> getPools(){
        return poolService.getPools();
    }

    @RequestMapping(value = "pool/{id}", method = RequestMethod.DELETE)
    public String deletePool(@PathVariable long id){
        poolService.deletePool(id);
        return "success";
    }
}
