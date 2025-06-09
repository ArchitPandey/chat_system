package com.homemade.chat_service.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Configuration
@EnableWebFluxSecurity
@Slf4j
public class WebSocketSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        /**
         * you can also do http.authenticationManager and then set the basicAuthManager there
         * this will mean that entire app will use basicAuthReactiveManager for all auth
         * however if you set the authManager inside httpBasic that means that this auth
         * manager will be used only when basic auth cred are passed
         * this allows to us to have a second auth mechanism using -
         * http
         *   .httpBasic(httpBasicSpec ->
         *       httpBasicSpec.authenticationManager(passwordOnlyManager)
         *   )
         *   .oauth2ResourceServer(oauth2 ->
         *       oauth2.jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(myConverter))
         *   );
         */
        return http
                .authorizeExchange(customizer -> customizer.pathMatchers("/chat-ws")
                            .authenticated()
                            .anyExchange()
                            .permitAll()
                )
                .httpBasic(customizer -> customizer
                        .authenticationManager(basicAuthReactiveAuthManager())
                )
                .csrf(customizer -> customizer.disable())
                .build();
    }

    ReactiveAuthenticationManager basicAuthReactiveAuthManager() {
        return authentication -> Mono.just(new UsernamePasswordAuthenticationToken(
                authentication.getName(),
                authentication.getCredentials().toString(),
                Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
        ));
    }

}
