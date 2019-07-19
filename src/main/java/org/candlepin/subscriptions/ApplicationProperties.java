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
package org.candlepin.subscriptions;

import org.candlepin.subscriptions.retention.TallyRetentionPolicyProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * POJO to hold property values via Spring's "Type-Safe Configuration Properties" pattern
 */
@Component
@ConfigurationProperties(prefix = "rhsm-subscriptions")
public class ApplicationProperties {

    private boolean prettyPrintJson = false;

    private final TallyRetentionPolicyProperties tallyRetentionPolicy = new TallyRetentionPolicyProperties();

    /**
     * Resource location a file containing a list of RHEL product IDs.
     */
    private String rhelProductListResourceLocation;

    /**
     * Resource location of a file containing a list of accounts to process.
     */
    private String accountListResourceLocation;

    /**
     * An hour based threshold used to determine whether an inventory host record's rhsm facts are outdated.
     * The host's rhsm.SYNC_TIMESTAMP fact is checked against this threshold. The default is 24 hours.
     */
    private int hostLastSyncThresholdHours = 24;

    /**
     * The batch size of account numbers that will be processed at a time while producing snapshots.
     * Default: 500
     */
    private int accountBatchSize = 500;

    public boolean isPrettyPrintJson() {
        return prettyPrintJson;
    }

    public void setPrettyPrintJson(boolean prettyPrintJson) {
        this.prettyPrintJson = prettyPrintJson;
    }

    public String getRhelProductListResourceLocation() {
        return rhelProductListResourceLocation;
    }

    public void setRhelProductListResourceLocation(String rhelProductListResourceLocation) {
        this.rhelProductListResourceLocation = rhelProductListResourceLocation;
    }

    public TallyRetentionPolicyProperties getTallyRetentionPolicy() {
        return tallyRetentionPolicy;
    }

    public String getAccountListResourceLocation() {
        return accountListResourceLocation;
    }

    public void setAccountListResourceLocation(String accountListResourceLocation) {
        this.accountListResourceLocation = accountListResourceLocation;
    }

    public int getHostLastSyncThresholdHours() {
        return hostLastSyncThresholdHours;
    }

    public void setHostLastSyncThresholdHours(int hostLastSyncThresholdHours) {
        this.hostLastSyncThresholdHours = hostLastSyncThresholdHours;
    }

    public int getAccountBatchSize() {
        return this.accountBatchSize;
    }

    public void setAccountBatchSize(int accountBatchSize) {
        this.accountBatchSize = accountBatchSize;
    }

}