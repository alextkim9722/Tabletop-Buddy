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
                .antMatchers(HttpMethod.GET, "/api/campaign","/api/campaign/*","/api/campaign/filtered").permitAll()
                .antMatchers(HttpMethod.POST, "/api/campaign").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/campaign/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/campaign/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/session/use/*","/api/session/camp/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/session").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/session/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/session/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.GET, "/api/userSchedule/*").permitAll()
                .antMatchers(HttpMethod.POST, "/api/userSchedule").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.PUT, "/api/userSchedule/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/userSchedule/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/campaign/user").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/campaign/user/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.POST, "/api/session/user").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/session/user/*").hasAnyRole("USER", "ADMIN")
                .antMatchers(HttpMethod.DELETE, "/api/user/*").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/api/*").permitAll()
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
                        .allowedOrigins("http://localhost:3000")
                        .allowedMethods("*");
            }
        };
    }
}
