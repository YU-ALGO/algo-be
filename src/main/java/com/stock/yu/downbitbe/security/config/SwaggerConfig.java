package com.stock.yu.downbitbe.security.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "ALGo REST API",
                description = "ALGo REST API Description",
                version = "v0.0.1"))
@Configuration
@RequiredArgsConstructor
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi customTestOpenAPi() {
        // /test 로 시작하는 API 들을 테스트 관련 API 로 그룹핑
        // member 로 시작하는 API 를 그룹핑 하고 싶다 라고 하면 메소드 이름을 변경하고 하나 더 만들어서 설정하면 됨

        String[] paths = {"/api/v1/**"};

        return GroupedOpenApi
                .builder()
                .group("테스트 관련 API")
                .pathsToMatch(paths)
                .addOpenApiCustomiser(buildSecurityOpenApi()).build();
    }

    public OpenApiCustomiser buildSecurityOpenApi() {
        // jwt token 을 한번 설정하면 header 에 값을 넣어주는 코드, 자세한건 아래에 추가적으로 설명할 예정
        return OpenApi -> OpenApi.addSecurityItem(new SecurityRequirement().addList("jwt token"))
                .getComponents().addSecuritySchemes("jwt token", new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT")
                        .scheme("bearer"));
    }

}