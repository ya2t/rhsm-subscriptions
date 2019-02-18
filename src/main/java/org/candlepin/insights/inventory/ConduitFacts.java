/*
 * Copyright (c) 2009 - 2019 Red Hat, Inc.
 *
 * This software is licensed to you under the GNU General Public License,
 * version 2 (GPLv2). There is NO WARRANTY for this software, express or
 * implied, including the implied warranties of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. You should have received a copy of GPLv2
 * along with this software; if not, see
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.txt.
 *
 * Red Hat trademarks are not licensed under GPLv2. No permission is
 * granted to use or replicate Red Hat trademarks that are incorporated
 * in this software or its documentation.
 */
package org.candlepin.insights.inventory;

import java.util.List;
import java.util.UUID;

/**
 * POJO that holds all facts scoped for collection by the conduit.
 */
public class ConduitFacts {
    private UUID subscriptionManagerId;
    private UUID biosUuid;
    private List<String> ipAddresses;
    private String fqdn;
    private List<String> macAddresses;
    private Integer cpuSockets;
    private Integer cpuCores;
    private Integer memory;
    private String architecture;
    private Boolean isVirtual;
    private String vmHost;
    private List<String> rhProd;

    public UUID getSubscriptionManagerId() {
        return subscriptionManagerId;
    }

    public void setSubscriptionManagerId(UUID subscriptionManagerId) {
        this.subscriptionManagerId = subscriptionManagerId;
    }

    public UUID getBiosUuid() {
        return biosUuid;
    }

    public void setBiosUuid(UUID biosUuid) {
        this.biosUuid = biosUuid;
    }

    public List<String> getIpAddresses() {
        return ipAddresses;
    }

    public void setIpAddresses(List<String> ipAddresses) {
        this.ipAddresses = ipAddresses;
    }

    public String getFqdn() {
        return fqdn;
    }

    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }

    public List<String> getMacAddresses() {
        return macAddresses;
    }

    public void setMacAddresses(List<String> macAddresses) {
        this.macAddresses = macAddresses;
    }

    public Integer getCpuSockets() {
        return cpuSockets;
    }

    public void setCpuSockets(Integer cpuSockets) {
        this.cpuSockets = cpuSockets;
    }

    public Integer getCpuCores() {
        return cpuCores;
    }

    public void setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getVmHost() {
        return vmHost;
    }

    public Boolean getVirtual() {
        return isVirtual;
    }

    public void setVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    public void setVmHost(String vmHost) {
        this.vmHost = vmHost;
    }

    public List<String> getRhProd() {
        return rhProd;
    }

    public void setRhProd(List<String> rhProd) {
        this.rhProd = rhProd;
    }
}