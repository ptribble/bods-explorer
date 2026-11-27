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
 * Represents a bus route.
 */
public final class GtfsRoute {

    private final String route_id;
    private final String agency_id;
    private final String route_short_name;
    private final String route_long_name;
    private final String route_type;

    public GtfsRoute(final String id, final String opid,
		 final String sname, final String lname, final String type) {
	route_id = stripquotes(id);
	agency_id = stripquotes(opid);
	route_short_name = stripquotes(sname);
	route_long_name = stripquotes(lname);
	route_type = stripquotes(type);
    }

    public String getID() {
	return route_id;
    }

    public String getOPID() {
	return agency_id;
    }

    public String getShortName() {
	return route_short_name;
    }

    public String getLongName() {
	return route_long_name;
    }

    public String getType() {
	return route_type;
    }

    @Override
    public String toString() {
	return route_id + "\n"
	    + "  operator = " + agency_id + "\n"
	    + "  short_name = " + route_short_name + "\n"
	    + "  long_name = " + route_long_name + "\n"
	    + "  type = " + route_type + "\n";
    }

    /*
     * Helper to strip quotes from the incoming strings.
     */
    private String stripquotes(final String instring) {
	return instring.replace("\"", "");
    }
}
