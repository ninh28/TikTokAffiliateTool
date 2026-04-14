package com.ninhquachhai.tiktoktool.repository;

import com.ninhquachhai.tiktoktool.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, String> {
    List<AppUser> findAllByOrderByLastLoginDesc();
}
