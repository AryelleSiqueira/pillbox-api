package com.ufes.pic2pillbox.repository;

import com.ufes.pic2pillbox.model.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Integer> {
}
