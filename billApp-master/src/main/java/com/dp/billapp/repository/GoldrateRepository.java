package com.dp.billapp.repository;

import com.dp.billapp.model.GoldRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoldrateRepository extends JpaRepository<GoldRate,Long> {

}
