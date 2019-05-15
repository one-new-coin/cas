package org.apereo.cas.ticket.refreshtoken;

import org.apereo.cas.authentication.Authentication;
import org.apereo.cas.authentication.principal.Service;
import org.apereo.cas.configuration.support.Beans;
import org.apereo.cas.services.ServicesManager;
import org.apereo.cas.support.oauth.services.OAuthRegisteredService;
import org.apereo.cas.ticket.ExpirationPolicy;
import org.apereo.cas.ticket.Ticket;
import org.apereo.cas.ticket.TicketFactory;
import org.apereo.cas.ticket.TicketGrantingTicket;
import org.apereo.cas.ticket.UniqueTicketIdGenerator;
import org.apereo.cas.util.DefaultUniqueTicketIdGenerator;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Default OAuth refresh token factory.
 *
 * @author Jerome Leleu
 * @since 5.0.0
 */
@RequiredArgsConstructor
public class DefaultRefreshTokenFactory implements RefreshTokenFactory {

    /**
     * Default instance for the ticket id generator.
     */
    protected final UniqueTicketIdGenerator refreshTokenIdGenerator;

    /**
     * ExpirationPolicy for refresh tokens.
     */
    protected final ExpirationPolicy expirationPolicy;

    /**
     * Services manager.
     */
    protected final ServicesManager servicesManager;

    public DefaultRefreshTokenFactory(final ExpirationPolicy expirationPolicy, final ServicesManager servicesManager) {
        this(new DefaultUniqueTicketIdGenerator(), expirationPolicy, servicesManager);
    }

    @Override
    public RefreshToken create(final Service service, final Authentication authentication,
                               final TicketGrantingTicket ticketGrantingTicket,
                               final Collection<String> scopes,
                               final String clientId,
                               final Map<String, Map<String, Object>> requestClaims) {
        val codeId = this.refreshTokenIdGenerator.getNewTicketId(RefreshToken.PREFIX);
        val expirationPolicyToUse = determineExpirationPolicyForService(service);
        val rt = new RefreshTokenImpl(codeId, service, authentication,
            expirationPolicyToUse, ticketGrantingTicket,
            scopes, clientId, requestClaims);

        if (ticketGrantingTicket != null) {
            ticketGrantingTicket.getDescendantTickets().add(rt.getId());
        }
        return rt;
    }

    @Override
    public TicketFactory get(final Class<? extends Ticket> clazz) {
        return this;
    }

    private ExpirationPolicy determineExpirationPolicyForService(final Service service) {
        val registeredService = (OAuthRegisteredService) servicesManager.findServiceBy(service);
        if (registeredService != null && registeredService.getRefreshTokenExpirationPolicy() != null) {
            val policy = registeredService.getRefreshTokenExpirationPolicy();
            val timeToKill = policy.getTimeToKill();
            if (StringUtils.isNotBlank(timeToKill)) {
                return new OAuthRefreshTokenExpirationPolicy(Beans.newDuration(timeToKill).getSeconds());
            }
        }
        return this.expirationPolicy;
    }
}
