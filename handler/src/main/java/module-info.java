open module leaflet.component.security.jwt.support.handler {
    requires leaflet.component.bridge.api;
    requires leaflet.component.rest.backend.api;
    requires leaflet.component.rest.backend.client;
    requires leaflet.component.security.jwt.support.api;

    requires com.fasterxml.jackson.databind;
    requires slf4j.api;
    requires spring.beans;
    requires spring.context;
    requires spring.security.core;
}