package org.nh.artha.config;

import io.github.jhipster.config.JHipsterProperties;
import org.nh.artha.security.AuthoritiesConstants;
import org.nh.artha.security.CustomUserAuthenticationConverter;
import org.nh.artha.security.jwt.JWTConfigurer;
import org.nh.artha.security.jwt.TokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
import org.springframework.web.filter.CorsFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

import javax.crypto.SecretKey;
import java.util.Collection;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final SecurityProblemSupport problemSupport;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserDetailsService userDetailsService;
    private final ApplicationProperties applicationProperties;
    private final JHipsterProperties jHipsterProperties;


    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, TokenProvider tokenProvider,
                                 CorsFilter corsFilter, SecurityProblemSupport problemSupport,
                                 UserDetailsService userDetailsService,
                                 ApplicationProperties applicationProperties,JHipsterProperties jHipsterProperties) {
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.problemSupport = problemSupport;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.applicationProperties = applicationProperties;
        this.jHipsterProperties=jHipsterProperties;

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    @Order //default lower
    public void configureGlobalDb() throws Exception {
        if (!applicationProperties.getSecurity().getAuthentication().getDb().isConfigured()) {
            return;
        }
        authenticationManagerBuilder
            .userDetailsService(userDetailsService)
            .passwordEncoder(passwordEncoder());
    }

    @Autowired
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public void configureGlobalLdap() throws Exception {
        if (!applicationProperties.getSecurity().getAuthentication().getLdap().isConfigured()) {
            return;
        }
        ActiveDirectoryLdapAuthenticationProvider adAuthenticationProvider = new ActiveDirectoryLdapAuthenticationProvider(
            applicationProperties.getSecurity().getAuthentication().getLdap().getDomain(),
            applicationProperties.getSecurity().getAuthentication().getLdap().getUrl());
        adAuthenticationProvider.setUserDetailsContextMapper(new UserDetailsContextMapper() {

            @Override
            public UserDetails mapUserFromContext(DirContextOperations dirContextOperations, String username, Collection<? extends GrantedAuthority> collection) {
                return userDetailsService.loadUserByUsername(username);
            }

            @Override
            public void mapUserToContext(UserDetails userDetails, DirContextAdapter dirContextAdapter) {
                throw new UnsupportedOperationException("LdapUserDetailsMapper only supports reading from a context. Please use a subclass if mapUserToContext() is required.");
            }
        });
        adAuthenticationProvider.setAuthoritiesMapper(new GrantedAuthoritiesMapper() {
            @Override
            public Collection<? extends GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> collection) {
                return collection;
            }
        });
        authenticationManagerBuilder.authenticationProvider(adAuthenticationProvider);

    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
            .antMatchers(HttpMethod.OPTIONS, "/**")
            .antMatchers("/app/**/*.{js,html}")
            .antMatchers("/i18n/**")
            .antMatchers("/content/**")
            .antMatchers("/swagger-ui/index.html")
            .antMatchers("/test/**");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .csrf()
            .disable()
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling()
            .authenticationEntryPoint(problemSupport)
            .accessDeniedHandler(problemSupport)
            .and()
            .headers()
            .contentSecurityPolicy("default-src 'self'; frame-src 'self' data:; script-src 'self' 'unsafe-inline' 'unsafe-eval' https://storage.googleapis.com; style-src 'self' 'unsafe-inline'; img-src 'self' data:; font-src 'self' data:")
            .and()
            .referrerPolicy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
            .and()
            .featurePolicy("geolocation 'none'; midi 'none'; sync-xhr 'none'; microphone 'none'; camera 'none'; magnetometer 'none'; gyroscope 'none'; speaker 'none'; fullscreen 'self'; payment 'none'")
            .and()
            .frameOptions()
            .deny()
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers("/api/authenticate").permitAll()
            .antMatchers("/api/register").permitAll()
            .antMatchers("/api/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/info").permitAll()
            .antMatchers("/management/prometheus").permitAll()
            .antMatchers("/management/**").hasAuthority(AuthoritiesConstants.ADMIN)
            .and()
            .httpBasic()
            .and()
            .apply(securityConfigurerAdapter());
        // @formatter:on
    }

    private JWTConfigurer securityConfigurerAdapter() throws Exception {
        return new JWTConfigurer(tokenProvider,createTokenStore(tokenProvider));
    }

    @Bean
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        TokenStore tokenStore =new JwtTokenStore(jwtAccessTokenConverter);;
        return tokenStore;
    }


    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(SignatureVerifier signatureVerifier) throws Exception {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setVerifier(signatureVerifier(tokenProvider));
        CustomUserAuthenticationConverter customUserAuthenticationConverter = new CustomUserAuthenticationConverter();
        customUserAuthenticationConverter.setUserDetailsService(userDetailsService);
        ((DefaultAccessTokenConverter)converter.getAccessTokenConverter()).setUserTokenConverter(customUserAuthenticationConverter);
        return converter;
    }

    @Bean
    public SignatureVerifier signatureVerifier(TokenProvider tokenProvider) throws Exception {
        try {
            return new MacSigner(tokenProvider.getKey().getAlgorithm(),(SecretKey) tokenProvider.getKey());
        } catch (IllegalStateException ex) {
            return null;
        }
    }

    private TokenStore createTokenStore(TokenProvider tokenProvider) throws Exception {
        return tokenStore(jwtAccessTokenConverter(new MacSigner((SecretKey) tokenProvider.getKey())));

    }
}
