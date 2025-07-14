package com.quickflash.meetingPost.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.quickflash.meetingPost.dto.MeetingPostElasticIndexDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MeetingPostElasticSearchService {

    private final ElasticsearchClient elasticsearchClient;

    public void indexMeetingPost(MeetingPostElasticIndexDto dto) {
        try {
            IndexResponse response = elasticsearchClient.index(i -> i
                    .index("meetingpost")                    // 인덱스명
                    .id(dto.getId().toString())              // 문서 ID (선택)
                    .document(dto)                           // 저장할 객체
            );
            log.info("색인 완료: {}", response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//
public List<MeetingPostElasticIndexDto> searchMeetingPost(String keyword) {
    try {
        SearchResponse<MeetingPostElasticIndexDto> response = elasticsearchClient.search(s -> s
                .index("meetingpost")
                .query(q -> q
                        .multiMatch(m -> m
                                .query(keyword)
                                .fields("location", "contentText")
                        )
                ), MeetingPostElasticIndexDto.class);

        log.info("검색 완료: total hits={}", response.hits().total().value());

        List<MeetingPostElasticIndexDto> results = response.hits().hits().stream()
                .map(Hit::source)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("검색 결과 DTO 목록: {}", results);
        return results;

    } catch (Exception e) {
        log.error("검색 실패", e);
        return Collections.emptyList();
    }
}

}
