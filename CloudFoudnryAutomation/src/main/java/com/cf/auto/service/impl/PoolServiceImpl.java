package com.cf.auto.service.impl;

import com.cf.auto.model.Pool;
import com.cf.auto.repository.IPoolRepository;
import com.cf.auto.service.IPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by dipen on 12/26/2017.
 */
@Service
public class PoolServiceImpl implements IPoolService {
    @Autowired
    private IPoolRepository poolRepository;

    @Override
    public Pool savePool(Pool pool) {
        return poolRepository.save(pool);
    }

    @Override
    public Pool getPoolByID(long id) {
        return poolRepository.findOne(id);
    }

    @Override
    public Pool updatePool(Pool pool) {
        return savePool(pool);
    }

    @Override
    public List<Pool> getPools() {
        return (List<Pool>) poolRepository.findAll();
    }

    @Override
    public void deletePool(long id) {
        poolRepository.delete(id);
    }
}
