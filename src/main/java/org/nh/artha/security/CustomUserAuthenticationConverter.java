package org.nh.artha.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.Collections;
import java.util.Map;

public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {
    private Logger logger = LoggerFactory.getLogger(CustomUserAuthenticationConverter.class);

    @Override
    public Authentication extractAuthentication(Map map) {
       logger.debug("CustomUserAuthenticationConverter :: extractAuthentication {} ",map);
        if(map.containsKey("sub")) {
            map.put("user_name", map.get("sub"));
        }
        return super.extractAuthentication(map);
    }


}
