package com.ilogic.ai.config;

import com.ilogic.ai.tools.OpsToolService;
import com.ilogic.ai.tools.OpsToolLiveService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class McpServerConfig {

    @Bean
    public ToolCallbackProvider opsTools(OpsToolService opsToolService, OpsToolLiveService opsToolLiveService) {
        // Automatically scans the service bean for @Tool annotations and registers them
        return MethodToolCallbackProvider.builder()
            .toolObjects(opsToolService, opsToolLiveService)
            .build();
    }

}
