package org.nh.artha.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.nh.artha.domain.*;
import org.nh.artha.repository.*;
import org.nh.artha.service.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    private final PreferencesRepository preferencesRepository;

    private final UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository;

    private final ObjectMapper objectMapper;

    private final RoleService roleService;


    private final UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository;

    public DomainUserDetailsService(UserRepository userRepository, PreferencesRepository preferencesRepository,UserOrganizationRoleMappingRepository userOrganizationRoleMappingRepository,
                                    ObjectMapper objectMapper,RoleService roleService,
                                    UserOrganizationDepartmentMappingRepository userOrganizationDepartmentMappingRepository) {
        this.userRepository = userRepository;
        this.preferencesRepository=preferencesRepository;
        this.objectMapper=objectMapper;
        this.roleService=roleService;
        this.userOrganizationRoleMappingRepository = userOrganizationRoleMappingRepository;
        this.userOrganizationDepartmentMappingRepository=userOrganizationDepartmentMappingRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        AuthenticatedUser authenticatedUser = userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> {
                Preferences preferences = preferencesRepository.findOneByUser(user.getId());
                if (preferences == null || preferences.getId()==null) {
                    preferences = new Preferences();
                    preferences.setUser(user);
                }
                List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                    .collect(Collectors.toList());
                if (preferences != null) {
                    loadUserPreferences(user, grantedAuthorities, preferences);

                    loadUserPreferredOrganizations(lowercaseLogin, preferences);
                }
                if(preferences.getId()==null) {
                    preferences = preferencesRepository.save(preferences);
                }
                return  new AuthenticatedUser(lowercaseLogin,
                    user.getPassword(), true, true, true, true,
                    grantedAuthorities).preference(
                    objectMapper.convertValue(preferences, org.nh.artha.security.dto.Preferences.class));
                })
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
        return authenticatedUser;

    }

    private void loadUserPreferences(User user, List<GrantedAuthority> grantedAuthorities, Preferences preferences) {
        if (preferences.getOrganization() != null) {
            Set<Privilege> privileges = new HashSet<Privilege>();
            List<Role> roles = userOrganizationRoleMappingRepository.findAllRolesByHospitalAndUserId(preferences.getOrganization().getId(), user.getId());
            roles.forEach(role -> {
                privileges.addAll(roleService.getAllPrivilegesForRoleHierarchy(role.getId()));
            });
            privileges.spliterator().forEachRemaining(privilege -> {
                if (privilege.isActive()) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(privilege.getCode()));
                }
            });
        }
    }



    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getLogin(),
            user.getPassword(),
            grantedAuthorities);
    }

    private void loadUserPreferredOrganizations(String lowercaseLogin, Preferences preferences) {
        List<UserOrganizationDepartmentMapping> userOrganizations = userOrganizationDepartmentMappingRepository.findByPreferredAndUsername(lowercaseLogin);
        for (UserOrganizationDepartmentMapping userOrganizationMapping : userOrganizations) {
            if(preferences!=null && preferences.getOrganization()==null && userOrganizationMapping.getOrganization()!=null && userOrganizationMapping.getOrganization().getCode()!=null){
                preferences.setOrganization(userOrganizationMapping.getOrganization());
                break;
            }
        }
    }
}
