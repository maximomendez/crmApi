package com.pathmonk.crmapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pathmonk.crmapi.model.Deal;

@Repository
public interface DealRepo extends JpaRepository<Deal, Long> {
}
