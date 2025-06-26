package com.quickflash.join.service;


import com.quickflash.join.dto.JoinBeforeMeetingDto;
import com.quickflash.join.entity.JoinEntity;
import com.quickflash.join.mapper.JoinMapper;
import com.quickflash.join.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class JoinBO {
    private final JoinRepository joinRepository;
    private final JoinMapper joinMapper;
    List<JoinEntity> getJoinEntityListByPostId(int postId){
       return joinRepository.findByPostId(postId);
    }

    public Boolean isIMember(int postId, int userId){
        return joinRepository.findByPostIdAndUserId(postId,userId).isPresent();
    }

   public List<JoinBeforeMeetingDto> getJoinListForBeforeMeetingByPostId(int postId){
        return     joinMapper.selectJoinBeforeMeetingDtoListByPostId(postId);
   }

}
