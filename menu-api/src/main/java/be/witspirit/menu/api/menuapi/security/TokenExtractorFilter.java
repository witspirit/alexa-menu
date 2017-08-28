package be.witspirit.menu.api.menuapi.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenExtractorFilter extends OncePerRequestFilter {
    public static final String BEARER = "bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = normalizeToken(request.getHeader("Authorization"));

        if (token != null) {
            SecurityContextHolder.getContext().setAuthentication(new TokenAuthenticationToken(token));
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Strip any Bearer prefixes to ensure we have a common parameters
     *
     * Depending on the client, the authorization header will contain the OAuth typical Bearer prefix.
     * We just need the token portion.
     *
     * @param rawToken The token as received from the Authorization header
     * @return The token without any identifying prefixes
     */
    private String normalizeToken(String rawToken) {
        if (rawToken == null) {
            return null;
        }
        if (rawToken.length() > BEARER.length()) {
            String prefixCandidate = rawToken.substring(0, BEARER.length());
            prefixCandidate = prefixCandidate.toLowerCase();
            if (prefixCandidate.equals(BEARER)) {
                return rawToken.substring((BEARER+" ").length());
            }
        }
        return rawToken;
    }
}
