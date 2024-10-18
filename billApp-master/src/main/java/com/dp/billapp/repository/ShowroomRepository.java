package com.dp.billapp.repository;

import com.dp.billapp.model.Showroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShowroomRepository extends JpaRepository<Showroom,Long> {
}
