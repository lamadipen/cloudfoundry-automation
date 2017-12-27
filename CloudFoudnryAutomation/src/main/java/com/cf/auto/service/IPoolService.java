package com.cf.auto.service;

import com.cf.auto.model.Pool;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
public interface IPoolService {
    public Pool savePool(Pool pool);
    public Pool getPoolByID(long id);
    public Pool updatePool(Pool pool);
    public List<Pool> getPools();
    public void deletePool(long id);
}
