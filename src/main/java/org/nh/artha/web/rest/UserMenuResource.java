package org.nh.artha.web.rest;

import org.nh.artha.config.ApplicationProperties;
import org.nh.artha.security.UserPreferencesUtils;
import org.nh.artha.service.FeatureService;
import org.nh.artha.service.RoleService;
import org.nh.artha.web.rest.errors.InternalServerErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@RestController
@RequestMapping("/api")
public class UserMenuResource {

    private final Logger log = LoggerFactory.getLogger(UserMenuResource.class);

    private final RoleService roleService;

    private final FeatureService featureService;

    private final ApplicationProperties applicationProperties;

    public UserMenuResource(RoleService roleService, FeatureService featureService, ApplicationProperties applicationProperties) {
        this.roleService = roleService;
        this.featureService = featureService;
        this.applicationProperties = applicationProperties;
    }

    /**
     * GET  /user-menus : get all the user menus for organization.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of userOrganizationRoleMappings in body
     */
    @GetMapping("/user-menus")
    public List<Map<String, Object>> getAllUserMenus() {
        log.debug("REST request to get all menus for a User");
        if (UserPreferencesUtils.getCurrentUserPreferences() == null) {
            throw new InternalServerErrorException("User does not have preferences");
        }
        if (UserPreferencesUtils.getCurrentUserPreferences().getOrganization() == null) {
            throw new InternalServerErrorException("User does not have Unit mapped");
        }
        List<Map<String, Object>> menus = new ArrayList<>();
        List moduleFeatures = roleService.getAllMenusForUserInAnOrganization(
            UserPreferencesUtils.getCurrentUserPreferences().getUser().getId(),
            UserPreferencesUtils.getCurrentUserPreferences().getOrganization().getId());
        for (int i = 0; i < moduleFeatures.size(); i++) {
            Object[] row = (Object[]) moduleFeatures.get(i);
            if ("all".equalsIgnoreCase(row[0].toString())) {//SUPER_USER
                List featureMenus = featureService.findFeatureMenusForAllModule();
                menus.clear();
                populateMenuForList(menus, featureMenus);
                break;
            } else if ("all".equalsIgnoreCase(row[2].toString())) {//MODULE_ADMIN
                removeModuleMenuWhenFound(menus, row[0].toString());
                List featureMenus = featureService.findFeatureMenusForModule(Long.valueOf(row[5].toString()));
                populateMenuForList(menus, featureMenus);
            } else {//OTHER_CASES
                populateMenuForRow(menus, row);
            }
        }
        return menus;
    }

    private void removeModuleMenuWhenFound(List<Map<String, Object>> menus, String moduleName) {
        for (int m = 0; m < menus.size(); m++) {
            if (moduleName.equals(menus.get(m).get("title"))) {
                menus.remove(m);
                break;
            }
        }
    }

    private List<Map<String, Object>> populateMenuForList(List<Map<String, Object>> menus, List featureMenus) {
        for (int i = 0; i < featureMenus.size(); i++) {
            Object[] row = (Object[]) featureMenus.get(i);
            if ("all".equalsIgnoreCase(row[2].toString())) {
                continue;
            }
            populateMenuForRow(menus, row);
        }
        return menus;
    }

    private void populateMenuForRow(List<Map<String, Object>> menus, Object[] row) {
        if ("Internal".equals(row[1].toString()) || "Dashboard".equals(row[1].toString()) || (row[6] == null) || row[6].toString().trim().length() == 0) {
            return;
        }
        Map<String, Object> moduleMap = getModuleMap(menus, row[0].toString());
        Map<String, Object> featureTypeMap = getFeatureTypeMap(moduleMap, row[1].toString());
        Map<String, Object> featureMap = new LinkedHashMap<>();
        featureMap.put("title", row[2].toString());
        featureMap.put("src", row[6].toString());
        featureMap.put("url", "/"+row[6].toString());
        featureMap.put("type", row[1].toString());
        ((List) featureTypeMap.get("childs")).add(featureMap);
    }

    private Map<String, Object> getFeatureTypeMap(Map<String, Object> moduleMap, String featureType) {
        List children = (List) moduleMap.get("childs");
        Iterator<Object> iterator = children.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> child = (Map<String, Object>) iterator.next();
            if (featureType.equals(child.get("title"))) {
                return child;
            }
        }
        Map<String, Object> menu = new LinkedHashMap<>();
        menu.put("title", featureType);
        menu.put("src", featureType.toLowerCase().replaceAll(" ", "-"));
        menu.put("type", featureType);
        menu.put("childs", new ArrayList<>());
        int order = 4;
        if ("Master".equals(featureType)) {
            order = 0;
        } else if ("Configuration".equals(featureType)) {
            order = 1;
        } else if ("Transaction".equals(featureType)) {
            order = 2;
        } else if ("Report".equals(featureType)) {
            order = 3;
        }
        children.add(order < children.size() ? order : children.size(), menu);
        return menu;
    }

    private Map<String, Object> getModuleMap(List<Map<String, Object>> menus, String moduleName) {
        for (int i = 0; i < menus.size(); i++) {
            if (moduleName.equals(menus.get(i).get("title"))) {
                return menus.get(i);
            }
        }
        Map<String, Object> menu = new LinkedHashMap<>();
        menu.put("title", moduleName);
        menu.put("src", moduleName.toLowerCase().replaceAll(" ", "-"));
        menu.put("childs", new ArrayList<>());
        menus.add(menu);
        return menu;
    }

    private String getUrlForFeature(String moduleName) {
        return "http://localhost:8097/#/";
    }

}
