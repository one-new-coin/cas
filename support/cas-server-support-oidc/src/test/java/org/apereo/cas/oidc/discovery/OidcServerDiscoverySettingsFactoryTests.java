package org.apereo.cas.oidc.discovery;

import org.apereo.cas.oidc.AbstractOidcTests;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This is {@link OidcServerDiscoverySettingsFactoryTests}.
 *
 * @author Misagh Moayyed
 * @since 5.3.0
 */
@Tag("OIDC")
public class OidcServerDiscoverySettingsFactoryTests extends AbstractOidcTests {

    @Test
    public void verifyAction() {
        assertFalse(oidcServerDiscoverySettings.getClaimsSupported().isEmpty());
        assertFalse(oidcServerDiscoverySettings.getClaimTypesSupported().isEmpty());
        assertFalse(oidcServerDiscoverySettings.getGrantTypesSupported().isEmpty());
        assertFalse(oidcServerDiscoverySettings.getIntrospectionSupportedAuthenticationMethods().isEmpty());
        assertFalse(oidcServerDiscoverySettings.getIdTokenSigningAlgValuesSupported().isEmpty());
        assertFalse(oidcServerDiscoverySettings.getSubjectTypesSupported().isEmpty());

        assertFalse(oidcServerDiscoverySettings.getResponseTypesSupported().isEmpty());
        assertFalse(oidcServerDiscoverySettings.getScopesSupported().isEmpty());

        assertNotNull(oidcServerDiscoverySettings.getEndSessionEndpoint());
        assertNotNull(oidcServerDiscoverySettings.getIntrospectionEndpoint());
        assertNotNull(oidcServerDiscoverySettings.getRegistrationEndpoint());
        assertNotNull(oidcServerDiscoverySettings.getTokenEndpoint());
        assertNotNull(oidcServerDiscoverySettings.getUserinfoEndpoint());
        assertNotNull(oidcServerDiscoverySettings.getIssuer());
        assertNotNull(oidcServerDiscoverySettings.getJwksUri());
        assertNotNull(oidcServerDiscoverySettings.getServerPrefix());
    }
}
