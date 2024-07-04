package com.workshop.users.repositories;

import com.workshop.users.model.WishProductEntity;
import com.workshop.users.model.WishProductPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishProductDAORepository extends JpaRepository<WishProductEntity, WishProductPK> {
}
