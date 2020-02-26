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
package org.candlepin.subscriptions.cloudigrade;

import org.candlepin.subscriptions.cloudigrade.api.model.ConcurrencyReport;
import org.candlepin.subscriptions.cloudigrade.api.resources.ConcurrentApi;
import org.candlepin.subscriptions.cloudigrade.client.CloudigradeApiFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class CloudigradeService {

    private CloudigradeApiFactory apiFactory;

    @Autowired
    public CloudigradeService(CloudigradeApiFactory apiFactory) {
        this.apiFactory = apiFactory;
    }

    public ConcurrencyReport getReport(String account) {
        try {
//            return apiFactory.getCustom(accountNumber).listDailyConcurrentUsages(null, null);

            ConcurrentApi api = apiFactory.getObject();
            api.getApiClient().addDefaultHeader("x-rh-identity", buildBase64EncodedIdentityHeader(account));
            // TODO Support paging.
            return api.listDailyConcurrentUsages(null, null);

        } catch (ApiException e) {
            throw new RuntimeException("Could not get cloudigrade data.", e);
        } catch (Exception e) {
            throw new RuntimeException("Could not create the API client.", e);
        }
    }

    private String buildBase64EncodedIdentityHeader(String account) {
        String identityHeader = String.format("{\"identity\": { \"account_number\": \"%s\"}}", account);
        return Base64.getEncoder().withoutPadding().encodeToString(identityHeader.getBytes());
    }
}
