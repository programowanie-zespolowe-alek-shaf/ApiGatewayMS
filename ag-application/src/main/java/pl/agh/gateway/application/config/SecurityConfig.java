package pl.agh.gateway.application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.MapSessionRepository;
import org.springframework.session.SessionRepository;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import javax.sql.DataSource;
import java.util.concurrent.ConcurrentHashMap;


@Configuration
@EnableWebSecurity
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DataSource dataSource;
    @Value("${server.servlet.session.timeout}")
    private Integer maxInactiveIntervalInSeconds;

    @Autowired
    public SecurityConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from customer.user where username = ?")
                .authoritiesByUsernameQuery("select username, role from customer.user_roles where username = ?");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().csrfTokenRepository(new HttpSessionCsrfTokenRepository())
                .and().authorizeRequests().anyRequest().authenticated().and().httpBasic()
                .and().logout().logoutUrl("/api/logout"); //POST method
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setCookieName("JSESSIONID");
        serializer.setCookiePath("/");
        serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
        return serializer;
    }

    @Bean
    public SessionRepository sessionRepository() {
        SessionRepository sessionRepository = new MapSessionRepository(new ConcurrentHashMap<>());
        ((MapSessionRepository) sessionRepository)
                .setDefaultMaxInactiveInterval(maxInactiveIntervalInSeconds);
        return sessionRepository;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**") //disabled for CORS
                .and().ignoring().antMatchers(HttpMethod.POST, "/api/customers-ms/users"); //disabled for register method
    }

    //todo fix problem with circular reference
    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .exposedHeaders("X-CSRF-Token", "Content-Type")
                        .allowedMethods("HEAD", "GET", "PUT", "POST", "DELETE", "PATCH")
                        .allowedOrigins("*");
            }
        };
    }*/
}