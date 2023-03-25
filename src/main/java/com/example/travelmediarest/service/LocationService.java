package com.example.travelmediarest.service;

import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class LocationService {
    @Autowired
    @Qualifier("locationRepository")
    LocationRepository locationRepository;

    public List<Location> fetchAllLocation() {
        List<Location> locationList = locationRepository.findAll();
        return locationList;
    }
}
