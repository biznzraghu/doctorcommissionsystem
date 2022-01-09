package org.nh.artha.job;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.domain.UserMaster;
import org.nh.artha.repository.AuthorityRepository;
import org.nh.artha.repository.UserMasterRepository;
import org.nh.artha.security.AuthoritiesConstants;
import org.nh.artha.service.UserMasterService;
import org.nh.artha.service.UserService;
import org.nh.artha.service.dto.UserDTO;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.HashSet;
import java.util.Set;

public class UserMasterToUaaUserSyncJob implements Job {

    private final Logger log = LoggerFactory.getLogger(UserMasterToUaaUserSyncJob.class);
    @Autowired
    private UserMasterService userMasterService;

    @Autowired
    private UserMasterRepository userMasterRepository;

    @Autowired
    private AuthorityRepository authorityRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private UserService userService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.debug("Synced Information for User: {}");
        Set<String> authoritiesSet = new HashSet<>();
        String authorities = authorityRepository.findById(AuthoritiesConstants.USER).orElse(null).getName();
        authoritiesSet.add(authorities);
        fetchUserMasterDataAndSyncToUaa(authoritiesSet);
    }

    public void fetchUserMasterDataAndSyncToUaa(Set<String> authoritiesSet) {
        long resultCount = userMasterRepository.getTotalRecordCount();
        log.debug("Request to sync data from userMaster to UAA {} ",resultCount);
        int pageSize = applicationProperties.getConfigs().getIndexPageSize();
        int lastPageNumber = (int) (Math.ceil(resultCount / pageSize));
        for (int i = 0; i <= lastPageNumber; i++) {
            Page<UserMaster> all = userMasterRepository.findAll(PageRequest.of(i, pageSize));
            log.debug("Request to sync data from userMaster to UAA page Number {} page size{} ",i,pageSize);
            all.getContent().parallelStream().forEach(userMaster -> {
                String employeeNumber = userMaster.getEmployeeNumber();
                if (!employeeNumber.equalsIgnoreCase("admin")) {
                    Long userMasterId = userMaster.getId();
                    String firstName = userMaster.getFirstName();
                    String lastName = userMaster.getLastName();
                    UserDTO userDTO = new UserDTO();
                    userDTO.setLogin(employeeNumber);
                    userDTO.setFirstName(firstName);
                    userDTO.setLastName(lastName);
                    userDTO.setActivated(true);
                    userDTO.setId(userMasterId);
                    userDTO.setAuthorities(authoritiesSet);
                    userService.createUser(userDTO, true);
                }
            });
        }
    }
}
