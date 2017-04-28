package be.witspirit.alexamenu;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Java Representation of Amazon Profile JSON
 */
public class AmazonProfile {
    private String name;
    private String email;
    @JsonProperty("user_id")
    private String userId;

    public String getName() {
        return name;
    }

    public AmazonProfile setName(String name) {
        this.name = name;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public AmazonProfile setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public AmazonProfile setUserId(String userId) {
        this.userId = userId;
        return this;
    }
}
