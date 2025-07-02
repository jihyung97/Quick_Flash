package com.quickflash.user.service;

import com.quickflash.common.HashUtils;
import com.quickflash.trust.service.TrustBO;
import com.quickflash.user.entity.UserEntity;
import com.quickflash.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final TrustBO trustBO;
    private final UserBO userBO;
   @Transactional
    public boolean addAllTableRelatedToUser(

            String loginId
            , String password
            , String name
            , String defaultLocation
    ) {
      int userId = userBO.addUser(loginId,  password, name, defaultLocation);

      trustBO.addTrust(userId);
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
