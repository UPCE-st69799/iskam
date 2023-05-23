package upce.cz.iskam.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
       auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.authorizeHttpRequests().antMatchers("/login").permitAll();
        http.authorizeHttpRequests().antMatchers(GET,"/appCategory").permitAll();
        http.authorizeHttpRequests().antMatchers(POST,"/appCategory").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(GET,"/ingredients").permitAll();
        http.authorizeHttpRequests().antMatchers(POST,"/ingredients").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(GET,"/appFood").permitAll();
        http.authorizeHttpRequests().antMatchers(POST,"/appFood/query?**").permitAll();
        http.authorizeHttpRequests().antMatchers(POST,"/appFood/query").permitAll();
        http.authorizeHttpRequests().antMatchers(POST,"/appFood/query**").permitAll();
        http.authorizeHttpRequests().antMatchers(GET,"/api/user/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().antMatchers(POST,"/api/user/save/**").hasAnyAuthority("ROLE_ADMIN");
        http.authorizeHttpRequests().anyRequest().permitAll();
        //http.authorizeHttpRequests().anyRequest().authenticated();
        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
