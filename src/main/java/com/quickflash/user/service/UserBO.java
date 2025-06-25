package com.quickflash.user.service;

import com.quickflash.common.HashUtils;
import com.quickflash.user.entity.UserEntity;
import com.quickflash.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserBO {
    private final UserRepository userRepository;

    public boolean addUser(
            String loginId
            , String password
            , String name
            , String defaultLocation
    ) {
        log.info("!!!!" + loginId);

            String hashedPassword = HashUtils.md5(password);
            UserEntity userEntity = UserEntity.builder()
                    .loginId(loginId)
                    .password(hashedPassword)
                    .name(name)
                    .defaultLocation(defaultLocation)
                    .build();
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + userEntity.getName());

            userRepository.save(userEntity);
            return true;




    }

    public Boolean isLoginIdDuplicated(String loginId){
        // return userRepository.findByLoginId(loginId).isEmpty();
        UserEntity user = userRepository.findByLoginId(loginId).orElse(null);
        return user != null;
    }
    public UserEntity getUserEntityByLoginIdAndPassword(String loginId, String password){
        String hashedPassword = HashUtils.md5(password);

        return userRepository.findByLoginIdAndPassword(loginId,hashedPassword).orElse(null);
    }


}

