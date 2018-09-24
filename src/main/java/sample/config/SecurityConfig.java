/*
 * Copyright 2014-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;

/**
 * Spring Security configuration.
 *
 * @author Rob Winch
 * @author Vedran Pavic
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    private FindByIndexNameSessionRepository<? extends Session> sessionRepository;

    @Override
    public void configure(WebSecurity web)
    {
        web.ignoring()
           .antMatchers("/expired");
    }

    // @formatter:off
	// tag::config[]
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
				.anyRequest().authenticated()
				.and()
			.formLogin()
				.permitAll()
			.defaultSuccessUrl("/hello", true)
				.permitAll()
			.and()
				.csrf().disable();

		http.sessionManagement().maximumSessions(2).and().invalidSessionUrl("/expired");
	}
	// end::config[]
	// @formatter:on

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        PasswordEncoder encoder = passwordEncoder();

        auth.inMemoryAuthentication()
            .withUser("user")
            .password(encoder.encode("password"))
            .roles("USER")
            .authorities(new SimpleGrantedAuthority("SOMETHING"));
    }

    @Autowired
    public void setSessionRepository(FindByIndexNameSessionRepository<? extends Session> sessionRepository)
    {
        this.sessionRepository = sessionRepository;
    }

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry()
    {
        return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
