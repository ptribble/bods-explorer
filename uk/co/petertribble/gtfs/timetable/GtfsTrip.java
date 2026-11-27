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
 * Represents a bus trip.
 */
public final class GtfsTrip {

    private final String route_id;
    private final String service_id;
    private final String trip_id;
    private final String trip_headsign;
    private final String direction_id;
    private final String block_id;
    private final String shape_id;
    private final String wheelchair_accessible;
    private final String vehicle_journey_code;

    public GtfsTrip(final String rid, final String sid, final String tid,
		final String headsign, final String direction,
		final String block, final String shape,
		final String accessible, final String vjcode) {
	route_id = stripquotes(rid);
	service_id = stripquotes(sid);
	trip_id = stripquotes(tid);
	trip_headsign = stripquotes(headsign);
	direction_id = stripquotes(direction);
	block_id = stripquotes(block);
	shape_id = stripquotes(shape);
	wheelchair_accessible = stripquotes(accessible);
	vehicle_journey_code = stripquotes(vjcode);
    }

    public String getRouteID() {
	return route_id;
    }

    public String getServiceID() {
	return service_id;
    }

    public String getTripID() {
	return trip_id;
    }

    public String getHeadsign() {
	return trip_headsign;
    }

    public String getDirection() {
	return direction_id;
    }

    public String getBlock() {
	return block_id;
    }

    public String getShape() {
	return shape_id;
    }

    public String getWheelchairAccessible() {
	return wheelchair_accessible;
    }

    public String getVehicleJourneyCode() {
	return vehicle_journey_code;
    }

    @Override
    public String toString() {
	return route_id + "\n"
	    + "  service_id = " + service_id + "\n"
	    + "  trip_id = " + trip_id + "\n"
	    + "  trip_headsign = " + trip_headsign + "\n"
	    + "  direction_id = " + direction_id + "\n"
	    + "  block_id = " + block_id + "\n"
	    + "  shape_id = " + shape_id + "\n"
	    + "  wheelchair_accessible = " + wheelchair_accessible + "\n"
	    + "  vehicle_journey_code = " + vehicle_journey_code + "\n";
    }

    /*
     * Helper to strip quotes from the incoming strings.
     */
    private String stripquotes(final String instring) {
	return instring.replace("\"", "");
    }
}
