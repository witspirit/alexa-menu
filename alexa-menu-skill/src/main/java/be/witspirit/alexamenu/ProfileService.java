package be.witspirit.alexamenu;

/**
 * Small interface to abstract our Profile Service implementation
 */
public interface ProfileService {

    AmazonProfile getProfile(String accessToken);
}
