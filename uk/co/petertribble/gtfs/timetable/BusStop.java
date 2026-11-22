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
 * Represents a bus stop.
 */
public final class BusStop {

    private final String stop_id;
    private final String stop_code;
    private final String stop_name;
    private final String stop_lat;
    private final String stop_lon;
    private final String wheelchair_boarding;
    private final String location_type;
    private final String parent_station;
    private final String platform_code;

    public BusStop(final String id, final String code, final String name,
		   final String lat, final String lon,
		   final String wheelchair, final String loctype,
		   final String parent, final String platform) {
	stop_id = stripquotes(id);
	stop_code = stripquotes(code);
	stop_name = stripquotes(name);
	stop_lat = stripquotes(lat);
	stop_lon = stripquotes(lon);
	wheelchair_boarding = stripquotes(wheelchair);
	location_type = stripquotes(loctype);
	parent_station = stripquotes(parent);
	platform_code = stripquotes(platform);
    }

    public String getID() {
	return stop_id;
    }

    public String getCode() {
	return stop_code;
    }

    public String getName() {
	return stop_name;
    }

    public String getLat() {
	return stop_lat;
    }

    public String getLon() {
	return stop_lon;
    }

    public String getWheelchairBoarding() {
	return wheelchair_boarding;
    }

    public String getLocType() {
	return location_type;
    }

    public String getParent() {
	return parent_station;
    }

    public String getPlatformCode() {
	return platform_code;
    }

    @Override
    public String toString() {
	return stop_id + "\n"
	    + "  code = " + stop_code + "\n"
	    + "  name = " + stop_name + "\n"
	    + "  lat = " + stop_lat + "\n"
	    + "  lon = " + stop_lon + "\n"
	    + "  wheelchair_boarding = " + wheelchair_boarding + "\n"
	    + "  location_type = " + location_type + "\n"
	    + "  parent_station = " + parent_station + "\n"
	    + "  platform_code = " + platform_code + "\n";
    }

    /*
     * Helper to strip quotes from the incoming strings.
     */
    private String stripquotes(final String instring) {
	return instring.replace("\"", "");
    }
}
