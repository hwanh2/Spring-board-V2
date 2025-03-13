package study.spring_board_V2.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApis() {
        return GroupedOpenApi.builder()
                .group("all") // 전체 API 그룹
                .pathsToMatch("/**") // 모든 API 포함
                .build();
    }
}