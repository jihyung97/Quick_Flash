package com.quickflash.trust.repository;

import com.quickflash.trust.entity.TrustEntity;
import com.quickflash.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TrustRepository extends JpaRepository<TrustEntity,Integer> {


}
