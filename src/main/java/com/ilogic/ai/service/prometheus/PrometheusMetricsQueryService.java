package com.ilogic.ai.service.prometheus;

public interface PrometheusMetricsQueryService {

    /**
        * Executes an instant PromQL query against the Prometheus metrics endpoint.
        *
        * @param promQLQuery the fully formed PromQL string (e.g., error rate or latency query)
     * @return raw JSON string containing the metric time-series results
     */
    String executeQuery(String promQLQuery);
}
