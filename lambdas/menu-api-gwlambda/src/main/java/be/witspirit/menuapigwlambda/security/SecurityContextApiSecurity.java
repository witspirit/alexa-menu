package be.witspirit.menuapigwlambda.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextApiSecurity implements ApiSecurity {

    @Override
    public String getAmazonUserId() {
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
