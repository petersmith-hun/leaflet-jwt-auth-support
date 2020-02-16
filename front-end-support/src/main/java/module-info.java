open module leaflet.component.security.jwt.support.frontend {
    requires leaflet.component.bridge.api;
    requires leaflet.component.security.jwt.support.api;

    requires java.annotation;
    requires java.compiler;
    requires org.slf4j;
    requires spring.beans;
    requires spring.boot;
    requires spring.context;
    requires spring.core;
    requires spring.security.core;
    requires spring.security.web;
    requires spring.web;
    requires org.apache.tomcat.embed.core;

    exports hu.psprog.leaflet.jwt.auth.support.filter;
    exports hu.psprog.leaflet.jwt.auth.support.filter.device;
    exports hu.psprog.leaflet.jwt.auth.support.logout;
}