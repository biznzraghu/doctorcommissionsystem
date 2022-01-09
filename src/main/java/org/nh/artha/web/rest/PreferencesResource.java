package org.nh.artha.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.nh.artha.domain.Preferences;
import org.nh.artha.repository.PreferencesRepository;
import org.nh.artha.service.UserMasterService;
import org.nh.artha.service.UserOrganizationDepartmentMappingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PreferencesResource {

    private final UserMasterService userMasterService;

    private final PreferencesRepository preferencesRepository;

    private final ObjectMapper objectMapper;

    private final UserOrganizationDepartmentMappingService userOrganizationDepartmentMappingService;

    public PreferencesResource( UserMasterService userMasterService,ObjectMapper objectMapper,
                                PreferencesRepository preferencesRepository,UserOrganizationDepartmentMappingService userOrganizationDepartmentMappingService){
        this.userMasterService=userMasterService;
        this.objectMapper = objectMapper;
        this.preferencesRepository=preferencesRepository;
        this.userOrganizationDepartmentMappingService=userOrganizationDepartmentMappingService;
    }

    @GetMapping("/change-preference/{id}")
    public Preferences changePreference(@PathVariable Long id){
        Preferences preferences = userOrganizationDepartmentMappingService.searchUserBasedOnOrganization(id);
        return preferences;
    }

    @GetMapping("/preference")
    public Preferences getCurrentUserPreference(@RequestParam("userId") Long userId){
        Preferences preferences = preferencesRepository.findOneByUser(userId);
        return preferences;
    }

}
