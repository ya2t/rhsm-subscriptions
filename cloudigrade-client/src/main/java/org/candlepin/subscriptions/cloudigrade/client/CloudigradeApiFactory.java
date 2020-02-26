/*
 * Copyright (c) 2009 - 2019 Red Hat, Inc.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Red Hat trademarks are not licensed under GPLv3. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.subscriptions.cloudigrade.client;

import org.candlepin.subscriptions.cloudigrade.ApiClient;
import org.candlepin.subscriptions.cloudigrade.JSON;
import org.candlepin.subscriptions.cloudigrade.api.resources.ConcurrentApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import java.util.Base64;

public class CloudigradeApiFactory implements FactoryBean<ConcurrentApi> {

    private static Logger log = LoggerFactory.getLogger(CloudigradeApiFactory.class);

    private final CloudigradeApiProperties properties;

    public CloudigradeApiFactory(CloudigradeApiProperties properties) {
        this.properties = properties;
    }

    @Override
    public ConcurrentApi getObject() throws Exception {
        if (properties.isUseStub()) {
            log.info("Using stub pinhead client");
            return new StubConcurrentApi();
        }

        ApiClient client;
        if (properties.usesClientAuth()) {
            log.info("Cloudigrade client configured with client-cert auth");
            client = new X509ApiClientFactory(properties.getX509ApiClientFactoryConfiguration()).getObject();
        }
        else {
            log.info("Cloudigrade client configured without client-cert auth");
            client = new ApiClient();
        }
        if (properties.getUrl() != null) {
            log.info("Cloudigrade URL: {}", properties.getUrl());
            client.setBasePath(properties.getUrl());
        }
        else {
            log.warn("Cloudigrade URL not set...");
        }

        // NOTE: The identity header must be set before making the appropriate API calls.
        client.addDefaultHeader("X-4Scale-Env", properties.getFourScaleEnv());
        client.setUsername("insights-qa");
        client.setPassword("redhat");
        return new ConcurrentApi(client);
    }

    public ConcurrentApi getCustom(String account) {
        if (properties.isUseStub()) {
            log.info("Using stub pinhead client");
            return new StubConcurrentApi();
        }

        ApiClient client = new ApiClient();
        if (properties.getUrl() != null) {
            log.info("Cloudigrade URL: {}", properties.getUrl());
            client.setBasePath(properties.getUrl());
        }
        else {
            log.warn("Cloudigrade URL not set...");
        }

        client.addDefaultHeader("x-rh-identity", buildBase64EncodedIdentityHeader(account));
        client.addDefaultHeader("X-4Scale-Env", properties.getFourScaleEnv());

        return new ConcurrentApi(client);
    }

    private String buildBase64EncodedIdentityHeader(String account) {
        String identityHeader = String.format("{\"identity\": { \"account_number\": \"{}\"}}", account);
        return Base64.getEncoder().withoutPadding().encodeToString(identityHeader.getBytes());
    }

    @Override
    public Class<?> getObjectType() {
        return ConcurrentApi.class;
    }

}
