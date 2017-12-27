package com.cf.auto.repository;

import com.cf.auto.model.Pool;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by dipen on 12/26/2017.
 */
@Repository
public interface IPoolRepository extends CrudRepository<Pool,Long> {
}
