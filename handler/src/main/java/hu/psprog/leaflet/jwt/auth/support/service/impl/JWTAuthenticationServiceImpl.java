package hu.psprog.leaflet.jwt.auth.support.service.impl;

import hu.psprog.leaflet.api.rest.request.user.LoginRequestModel;
import hu.psprog.leaflet.api.rest.request.user.PasswordResetDemandRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.user.LoginResponseDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;
import hu.psprog.leaflet.bridge.service.UserBridgeService;
import hu.psprog.leaflet.jwt.auth.support.exception.TokenAuthenticationFailureException;
import hu.psprog.leaflet.jwt.auth.support.service.AuthenticationService;
import hu.psprog.leaflet.jwt.auth.support.service.impl.utility.AuthenticationUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Peter Smith
 */
@Service
public class JWTAuthenticationServiceImpl implements AuthenticationService {

    private static final String INVALID_USER_CREDENTIALS = "Invalid user credentials.";

    private UserBridgeService userBridgeService;
    private AuthenticationUtility authenticationUtility;

    @Autowired
    public JWTAuthenticationServiceImpl(UserBridgeService userBridgeService, AuthenticationUtility authenticationUtility) {
        this.userBridgeService = userBridgeService;
        this.authenticationUtility = authenticationUtility;
    }

    @Override
    public void demandPasswordReset(PasswordResetDemandRequestModel passwordResetDemandRequestModel) throws CommunicationFailureException {
        userBridgeService.demandPasswordReset(passwordResetDemandRequestModel);
    }

    @Override
    public void confirmPasswordReset(UserPasswordRequestModel userPasswordRequestModel, String token) throws CommunicationFailureException {
        authenticationUtility.createAndStoreTemporal(token);
        userBridgeService.confirmPasswordReset(userPasswordRequestModel);
    }

    @Override
    public void renewToken(Authentication authentication) throws CommunicationFailureException {
        authenticationUtility.replace(authentication.getPrincipal().toString(), userBridgeService.renewToken().getToken());
    }

    @Override
    public void revokeToken() throws CommunicationFailureException {
        userBridgeService.revokeToken();
    }

    @Override
    public Authentication claimToken(Authentication authentication) throws CommunicationFailureException {

        LoginResponseDataModel loginResponseModel = userBridgeService.claimToken(convertToLoginRequest(authentication));

        if (Objects.isNull(loginResponseModel) || loginResponseModel.getStatus() != LoginResponseDataModel.AuthenticationResult.AUTH_SUCCESS) {
            throw new TokenAuthenticationFailureException(INVALID_USER_CREDENTIALS);
        }

        return authenticationUtility.create(authentication, loginResponseModel);
    }

    private LoginRequestModel convertToLoginRequest(Authentication authentication) {

        LoginRequestModel loginRequestModel = new LoginRequestModel();
        loginRequestModel.setEmail(authentication.getPrincipal().toString());
        loginRequestModel.setPassword(authentication.getCredentials().toString());

        return loginRequestModel;
    }
}
