package com.ufes.pic2pillbox.repository;

import com.ufes.pic2pillbox.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeRepository extends JpaRepository<Code, Integer> {

    Optional<Code> findByUserId(Integer userId);
}
