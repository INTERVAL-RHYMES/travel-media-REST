package com.example.travelmediarest.config;

import com.example.travelmediarest.model.Location;
import com.example.travelmediarest.repository.LocationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DevelopmentConfig {

    @Bean
    public CommandLineRunner dataLoader(LocationRepository locationRepository) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                System.out.println("CommandLineRunner location");
                locationRepository.save(new Location("Dhaka"));
                locationRepository.save(new Location("Sylhet"));
                locationRepository.save(new Location("Dinajpur"));
                locationRepository.save(new Location("Barishal"));
                locationRepository.save(new Location("Bandarban"));
                locationRepository.save(new Location("Khulna"));
                locationRepository.save(new Location("Rajshahi"));
            }
        };
    }
}
