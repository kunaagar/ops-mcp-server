package com.ilogic.ai.service;

import org.springaicommunity.mcp.annotation.McpResource;
import org.springframework.stereotype.Service;

@Service
public class OpsResourceService {

    // ==========================================
    // 1. MCP RESOURCE: Live Server Logs via URI
    // ==========================================

    @McpResource(
        uri = "logs://{serviceName}/{environment}",
        name = "Application Logs",
        description = "Fetch live system logs for a given microservice and environment."
    )
    public String getServiceLogs(String serviceName, String environment) {
        // Simulating log output retrieval
        return """
            [INFO] 2026-10-22 10:15:02 - Starting %s in %s environment
            [WARN] 2026-10-22 10:16:45 - Connection pool usage exceeded 80%%
            [ERROR] 2026-10-22 10:18:12 - Database connection timeout on node-db-02
            [INFO] 2026-10-22 10:19:00 - Circuit breaker state changed to HALF_OPEN
            """.formatted(serviceName, environment);
    }

}
