package com.seasongg.user.models.permissions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collection;


@Service
public class SggAuthorizationResolver {

    private final Logger LOG = LoggerFactory.getLogger(SggAuthorizationResolver.class);
    private static final String UNEXPECTED_ERROR = "Unexpected error occurred while comparing two authorities: {}, {}";

    private static final String WILDCARD = "*";

    public boolean hasAuthority(String requiredAuthorization) {

        PermissionAssertion userAuthority = null;
        try {
            PermissionAssertion permissionAssertion = new PermissionAssertion(requiredAuthorization);

            for (GrantedAuthority authority : getAuthorities()) {
                userAuthority = new PermissionAssertion(authority.toString());

                if (permissionAssertion.getResourceType().equals(userAuthority.getResourceType())) {
                    if (permissionAssertion.getAction().equals(userAuthority.getAction()) ||
                            userAuthority.getAction().equals(WILDCARD)) {

                        if (permissionAssertion.getInstanceId().equals(userAuthority.getInstanceId())) {
                            return true;
                        }
                    }
                }

            }
        } catch (Exception e) {
            LOG.error(UNEXPECTED_ERROR, requiredAuthorization, userAuthority, e);
            return false;
        }

        return false;
    }

    private Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }

    private static class PermissionAssertion {

        private final String resourceType;
        private final String action;
        private final String instanceId;

        PermissionAssertion(String permissionAssertionString) {
            String[] permissionsRequiredArray = permissionAssertionString.split(":");

            this.resourceType = permissionsRequiredArray[0];
            this.action = permissionsRequiredArray[1];
            this.instanceId = permissionsRequiredArray[2];
        }

        public String getResourceType() {
            return resourceType;
        }

        public String getAction() {
            return action;
        }

        public String getInstanceId() {
            return instanceId;
        }

        @Override
        public String toString() {
            return this.resourceType + ":" + this.action + ":" + this.instanceId;
        }

    }

}
