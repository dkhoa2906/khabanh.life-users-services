//package life.khabanh.usersservices.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfigurerAdapter {
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                // Allow access to the '/new-user' endpoint without authentication
//                .antMatchers("/new-user").permitAll()  // No authentication required
//                .anyRequest().authenticated()  // All other requests require authentication
//                .and()
//                .csrf().disable();  // Disable CSRF for testing or API calls (optional)
//    }
//}
//
