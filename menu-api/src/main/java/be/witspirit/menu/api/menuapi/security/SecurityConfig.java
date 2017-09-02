package be.witspirit.menu.api.menuapi.security;

import be.witspirit.amazonlogin.AmazonProfileService;
import be.witspirit.amazonlogin.ProfileService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder authManBuilder) {
        authManBuilder.authenticationProvider(amazonLoginAuthProvider());
    }

    @Bean
    public ProfileService profileService() {
        return new AmazonProfileService();
    }

    @Bean
    public AmazonLoginAuthProvider amazonLoginAuthProvider() {
        return new AmazonLoginAuthProvider(profileService());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(new TokenExtractorFilter(), BasicAuthenticationFilter.class);
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll() // Ensure we support CORS Pre-flight inquiries without Authentication !
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable(); // Not required when working stateless... Strange that this is not implied in the above Stateless
    }
}
