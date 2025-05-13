package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.transaction.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentTransactionRepository
        extends JpaRepository<PaymentTransaction, Long> {

}
