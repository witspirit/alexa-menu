package be.witspirit.menu.api.menuapi.test;

import be.witspirit.amazonlogin.AmazonProfile;

public class MockCaller {
    private String key;

    public MockCaller(String key) {
        this.key = key;
    }

    public String token() {
        return key+"Token";
    }

    public String name() {
        return key+"Name";
    }

    public String email() {
        return key+"_email@test.example.com";
    }

    public String userId() {
        return key+"UserId";
    }

    public AmazonProfile profile() {
        return new AmazonProfile().setName(name()).setEmail(email()).setUserId(userId());
    }




}
