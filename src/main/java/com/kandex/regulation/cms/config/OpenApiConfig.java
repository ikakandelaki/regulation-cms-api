package com.kandex.regulation.cms.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
//@SecurityScheme(name = "Authorization",
//        type = SecuritySchemeType.OAUTH2,
//        flows = @OAuthFlows(authorizationCode = @OAuthFlow(
//                authorizationUrl = "${spring.security.oauth2.authorizationserver.endpoint.authorization-uri}",
//                tokenUrl = "${spring.security.oauth2.authorizationserver.endpoint.token-uri}",
//                scopes = {
//                        @OAuthScope(name = "openid", description = "openid scope")
//                })))
@OpenAPIDefinition(servers = {@Server(url = "/")},
        info = @Info(title = "Regulation Cms Service APIs",
                version = "v1.0"))
public class OpenApiConfig {

}