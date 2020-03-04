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
package org.candlepin.subscriptions.tally;

import org.candlepin.subscriptions.cloudigrade.CloudigradeService;
import org.candlepin.subscriptions.cloudigrade.api.model.ConcurrencyReport;
import org.candlepin.subscriptions.cloudigrade.api.model.ConcurrentUsage;
import org.candlepin.subscriptions.db.model.HardwareMeasurementType;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Collects usage measurements from cloudigrade.
 */
@Component
public class CloudigradeAccountUsageCollector {

    private final CloudigradeService service;

    public CloudigradeAccountUsageCollector(CloudigradeService service) {
        this.service = service;
    }

    public Map<String, AccountUsageCalculation> collect(Map<String, AccountUsageCalculation> existingCalcs,
        Collection<String> accounts) {

        // Get all cloudigrade measurements
        for (String account : accounts) {
            ConcurrencyReport report = service.getReport(account);
            if (report.getData().isEmpty()) {
                // Nothing to add
                continue;
            }

            // We are only pulling data back for a single day (today)
            ConcurrentUsage usage = report.getData().get(0);
            if (usage.getInstances() == 0) {
                // Nothing to add
                continue;
            }

            // Ensure that there's an account calculation for this account. If a host
            // wasn't present in HBI it may have not been added.
            existingCalcs.putIfAbsent(account, new AccountUsageCalculation(account));

            AccountUsageCalculation calc = existingCalcs.get(account);
            // Cloudigrade only tracks RHEL at the moment.
            ProductUsageCalculation rhelCalc = calc.containsProductCalculation("RHEL") ?
                                                   calc.getProductCalculation("RHEL") : new ProductUsageCalculation("RHEL");

            // Cores are not applicable for aws cloud providers so we set it to 0.
            rhelCalc.addCloudProvider(HardwareMeasurementType.AWS_CLOUDIGRADE,
                0, usage.getInstances(), usage.getInstances());
            calc.addProductCalculation(rhelCalc);
        }
        return existingCalcs;
    }
}
