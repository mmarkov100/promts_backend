package com.promts.promts_test_server.promts_db.repository;

import com.promts.promts_test_server.promts_db.entity.user.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUidFirebase(String uidFirebase);
}
