/*
 *  Copyright (c) 2024 Bayerische Motoren Werke Aktiengesellschaft (BMW AG)
 *
 *  This program and the accompanying materials are made available under the
 *  terms of the Apache License, Version 2.0 which is available at
 *  https://www.apache.org/licenses/LICENSE-2.0
 *
 *  SPDX-License-Identifier: Apache-2.0
 *
 *  Contributors:
 *       Bayerische Motoren Werke Aktiengesellschaft (BMW AG) - initial API and implementation
 *
 */

package org.eclipse.edc.connector.controlplane.services.protocol;

import org.eclipse.edc.policy.engine.spi.PolicyEngine;
import org.eclipse.edc.policy.model.Policy;
import org.eclipse.edc.spi.agent.ParticipantAgent;
import org.eclipse.edc.spi.agent.ParticipantAgentService;
import org.eclipse.edc.spi.iam.ClaimToken;
import org.eclipse.edc.spi.iam.IdentityService;
import org.eclipse.edc.spi.iam.RequestContext;
import org.eclipse.edc.spi.iam.TokenRepresentation;
import org.eclipse.edc.spi.result.Result;
import org.eclipse.edc.spi.result.ServiceFailure;
import org.eclipse.edc.spi.types.domain.message.RemoteMessage;
import org.junit.jupiter.api.Test;

import static java.util.Collections.emptyMap;
import static org.eclipse.edc.junit.assertions.AbstractResultAssert.assertThat;
import static org.eclipse.edc.spi.result.ServiceFailure.Reason.UNAUTHORIZED;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProtocolTokenValidatorImplTest {

    private final ParticipantAgentService agentService = mock();
    private final IdentityService identityService = mock();
    private final PolicyEngine policyEngine = mock();
    private final ProtocolTokenValidatorImpl validator = new ProtocolTokenValidatorImpl(identityService, policyEngine, mock(), agentService);

    @Test
    void shouldVerifyToken() {
        var participantAgent = new ParticipantAgent(emptyMap(), emptyMap());
        var claimToken = ClaimToken.Builder.newInstance().build();
        var policy = Policy.Builder.newInstance().build();
        var tokenRepresentation = TokenRepresentation.Builder.newInstance().build();
        when(identityService.verifyJwtToken(any(), any())).thenReturn(Result.success(claimToken));
        when(agentService.createFor(any())).thenReturn(participantAgent);

        var result = validator.verify(tokenRepresentation, "scope", policy, new TestMessage());

        assertThat(result).isSucceeded().isSameAs(participantAgent);
        verify(agentService).createFor(claimToken);
        verify(policyEngine).evaluate(eq("scope"), same(policy), argThat(ctx -> {
            var reqContext = ctx.getContextData(RequestContext.class);
            return reqContext.getMessage().getClass().equals(TestMessage.class) && reqContext.getDirection().equals(RequestContext.Direction.Ingress);
        }));
        verify(identityService).verifyJwtToken(same(tokenRepresentation), any());
    }

    @Test
    void shouldReturnUnauthorized_whenTokenIsNotValid() {
        when(identityService.verifyJwtToken(any(), any())).thenReturn(Result.failure("failure"));

        var result = validator.verify(TokenRepresentation.Builder.newInstance().build(), "scope", Policy.Builder.newInstance().build(), new TestMessage());

        assertThat(result).isFailed().extracting(ServiceFailure::getReason).isEqualTo(UNAUTHORIZED);
    }

    static class TestMessage implements RemoteMessage {
        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getCounterPartyAddress() {
            return "http://connector";
        }

        @Override
        public String getCounterPartyId() {
            return null;
        }
    }
}
