package com.ufes.pic2pillbox.repository;

import com.ufes.pic2pillbox.model.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SlotRepository extends JpaRepository<Slot, Integer> {

    List<Slot> findAllByUserId(Integer userId);
}
