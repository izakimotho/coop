package kimotho.coop.config;



import kimotho.coop.service.Impl.JwtUserDetailsService;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class JwtSecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserDetailsService jwtUserDetailsService;

    public JwtSecurityConfig(final JwtRequestFilter jwtRequestFilter,
            final PasswordEncoder passwordEncoder,
            final JwtUserDetailsService jwtUserDetailsService) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.passwordEncoder = passwordEncoder;
        this.jwtUserDetailsService = jwtUserDetailsService;
    }

    @Bean
    public AuthenticationManager jwtAuthenticationManager() {
        final DaoAuthenticationProvider jwtAuthenticationManager = new DaoAuthenticationProvider(passwordEncoder);
        jwtAuthenticationManager.setUserDetailsService(jwtUserDetailsService);
        return new ProviderManager(jwtAuthenticationManager);
    }

    @Bean
    public SecurityFilterChain jwtFilterChain(final HttpSecurity http) throws Exception {
        return http.cors(withDefaults())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .authenticationManager(jwtAuthenticationManager())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public QueueConfiguration queueConfiguration() {
        return new QueueConfiguration();
    }
}

class QueueConfiguration {
    @Bean
    public Queue customerQueue() {
        return new Queue("customer_queue");
    }
}
