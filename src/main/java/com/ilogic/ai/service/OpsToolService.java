package com.ilogic.ai.service;

import com.ilogic.ai.dto.AuditSummary;
import com.ilogic.ai.dto.ServerHealth;
import java.util.Random;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

@Service
public class OpsToolService {

    private final Random random = new Random();

    @Tool(description = "Check the health status and CPU utilization of a specified microservice or server node.")
    public ServerHealth checkServerHealth(String nodeName) {
        // Simulating enterprise infra metrics

        boolean isHealthy = random.nextDouble() > 0.2; //80% chances healthy
        int cpuUsage = random.nextInt(75);
        int memoryUsage = random.nextInt(50);

        String status = isHealthy ? "ONLINE" : "DEGRADED";
        return new ServerHealth(nodeName, status, cpuUsage + "%", memoryUsage + "%");
    }

    @Tool(description = "Fetch recent critical security audit logs or error counts for a given application namespace.")
    public AuditSummary getAuditSummary(String namespace) {
        int errorCount = random.nextInt(15);
        String lastIncident = errorCount > 5 ? "High latency spike detected on API gateway" : "None";

        return new AuditSummary(namespace, errorCount, lastIncident);
    }

}
