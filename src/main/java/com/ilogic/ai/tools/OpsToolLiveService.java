package com.ilogic.ai.tools;

import com.ilogic.ai.service.prometheus.PrometheusMetricsQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OpsToolLiveService {

    private final PrometheusMetricsQueryService service;

    @Tool(description = "Execute a PromQL query against the Prometheus metrics server to fetch real-time telemetry data, including p95/p99 latencies, error rates, and request throughput and all types of metrics to perform observability actions.")
    public String queryPrometheusMetrics(String promQLQuery) {
        return service.executeQuery(promQLQuery);
    }
}
