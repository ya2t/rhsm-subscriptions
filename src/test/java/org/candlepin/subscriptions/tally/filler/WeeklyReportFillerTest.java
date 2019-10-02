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
package org.candlepin.subscriptions.tally.filler;

import static org.candlepin.subscriptions.tally.filler.Assertions.assertSnapshot;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.candlepin.subscriptions.FixedClockConfiguration;
import org.candlepin.subscriptions.db.model.Granularity;
import org.candlepin.subscriptions.util.ApplicationClock;
import org.candlepin.subscriptions.utilization.api.model.TallyReport;
import org.candlepin.subscriptions.utilization.api.model.TallySnapshot;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

public class WeeklyReportFillerTest {

    private ApplicationClock clock;
    private ReportFiller filler;

    public WeeklyReportFillerTest() {
        clock = new FixedClockConfiguration().fixedClock();
        filler = ReportFillerFactory.getInstance(clock, Granularity.WEEKLY);
    }

    @Test
    public void noExistingSnapsShouldFillWithWeeklyGranularity() {
        OffsetDateTime start = clock.startOfCurrentWeek();
        OffsetDateTime end = start.plusWeeks(3);

        TallyReport report = new TallyReport();
        filler.fillGaps(report, start, end);

        List<TallySnapshot> filled = report.getData();
        assertEquals(4, filled.size());
        assertSnapshot(filled.get(0), start, 0, 0, 0, false);
        assertSnapshot(filled.get(1), start.plusWeeks(1), 0, 0, 0, false);
        assertSnapshot(filled.get(2), start.plusWeeks(2), 0, 0, 0, false);
        assertSnapshot(filled.get(3), start.plusWeeks(3), 0, 0, 0, false);
    }

    @Test
    public void startAndEndDatesForWeeklyAreResetWhenDateIsMidWeek() {
        // Mid week start
        OffsetDateTime start = clock.now();
        // Mid week end
        OffsetDateTime end = start.plusWeeks(3);

        // Expected to start on the beginning of the week.
        OffsetDateTime expectedStart = clock.startOfWeek(start);

        TallyReport report = new TallyReport();
        filler.fillGaps(report, start, end);

        List<TallySnapshot> filled = report.getData();
        assertEquals(4, filled.size());
        assertSnapshot(filled.get(0), expectedStart, 0, 0, 0, false);
        assertSnapshot(filled.get(1), expectedStart.plusWeeks(1), 0, 0, 0, false);
        assertSnapshot(filled.get(2), expectedStart.plusWeeks(2), 0, 0, 0, false);
        assertSnapshot(filled.get(3), expectedStart.plusWeeks(3), 0, 0, 0, false);
    }

    @Test
    public void testSnapshotsIgnoredWhenNoDatesSet() {
        OffsetDateTime start = clock.startOfCurrentWeek();
        OffsetDateTime end = start.plusWeeks(3);

        TallySnapshot snap1 = new TallySnapshot().cores(2).sockets(3).instanceCount(4)
            .hasData(true);
        TallySnapshot snap2 = new TallySnapshot().cores(5).sockets(6).instanceCount(7)
            .hasData(true);
        List<TallySnapshot> snaps = Arrays.asList(snap1, snap2);

        TallyReport report = new TallyReport().data(snaps);
        filler.fillGaps(report, start, end);

        List<TallySnapshot> filled = report.getData();
        assertEquals(4, filled.size());
        assertSnapshot(filled.get(0), start, 0, 0, 0, false);
        assertSnapshot(filled.get(1), start.plusWeeks(1), 0, 0, 0, false);
        assertSnapshot(filled.get(2), start.plusWeeks(2), 0, 0, 0, false);
        assertSnapshot(filled.get(3), start.plusWeeks(3), 0, 0, 0, false);
    }

    @Test
    public void shouldFillGapsBasedOnExistingSnapshotsForWeeklyGranularity() {
        OffsetDateTime start = clock.startOfCurrentWeek();
        OffsetDateTime snap1Date = start.plusWeeks(1);
        OffsetDateTime end = start.plusWeeks(3);

        TallySnapshot snap1 = new TallySnapshot().date(snap1Date).cores(2).sockets(3).instanceCount(4)
            .hasData(true);
        TallySnapshot snap2 = new TallySnapshot().date(end).cores(5).sockets(6).instanceCount(7)
            .hasData(true);
        List<TallySnapshot> snaps = Arrays.asList(snap1, snap2);

        TallyReport report = new TallyReport().data(snaps);
        filler.fillGaps(report, start, end);

        List<TallySnapshot> filled = report.getData();
        assertEquals(4, filled.size());
        assertSnapshot(filled.get(0), start, 0, 0, 0, false);
        assertSnapshot(filled.get(1), snap1.getDate(), snap1.getCores(), snap1.getSockets(),
            snap1.getInstanceCount(), true);
        assertSnapshot(filled.get(2), start.plusWeeks(2), 0, 0, 0, false);
        assertSnapshot(filled.get(3), snap2.getDate(), snap2.getCores(), snap2.getSockets(),
            snap2.getInstanceCount(), true);
    }

}
