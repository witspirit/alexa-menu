package be.witspirit.menu.api.menuapi.security;

import be.witspirit.amazonlogin.AmazonProfile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class AmazonAuthentication implements Authentication {

    private final String token;
    private final AmazonProfile amazonProfile;

    public AmazonAuthentication(String token, AmazonProfile amazonProfile) {
        this.token = token;
        this.amazonProfile = amazonProfile;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.commaSeparatedStringToAuthorityList("AMAZON_USER");
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public AmazonProfile getDetails() {
        return amazonProfile;
    }

    @Override
    public Object getPrincipal() {
        return amazonProfile.getUserId();
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("isAuthenticated is fixed to true for this type of authentication");
    }

    @Override
    public String getName() {
        return amazonProfile.getName();
    }

    public String getToken() {
        return token;
    }

}
