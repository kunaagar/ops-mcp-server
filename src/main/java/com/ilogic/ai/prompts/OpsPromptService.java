package com.ilogic.ai.prompts;

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.util.List;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Service;

@Service
public class OpsPromptService {

    // ==========================================
    // 2. MCP PROMPT: Pre-defined Incident Report Prompt Template
    // ==========================================
    @McpPrompt(
        name = "incident_triage",
        description = "Generate a structured incident response analysis for an operational alert."
    )
    public GetPromptResult generateIncidentPrompt(
        @McpArg(description = "Service or application name", required = true) String serviceName,
        @McpArg(description = "Severity level (e.g., CRITICAL, HIGH, MEDIUM)", required = true) String severity
    ) {
        String template = """
            You are an expert SRE on duty. An incident has been triggered for:
            - Service: %s
            - Severity: %s
            
            Please perform the following steps:
            1. Query health metrics using server tools.
            2. Fetch recent logs using the resource URI `logs://%s/production`.
            3. Provide a root cause summary and immediate remediation steps.
            """.formatted(serviceName, severity, serviceName);

        return new GetPromptResult(
            "Incident Response Workflow",
            List.of(new PromptMessage(Role.USER, new TextContent(template)))
        );
    }
}
