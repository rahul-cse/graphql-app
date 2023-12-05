package com.rahul.graphqldemo.service;

import com.rahul.graphqldemo.model.entity.CityEntity;
import com.rahul.graphqldemo.repo.CityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CityService {

    CityRepository cityRepository;

    public void save(CityEntity cityEntity){
        cityRepository.save(cityEntity);
    }

    public List<CityEntity> getAll(){
        return cityRepository.findAll();
    }
}
