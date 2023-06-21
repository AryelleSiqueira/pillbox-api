package com.ufes.pic2pillbox.repository;

import com.ufes.pic2pillbox.model.Alarm;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends CrudRepository<Alarm, Integer> {
}
