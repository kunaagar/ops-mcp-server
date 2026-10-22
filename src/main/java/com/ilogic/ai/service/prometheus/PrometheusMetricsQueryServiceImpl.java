package com.ilogic.ai.service.prometheus;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Slf4j
@Service
public class PrometheusMetricsQueryServiceImpl implements PrometheusMetricsQueryService {

    @Value("${app.prometheus.url}")
    private String prometheusUrl;

    private final RestClient prometheusClient;

    public PrometheusMetricsQueryServiceImpl(RestClient.Builder restClientBuilder) {
        this.prometheusClient = restClientBuilder
            .baseUrl("http://localhost:9090")
            .build();
    }

    @Override
    public String executeQuery(String promQLQuery) {
        log.info("Executing PromQL query against Prometheus: {}", promQLQuery);

        try {
            JsonNode response = prometheusClient.get()
                .uri(uriBuilder -> uriBuilder.path("/api/v1/query")
                    .queryParam("query", promQLQuery)
                    .build())
                .retrieve()
                .body(JsonNode.class);

            if (response != null && response.has("data")) {
                return response.get("data").toPrettyString();
            }

            log.warn("Prometheus returned an empty or malformed response for query: {}", promQLQuery);
            return "No data returned from Prometheus for query: " + promQLQuery;

        } catch (RestClientException e) {
            log.error("Communication failure while querying Prometheus server: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to communicate with Prometheus server: " + e.getMessage(), e);
        }
    }
}
