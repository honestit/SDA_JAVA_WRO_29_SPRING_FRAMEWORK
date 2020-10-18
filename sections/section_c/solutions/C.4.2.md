Rozbudowanie konfiguracji `SecurityConfig` zgodnie z wytycznymi z zadania:

```java
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").anonymous()
                .antMatchers("/logout").authenticated()
                .antMatchers({"/user", "/user/**"}).hasRole("USER")
                .antMatchers({"/admin", "/admin/**"}).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .and()
            .logout()
                .logoutSuccessUrl("/")
                .and()
            .csrf().disable();
    }
```