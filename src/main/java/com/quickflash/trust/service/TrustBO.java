package com.quickflash.trust.service;

import com.quickflash.meetingPost.service.Response;
import com.quickflash.meeting_join.entity.MeetingJoinEntity;
import com.quickflash.meeting_join.mapper.MeetingJoinMapper;
import com.quickflash.meeting_join.repository.MeetingJoinRepository;
import com.quickflash.trust.entity.TrustEntity;
import com.quickflash.trust.mapper.TrustMapper;
import com.quickflash.trust.repository.TrustRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static groovyjarjarantlr4.v4.gui.Trees.save;

@RequiredArgsConstructor
@Service
@Slf4j
public class TrustBO {
    private final TrustRepository trustRepository;
    private final TrustMapper trustMapper;


    public boolean addTrust(int userId){

        TrustEntity trustEntity = TrustEntity.builder()
                .userId(userId)
                .trustOfLeader(0.0)
                .trustOfMember(0.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        trustRepository. save(trustEntity);
        return true; // 나중에 try catch로 바꿀것
    }

    public boolean updateTrustByBatch(Map<Integer,Double> trustMap){

       trustMapper.updateTrustOfMemberByBatch(trustMap);
       return true;
    }
}

