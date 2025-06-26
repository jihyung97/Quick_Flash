package com.quickflash.user.repository;

import com.quickflash.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    public Optional<UserEntity> findByLoginId(String loginId);
   public Optional<UserEntity> findByLoginIdAndPassword(String loginId, String password);
    @Query("SELECT userEntity.name FROM UserEntity userEntity WHERE userEntity.id = :id")
    String findNameById(@Param("id") Integer id);
//    //userName 중복 불가능 설정 해야됨 (회원가입), table name unique 키로
//    public Optional<UserEntity> findFirstByName(String userName);
//    public Optional<UserEntity> findById(int Id);
//    List<UserEntity> findByIdIn(List<Integer> userIds);
}

