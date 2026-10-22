package com.ilogic.ai.dto;

public record AuditSummary(String namespace,
                           int errorCount,
                           String lastIncident) {}
