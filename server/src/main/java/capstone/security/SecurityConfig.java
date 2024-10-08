package capstone.security;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConverter converter;

    public SecurityConfig(JwtConverter converter) {
        this.converter = converter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();

        http.cors();

        http.authorizeRequests()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/create_account").permitAll()
                .antMatchers(HttpMethod.GET, "/api/campaign","/api/campaign/*","/api/campaign/filtered",
                        "/api/session/use/*","/api/session/camp/*", "/api/userSchedule/*","/api/user/id/*",
                        "/api/userSchedule/id/*",
                        "/").permitAll()
                .antMatchers(HttpMethod.POST, "/api/campaign", "/api/session", "/api/userSchedule",
                        "/api/campaign/user", "/api/session/user").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/campaign/*", "/api/session/*",
                        "/api/userSchedule/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/campaign/*", "/api/session/*",
                        "/api/userSchedule/*", "/api/campaign/user/*", "/api/session/user/*", "/api/su/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/user/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/user/*").hasAnyRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtRequestFilter(authenticationManager(), converter))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    @Bean
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedHeaders("Authorization", "Content-Type")
                        .allowedOrigins("*")
                        .allowedMethods("*");
            }
        };
    }
}
