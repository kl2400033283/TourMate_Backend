package com.klu.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.*;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ MUST
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").permitAll()
                .requestMatchers("/api/bookings/**").permitAll()
                .requestMatchers("/api/guide/**").permitAll()
                .requestMatchers("/api/host/**").permitAll()
                .requestMatchers("/api/chat/**").permitAll()
                .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .authorizationEndpoint(a -> a.baseUri("/oauth2/authorization"))
                .redirectionEndpoint(r -> r.baseUri("/login/oauth2/code/*"))
                .successHandler((request, response, authentication) -> {
                    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
                    String email = oauthUser.getAttribute("email");
                    String name = oauthUser.getAttribute("name");
                    String token = jwtUtil.generateToken(email);
                    String redirectUrl = "https://tour-mate-frontend.vercel.app/oauth2/callback"
                            + "?token=" + URLEncoder.encode(token, StandardCharsets.UTF_8)
                            + "&email=" + URLEncoder.encode(email, StandardCharsets.UTF_8)
                            + "&name=" + URLEncoder.encode(name, StandardCharsets.UTF_8);
                    response.sendRedirect(redirectUrl);
                })
            )
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    //  ROBUST CORS CONFIG
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        
        //  Using allowedOriginPatterns is more robust for modern Vite/React setups
        config.setAllowedOriginPatterns(Arrays.asList(
        		 "http://localhost:5173",
        		 "http://localhost:5174",
        		 "http://127.0.0.1:5173",
        	        "https://tour-mate-frontend.vercel.app"
        )); 
        
        //  Explicitly defining headers and methods prevents pre-flight (OPTIONS) blocks
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}