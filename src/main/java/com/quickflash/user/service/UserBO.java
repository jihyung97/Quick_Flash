package com.quickflash.user.service;

import com.quickflash.common.HashUtils;
import com.quickflash.user.dto.UserIdToNameDto;
import com.quickflash.user.entity.UserEntity;
import com.quickflash.user.mapper.UserMapper;
import com.quickflash.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserBO {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public int addUser(
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

          return  userRepository.save(userEntity).getId(); // User를 생성하는 동시에 그 id로 trust 테이블을 만들기 위해 addUser는 userId를 반환





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

    public String getUserNameById(int id){
        return userRepository.findNameById(id);
    }
    public List<UserIdToNameDto> getIdToUserNameMapByIdSet(Set<Integer> idSet){

        if(idSet == null || idSet.isEmpty()){
            return null;
        }
       return userMapper.selectIdToUserNameMapByIdSet(idSet);

    }


    public List<Integer> getUserIdsForTrustBatch(int offset, int batch) {
        log.info("");
        List<Integer> batchList = userMapper.selectUserIdsForBatch(offset,batch);
        log.info("{}",batchList);
        return batchList;
    }



}

