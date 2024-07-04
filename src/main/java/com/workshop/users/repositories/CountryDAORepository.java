package com.workshop.users.repositories;

import com.workshop.users.model.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryDAORepository extends JpaRepository<CountryEntity,Long> {
    Optional<CountryEntity> findByName(String name);

}
