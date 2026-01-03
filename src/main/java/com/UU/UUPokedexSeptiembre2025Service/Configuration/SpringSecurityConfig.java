//package com.UU.UUPokedexSeptiembre2025Service.Configuration;
//
//import com.UU.UUPokedexSeptiembre2025Service.Service.JwtService;
//import com.UU.UUPokedexSeptiembre2025Service.Service.JwtTokenUsoService;
//import com.UU.UUPokedexSeptiembre2025Service.Service.TokenBlackListService;
//import com.UU.UUPokedexSeptiembre2025Service.Service.UserDetailsJPAService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.ProviderManager;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SpringSecurityConfig {
//
//    private final UserDetailsJPAService userDetailsJPAService;
//    private final JwtService jwtService;
//    private final TokenBlackListService tokenBlackListService;
//    private final JwtTokenUsoService jwtTokenUsoService;
//
//    public SpringSecurityConfig(
//            UserDetailsJPAService userDetailsJPAService,
//            JwtService jwtService,
//            TokenBlackListService tokenBlackListService,
//            JwtTokenUsoService jwtTokenUsoService) {
//
//        this.userDetailsJPAService = userDetailsJPAService;
//        this.jwtService = jwtService;
//        this.tokenBlackListService = tokenBlackListService;
//        this.jwtTokenUsoService = jwtTokenUsoService;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//
////        http.csrf(csrf -> csrf.disable())
////                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
////                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests(auth -> auth
////                .requestMatchers("/api/Login").permitAll()
////                .requestMatchers("/api/Logout").authenticated()
////                )
////                .userDetailsService(userDetailsJPAService)
////                .addFilterBefore(jwtAuthFilter(), UsernamePasswordAuthenticationFilter.class);
//        return http.build();
//    }
//    
//    @Bean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }
//    
//    @Bean
//    public AuthenticationManager authenticationManager() {
////        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
////        provider.setUserDetailsService(userDetailsJPAService);
////        provider.setPasswordEncoder(passwordEncoder());
////        return new ProviderManager(provider);
////    }
//
//    return;
//}
