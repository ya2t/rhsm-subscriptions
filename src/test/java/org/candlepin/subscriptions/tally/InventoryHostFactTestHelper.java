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

import org.candlepin.subscriptions.db.model.ServiceLevel;
import org.candlepin.subscriptions.inventory.db.model.InventoryHostFacts;

import org.springframework.util.StringUtils;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Provides helper functions for test cases.
 */
public class InventoryHostFactTestHelper {

    private InventoryHostFactTestHelper() {
        throw new IllegalStateException("Utility class; should never be instantiated!");
    }

    public static ClassifiedInventoryHostFacts createHypervisor(String account, String orgId, Integer product,
        int cores, int sockets) {
        InventoryHostFacts baseFacts = createBaseHost(account, orgId);
        baseFacts.setProducts(product.toString());
        baseFacts.setCores(cores);
        baseFacts.setSockets(sockets);

        ClassifiedInventoryHostFacts facts = new ClassifiedInventoryHostFacts(baseFacts);
        facts.setHypervisor(true);
        return facts;
    }

    public static ClassifiedInventoryHostFacts createGuest(String hypervisorUUID, String account,
        String orgId, Integer product, int cores, int sockets) {
        InventoryHostFacts baseFacts = createBaseHost(account, orgId);
        baseFacts.setProducts(product.toString());
        baseFacts.setCores(cores);
        baseFacts.setSockets(sockets);
        baseFacts.setHypervisorUuid(hypervisorUUID);
        baseFacts.setGuestId(UUID.randomUUID().toString());
        baseFacts.setVirtual(true);

        ClassifiedInventoryHostFacts facts = new ClassifiedInventoryHostFacts(baseFacts);
        facts.setHypervisorUnknown(hypervisorUUID == null || hypervisorUUID.isEmpty());
        return facts;
    }

    public static ClassifiedInventoryHostFacts createRhsmHost(String account, String orgId,
        List<Integer> products, Integer cores, Integer sockets, String syspurposeRole,
        OffsetDateTime syncTimestamp) {
        return createRhsmHost(account, orgId, StringUtils.collectionToCommaDelimitedString(products),
            cores, sockets, syspurposeRole, syncTimestamp);
    }

    public static ClassifiedInventoryHostFacts createRhsmHost(List<Integer> products, Integer cores,
        Integer sockets, String syspurposeRole, OffsetDateTime syncTimestamp) {
        return createRhsmHost("Account", "test_org", StringUtils.collectionToCommaDelimitedString(products),
            cores, sockets, syspurposeRole, syncTimestamp);
    }

    public static ClassifiedInventoryHostFacts createRhsmHost(String account, String orgId, String products,
        Integer cores, Integer sockets, String syspurposeRole, OffsetDateTime syncTimeStamp) {
        return createRhsmHost(account, orgId, products, ServiceLevel.UNSPECIFIED, cores, sockets,
            syspurposeRole, syncTimeStamp);
    }

    public static ClassifiedInventoryHostFacts createRhsmHost(String account, String orgId, String products,
        ServiceLevel sla, Integer cores, Integer sockets, String syspurposeRole,
        OffsetDateTime syncTimeStamp) {
        InventoryHostFacts baseFacts = createBaseHost(account, orgId);
        baseFacts.setProducts(products);
        baseFacts.setCores(cores);
        baseFacts.setSockets(sockets);
        baseFacts.setSyspurposeRole(syspurposeRole);
        baseFacts.setSyspurposeSla(sla.getValue());
        baseFacts.setSyncTimestamp(syncTimeStamp.toString());
        return new ClassifiedInventoryHostFacts(baseFacts);
    }

    public static ClassifiedInventoryHostFacts createQpcHost(String qpcProducts,
        OffsetDateTime syncTimestamp) {
        InventoryHostFacts baseFacts = createBaseHost("Account", "test_org");
        baseFacts.setQpcProducts(qpcProducts);
        baseFacts.setSyncTimestamp(syncTimestamp.toString());
        return new ClassifiedInventoryHostFacts(baseFacts);
    }

    public static ClassifiedInventoryHostFacts createSystemProfileHost(List<Integer> products,
        Integer coresPerSocket, Integer sockets, OffsetDateTime syncTimestamp) {
        return createSystemProfileHost("Account", "test-org", products, coresPerSocket, sockets,
            syncTimestamp);
    }

    public static ClassifiedInventoryHostFacts createSystemProfileHost(String account, String orgId,
        List<Integer> productIds, Integer coresPerSocket, Integer sockets, OffsetDateTime syncTimestamp) {
        InventoryHostFacts baseFacts = createBaseHost(account, orgId);
        baseFacts.setSystemProfileProductIds(StringUtils.collectionToCommaDelimitedString(productIds));
        baseFacts.setSystemProfileCoresPerSocket(coresPerSocket);
        baseFacts.setSystemProfileSockets(sockets);
        baseFacts.setSyncTimestamp(syncTimestamp.toString());
        return new ClassifiedInventoryHostFacts(baseFacts);
    }

    public static InventoryHostFacts createBaseHost(String account, String orgId) {
        InventoryHostFacts baseFacts = new InventoryHostFacts();
        baseFacts.setAccount(account);
        baseFacts.setDisplayName("Test System");
        baseFacts.setOrgId(orgId);
        baseFacts.setSyncTimestamp(OffsetDateTime.now().toString());
        baseFacts.setSubscriptionManagerId(UUID.randomUUID().toString());
        baseFacts.setVirtual(false);
        return baseFacts;
    }
}