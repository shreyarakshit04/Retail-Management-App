package com.dp.billapp.repository;

import com.dp.billapp.model.Draft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DraftRepository extends JpaRepository<Draft,Long> {
    Optional<Draft> findByInvoiceId(String invoiceId);
}
