open module leaflet.component.security.jwt.support.api {
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires leaflet.component.bridge.api;
    requires leaflet.component.rest.backend.api;
    requires org.apache.commons.lang3;
    requires spring.security.core;

    exports hu.psprog.leaflet.jwt.auth.support.domain;
    exports hu.psprog.leaflet.jwt.auth.support.exception;
    exports hu.psprog.leaflet.jwt.auth.support.service;
}