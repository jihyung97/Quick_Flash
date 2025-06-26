package com.quickflash.join.service;

import com.quickflash.join.dto.JoinBeforeMeetingDto;
import com.quickflash.join.entity.JoinEntity;
import com.quickflash.join.repository.JoinRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class JoinService {
    private final JoinBO joinBO;

   public List<JoinBeforeMeetingDto> generateJoinBeforeMeetingDtoListByPostId(int postId){

       List<JoinBeforeMeetingDto> joinBeforeList =  joinBO.getJoinListForBeforeMeetingByPostId(postId);
       Set<Integer> userIdSet = new HashSet<>();

       for(JoinBeforeMeetingDto joinBefore : joinBeforeList){
           userIdSet.add(joinBefore.getUserId());
       }




     // ability table에서 userIdSet으로 UserIdToAbility Map 만들기
       for(JoinBeforeMeetingDto joinBeforeMeetingDto : joinBeforeList){

       }
       return joinBeforeList;
   }

}
