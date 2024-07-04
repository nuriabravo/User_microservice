package com.workshop.users.repositories;

import com.workshop.users.model.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressDAORepository extends JpaRepository<AddressEntity,Long> {
}
