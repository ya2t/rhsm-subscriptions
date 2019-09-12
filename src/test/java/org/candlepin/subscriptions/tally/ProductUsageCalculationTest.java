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

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

public class ProductUsageCalculationTest {

    @Test
    public void testIncrementInstances() {
        ProductUsageCalculation calculation = new ProductUsageCalculation("Product");
        int expectedInstances = 10;
        IntStream.rangeClosed(0, 4).forEach(i -> calculation.addInstances(i));
        assertEquals(expectedInstances, calculation.getInstanceCount());
    }

    @Test
    public void testAddingCores() {
        ProductUsageCalculation calculation = new ProductUsageCalculation("Product");
        int expectedCores = 10;
        IntStream.rangeClosed(0, 4).forEach(i -> calculation.addCores(i));
        assertEquals(expectedCores, calculation.getTotalCores());
    }

    @Test
    public void testAddingSockets() {
        ProductUsageCalculation calculation = new ProductUsageCalculation("Product");
        int expectedSockets = 10;
        IntStream.rangeClosed(0, 4).forEach(i -> calculation.addSockets(i));
        assertEquals(expectedSockets, calculation.getTotalSockets());
    }

}