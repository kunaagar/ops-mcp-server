package com.ilogic.ai.dto;

public record ServerHealth(String nodeName,
                           String status,
                           String cpuUsage,
                           String memoryUsage) {

}
