package com.luannv.rentroom.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luannv.rentroom.exception.ErrorCode;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
  private final UserDetailsService userDetailsService;
  private final String[] PUBLIC_ENDPOINTS = {
      "/login/**",
      "/register/**",
      "/introspect/**",
      "/logout/**"
  };
  private final String[] ADMIN_ENDPOINTS = {
      "/api/roles/**"
  };
  private final String[] MANAGER_ENDPOINTS = {
      "/api/users/**"
  };
  private final String[] SWAGGER_WHITELIST = {
      "/v3/api-docs/**",
      "/swagger-ui/**",
      "/swagger-ui.html"
  };
  @Value("${security.jwt.signerKey}")
  private String KEY_SIGNER;

  @Autowired
  public SecurityConfig(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .cors(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        // .authorizeHttpRequests(auth -> auth
        // .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
        // .requestMatchers(SWAGGER_WHITELIST).permitAll() // dev
        // .requestMatchers(ADMIN_ENDPOINTS).hasAnyRole(RoleEnums.ADMIN.name())
        // .requestMatchers(HttpMethod.POST,
        // MANAGER_ENDPOINTS)
        // .hasAnyRole(RoleEnums.ADMIN.name(),
        // RoleEnums.MODERATOR.name())
        // .anyRequest().authenticated())
        .authorizeHttpRequests(auth -> auth
            .anyRequest().permitAll())
        .formLogin(login -> login.disable())
        .logout(logout -> logout.disable())
        .userDetailsService(userDetailsService);
    // http.httpBasic(Customizer.withDefaults());
    http.exceptionHandling(e -> e
        .authenticationEntryPoint((request, response, authException) -> {
          response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
          response.setContentType("application/json");

          Map<String, Object> errorResponse = errorResponseTemplate(
              ErrorCode.UNAUTHENTICATED.getCode(),
              ErrorCode.UNAUTHENTICATED.getMessages());

          String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
          response.getWriter().write(jsonResponse);
        })
        .accessDeniedHandler((request, response, exception) -> {
          response.setStatus(HttpServletResponse.SC_FORBIDDEN);
          response.setContentType("application/json");

          Map<String, Object> errorResponse = errorResponseTemplate(
              ErrorCode.ACCESS_DENIED.getCode(),
              ErrorCode.ACCESS_DENIED.getMessages());

          String jsonResponse = new ObjectMapper().writeValueAsString(errorResponse);
          response.getWriter().write(jsonResponse);
        }));

    http.oauth2ResourceServer(auth -> auth
        .jwt(jwtConfigurer -> jwtConfigurer
            .decoder(jwtDecoder())
            .jwtAuthenticationConverter(jwtAuthenticationConverter())));
    return http.build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    SecretKeySpec secretKeySpec = new SecretKeySpec(KEY_SIGNER.getBytes(), "HS512");
    return NimbusJwtDecoder
        .withSecretKey(secretKeySpec)
        .macAlgorithm(MacAlgorithm.HS512)
        .build();
  }

  @Bean
  public JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
    converter.setJwtGrantedAuthoritiesConverter(source -> {
      List<String> roles = source.getClaimAsStringList("roles");
      if (roles == null)
        return List.of();
      return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
          .collect(Collectors.toList());
    });
    return converter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  private static Map<String, Object> errorResponseTemplate(Integer errorCode, String message) {
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.putIfAbsent("error", errorCode);
    errorResponse.putIfAbsent("message", message);
    errorResponse.putIfAbsent("timestamp", System.currentTimeMillis());
    return errorResponse;
  }
  // Hard code user data
  // @Bean
  // public UserDetailsService userDetailsService() {
  // UserDetails admin = User
  // .builder()
  // .username("admin")
  // .password(passwordEncoder().encode("admin"))
  // .roles("ADMIN")
  // .build();
  // UserDetails moderator = User
  // .builder()
  // .username("moderator")
  // .password(passwordEncoder().encode("moderator"))
  // .roles("MODERATOR")
  // .build();
  // UserDetails user = User
  // .builder()
  // .username("user")
  // .password(passwordEncoder().encode("user"))
  // .roles("USER")
  // .build();
  // return new InMemoryUserDetailsManager(admin, moderator, user);
  // }
}
