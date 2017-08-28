package be.witspirit.menu.api.menuapi.security;

import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.common.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class AmazonLoginAuthProvider implements AuthenticationProvider {
    private static final Logger LOG = LoggerFactory.getLogger(AmazonLoginAuthProvider.class);

    private final ProfileService profileService;

    public AmazonLoginAuthProvider(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        TokenAuthenticationToken tokenAuthentication = (TokenAuthenticationToken) authentication;
        String token = tokenAuthentication.getToken();

        try {
            AmazonProfile amazonProfile = profileService.getProfile(token);
            return new AmazonAuthentication(token, amazonProfile);
        } catch (InvalidTokenException e) {
            LOG.info("Failed to authenticate "+ token +" : "+e.getMessage());
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
