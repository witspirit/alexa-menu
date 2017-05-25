/*
* Copyright 2015-2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
*
* Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at
*
*     http://aws.amazon.com/apache2.0/
*
* or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
*/
package be.witspirit.authorizer;

import be.witspirit.amazonlogin.AmazonProfile;
import be.witspirit.amazonlogin.AmazonProfileService;
import be.witspirit.amazonlogin.InvalidTokenException;
import be.witspirit.amazonlogin.ProfileService;
import be.witspirit.authorizer.io.AuthPolicy;
import be.witspirit.authorizer.io.TokenAuthorizerContext;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles IO for a Java Lambda function as a custom authorizer for API Gateway
 *
 * @author Jack Kohn
 *
 */
public class ApiGwAuthorizerHandler implements RequestHandler<TokenAuthorizerContext, AuthPolicy> {
    private static final Logger LOG = LoggerFactory.getLogger(ApiGwAuthorizerHandler.class);

    private final ProfileService profileService;

    public ApiGwAuthorizerHandler() {
        this(new AmazonProfileService());
    }

    public ApiGwAuthorizerHandler(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public AuthPolicy handleRequest(TokenAuthorizerContext input, Context context) {

        String token = input.getAuthorizationToken();
        LOG.debug("Incoming token : {}", token);

        // validate the incoming token
        // and produce the principal user identifier associated with the token

        // this could be accomplished in a number of ways:
        // 1. Call out to OAuth provider
        // 2. Decode a JWT token in-line
        // 3. Lookup in a self-managed DB

        // This implementation goes to AmazonLogin
        AmazonProfile profile;
        try {
            profile = profileService.getProfile(token);
            LOG.debug("Obtained profile : {}", profile);
        } catch (InvalidTokenException e) {
            throw new RuntimeException("Unauthorized"); // Standardized 401 response
        }

        String principalId = profile.getUserId();

        // if the client token is not recognized or invalid
        // you can send a 401 Unauthorized response to the client by failing like so:
        // throw new RuntimeException("Unauthorized");

        // if the token is valid, a policy should be generated which will allow or deny access to the client

        // if access is denied, the client will receive a 403 Access Denied response
        // if access is allowed, API Gateway will proceed with the back-end integration configured on the method that was called

        String methodArn = input.getMethodArn();
        String[] arnPartials = methodArn.split(":");
        String region = arnPartials[3];
        String awsAccountId = arnPartials[4];
        String[] apiGatewayArnPartials = arnPartials[5].split("/");
        String restApiId = apiGatewayArnPartials[0];
        String stage = apiGatewayArnPartials[1];
        String httpMethod = apiGatewayArnPartials[2];
        String resource = ""; // root resource
        if (apiGatewayArnPartials.length == 4) {
            resource = apiGatewayArnPartials[3];
        }

        // this function must generate a policy that is associated with the recognized principal user identifier.
        // depending on your use case, you might store policies in a DB, or generate them on the fly

        // keep in mind, the policy is cached for 5 minutes by default (TTL is configurable in the authorizer)
        // and will apply to subsequent calls to any method/resource in the RestApi
        // made with the same token

        // For now resolve authorization via the Lambda Environment
        if (authorized(profile)) {
            LOG.debug("{} is authorized", profile.getEmail());
            AuthPolicy policy = new AuthPolicy(principalId, AuthPolicy.PolicyDocument.getAllowAllPolicy(region, awsAccountId, restApiId, stage));
            policy.getContext().put("name", profile.getName());
            policy.getContext().put("email", profile.getEmail());
            return policy;
        }
        LOG.debug("{} is NOT authorized", profile.getEmail());

        // the example policy below denies access to all resources in the RestApi
        return new AuthPolicy(principalId, AuthPolicy.PolicyDocument.getDenyAllPolicy(region, awsAccountId, restApiId, stage));
    }

    private boolean authorized(AmazonProfile profile) {
        // A bit naive, but will do for now, until I implement a proper system.
        String authorizedEmails = System.getProperty("authorizedEmails");
        if (authorizedEmails == null) {
            authorizedEmails = System.getenv("authorizedEmails"); // I expected these to be available above as well... But doesn't seem to be the case.
        }
        LOG.debug("authorizedEmails : {}", authorizedEmails);
        if (authorizedEmails == null) {
            authorizedEmails = "";
        }
        return authorizedEmails.contains(profile.getEmail());
    }

}