package hu.psprog.leaflet.jwt.auth.support.frontend.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for JWT authentication front-end support module.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.jwt.auth.support.filter",
        "hu.psprog.leaflet.jwt.auth.support.logout"})
public class JWTAuthenticationFrontEndSupportConfiguration {
}
