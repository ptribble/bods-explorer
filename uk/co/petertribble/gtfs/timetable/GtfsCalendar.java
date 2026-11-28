/*
 * SPDX-License-Identifier: CDDL-1.0
 *
 * This file and its contents are supplied under the terms of the
 * Common Development and Distribution License ("CDDL"), version 1.0.
 * You may only use this file in accordance with the terms of version
 * 1.0 of the CDDL.
 *
 * A full copy of the text of the CDDL should have accompanied this
 * source. A copy of the CDDL is also available via the Internet at
 * http://www.illumos.org/license/CDDL.
 *
 * Copyright 2026 Peter C. Tribble
 */

package uk.co.petertribble.gtfs.timetable;

/**
 * Represents a timetable calendar.
 */
public final class GtfsCalendar {

    private final String service_id;
    private final boolean monday;
    private final boolean tuesday;
    private final boolean wednesday;
    private final boolean thursday;
    private final boolean friday;
    private final boolean saturday;
    private final boolean sunday;
    private final String start_date;
    private final String end_date;

    public GtfsCalendar(final String id, final String mo,
			final String tu, final String wed, final String th,
			final String fri, final String sat, final String sun,
			final String sdate, final String edate) {
	service_id = stripquotes(id);
	monday = dayruns(mo);
	tuesday = dayruns(tu);
	wednesday = dayruns(wed);
	thursday = dayruns(th);
	friday = dayruns(fri);
	saturday = dayruns(sat);
	sunday = dayruns(sun);
	start_date = stripquotes(sdate);
	end_date = stripquotes(edate);
    }

    public String getID() {
	return service_id;
    }

    public boolean runsOnMonday() {
	return monday;
    }

    public boolean runsOnTuesday() {
	return tuesday;
    }

    public boolean runsOnWednesday() {
	return wednesday;
    }

    public boolean runsOnThursday() {
	return thursday;
    }

    public boolean runsOnFriday() {
	return friday;
    }

    public boolean runsOnSaturday() {
	return saturday;
    }

    public boolean runsOnSunday() {
	return sunday;
    }

    public String getStartDate() {
	return start_date;
    }

    public String getEndDate() {
	return end_date;
    }

    public String daysRunning() {
	StringBuilder sb = new StringBuilder();
	if (monday) {
	    sb.append("Mo");
	}
	if (tuesday) {
	    sb.append("Tu");
	}
	if (wednesday) {
	    sb.append("We");
	}
	if (thursday) {
	    sb.append("Th");
	}
	if (friday) {
	    sb.append("Fr");
	}
	if (saturday) {
	    sb.append("Sa");
	}
	if (sunday) {
	    sb.append("Su");
	}
	return sb.toString();
    }

    @Override
    public String toString() {
	return service_id + "\n"
	    + "  runs on = " + daysRunning() + "\n"
	    + "  between = " + start_date + " and " + end_date + "\n";
    }

    /*
     * Helper to turn String representing running on that day to a boolean.
     * 1 = runs on that day (true), 0 = doesn't run (false).
     */
    private boolean dayruns(final String s) {
	return "1".equals(stripquotes(s));
    }

    /*
     * Helper to strip quotes from the incoming strings.
     */
    private String stripquotes(final String instring) {
	return instring.replace("\"", "");
    }
}
