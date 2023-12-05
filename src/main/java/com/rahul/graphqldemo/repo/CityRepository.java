package com.rahul.graphqldemo.repo;

import com.rahul.graphqldemo.model.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<CityEntity, Long> {
}
