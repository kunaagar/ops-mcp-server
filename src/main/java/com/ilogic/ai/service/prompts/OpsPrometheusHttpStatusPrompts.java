package com.ilogic.ai.service.prompts;

import io.modelcontextprotocol.spec.McpSchema.GetPromptResult;
import io.modelcontextprotocol.spec.McpSchema.PromptMessage;
import io.modelcontextprotocol.spec.McpSchema.Role;
import io.modelcontextprotocol.spec.McpSchema.TextContent;
import java.util.List;
import org.springaicommunity.mcp.annotation.McpArg;
import org.springaicommunity.mcp.annotation.McpPrompt;
import org.springframework.stereotype.Service;

@Service
public class OpsPrometheusHttpStatusPrompts {

    @McpPrompt(
        name = "http-status-count",
        description = "Count HTTP responses for a status code (default 404) over the last X duration, per service"
    )
    public GetPromptResult httpStatusCount(
        @McpArg(name = "duration", description = "Loopback window, e.g. 1m, 5m, 15m, 1h", required = true) String duration,
        @McpArg(name = "status", description = "HTTP status code, default 404", required = false) String status,
        @McpArg(name = "service", description = "Optional job name, e.g. customer-service. Omit for all services", required = false) String service) {

        String st = (status == null || status.isBlank()) ? "404" : status;
        String svc = (service == null || service.isBlank()) ? "all services" : service;

        String jobFilter = svc.equals("all services")
            ? " (all jobs)"
            : " and job = " + svc;

        String text = """
            Use the queryPrometheusMetrics tool to find how many HTTP %1$s responses
            occurred in the last %2$s for %3$s.
            
            Metric: http_server_requests_seconds_count (Micrometer counter; labels
            include job, status, uri, method, outcome).
            
            Tool constraints:
            1. Do NOT use curly braces or label matchers in any query. The tool rejects
               them. Instead, group with "by" and read the rows you need from the result.
            2. Run these queries, in order, exactly as written:
               a) sum(increase(http_server_requests_seconds_count[%2$s])) by (job, status)
               b) sum(increase(http_server_requests_seconds_count[%2$s])) by (job, uri, status)
               c) sum(rate(http_server_requests_seconds_count[%2$s])) by (job, status) * 60
            3. From each result, keep only rows where status = %1$s%4$s.
            
            Window rules:
            - If the window is shorter than 4x the scrape interval, the result may be
              empty or unreliable. Say so, and rerun with 5m as a cross-check.
            - increase() extrapolates and can return fractions. Round to the nearest
              whole number in the final tables and state that it is an estimate.
              Show the raw unrounded value in the per-query sections.
            
            Empty or error handling:
            - An empty result means no matching series, not zero. Do not report it as 0.
              Run query (a) again and check that the metric exists at all.
            - A real zero is a row that is present with value 0.
            - If the tool returns an error, show the exact error text and stop.
              Never guess a number.
            
            Report format (follow this order):
            1. "Queries executed and results": one block per query, in order (a, b, c).
               Each block has:
               - the exact PromQL string in a code block
               - below it, a small table of the filtered rows (job, [uri], status,
                 raw value), or the line "No matching row (empty result)" if none
            2. "Final report":
               - Table 1: service (job) | count in last %2$s (estimate) | rate per minute
               - Table 2: uri | count in last %2$s (estimate) for the endpoints that
                 account for the count
            3. Interpretation, one or two lines:
               - ongoing (rate above 0) vs burst that has ended (count above 0, rate 0)
               - real routes (resource not found) vs catch-all/UNKNOWN (unmapped URLs)
            4. Window and caveats: state the window used, that counts are estimates,
               and note any small mismatch between queries caused by run-time offsets.
            """.formatted(st, duration, svc, jobFilter);

        return new GetPromptResult(
            "HTTP status count",
            List.of(new PromptMessage(Role.USER, new TextContent(text))));
    }

}
