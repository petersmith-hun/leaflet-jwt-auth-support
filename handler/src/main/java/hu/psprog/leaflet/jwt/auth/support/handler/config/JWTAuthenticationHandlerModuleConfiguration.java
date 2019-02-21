package hu.psprog.leaflet.jwt.auth.support.handler.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for JWT authentication handler module.
 *
 * @author Peter Smith
 */
@Configuration
@ComponentScan(basePackages = {
        "hu.psprog.leaflet.jwt.auth.support.bridge",
        "hu.psprog.leaflet.jwt.auth.support.provider",
        "hu.psprog.leaflet.jwt.auth.support.service.impl"})
public class JWTAuthenticationHandlerModuleConfiguration {
}
