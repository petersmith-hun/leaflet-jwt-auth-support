package hu.psprog.leaflet.jwt.auth.support.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import hu.psprog.leaflet.jwt.auth.support.domain.deserializer.JWTExpirationDateDeserializer;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * JWT token payload wrapper model.
 *
 * @author Peter Smith
 */
@JsonDeserialize(builder = AuthenticationUserDetailsModel.AuthenticationUserDetailsModelBuilder.class)
public final class AuthenticationUserDetailsModel {

    private String name;
    private String role;
    private Long id;
    private Date expiration;

    private AuthenticationUserDetailsModel() {
        // prevent direct initialization
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public Date getExpiration() {
        return expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        AuthenticationUserDetailsModel that = (AuthenticationUserDetailsModel) o;

        return new EqualsBuilder()
                .append(name, that.name)
                .append(role, that.role)
                .append(id, that.id)
                .append(expiration, that.expiration)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(name)
                .append(role)
                .append(id)
                .append(expiration)
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .append("role", role)
                .append("id", id)
                .append("expiration", expiration)
                .toString();
    }

    public static AuthenticationUserDetailsModelBuilder getBuilder() {
        return new AuthenticationUserDetailsModelBuilder();
    }

    /**
     * Builder for {@link AuthenticationUserDetailsModel}.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static final class AuthenticationUserDetailsModelBuilder {
        private String name;
        private String role;
        private Long id;
        private Date expiration;

        private AuthenticationUserDetailsModelBuilder() {
        }

        @JsonProperty("name")
        public AuthenticationUserDetailsModelBuilder withName(String name) {
            this.name = name;
            return this;
        }

        @JsonProperty("rol")
        public AuthenticationUserDetailsModelBuilder withRole(String role) {
            this.role = role;
            return this;
        }

        @JsonProperty("uid")
        public AuthenticationUserDetailsModelBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        @JsonProperty("exp")
        @JsonDeserialize(using = JWTExpirationDateDeserializer.class)
        public AuthenticationUserDetailsModelBuilder withExpiration(Date expiration) {
            this.expiration = expiration;
            return this;
        }

        public AuthenticationUserDetailsModel build() {
            AuthenticationUserDetailsModel authenticationUserDetailsModel = new AuthenticationUserDetailsModel();
            authenticationUserDetailsModel.role = this.role;
            authenticationUserDetailsModel.expiration = this.expiration;
            authenticationUserDetailsModel.name = this.name;
            authenticationUserDetailsModel.id = this.id;
            return authenticationUserDetailsModel;
        }
    }
}
