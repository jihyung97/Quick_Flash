package com.quickflash.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticClientFactory {
    @Bean
    public  ElasticsearchClient initiateClient(ObjectMapper objectMapper) {
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();

        RestClientTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(objectMapper)
        );

        return new ElasticsearchClient(transport);
    }


    @Bean
    public ElasticsearchClient elasticsearchClient() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        // 타입 정보 포함 비활성화 (activateDefaultTyping 호출 안함)

        // RestClient 생성
        RestClient restClient = RestClient.builder(
                new HttpHost("localhost", 9200)
        ).build();

        // Elasticsearch 클라이언트 전송부에 JacksonJsonpMapper 적용
        RestClientTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper(objectMapper)
        );

        return new ElasticsearchClient(transport);
    }
}
