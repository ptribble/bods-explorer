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
 * Represents a stop time.
 */
public final class StopTime {

    private final String trip_id;
    private final String arrival_time;
    private final String departure_time;
    private final String stop_id;
    private final String stop_sequence;
    private final String stop_headsign;
    private final String pickup_type;
    private final String drop_off_type;
    private final String shape_dist_travelled;
    private final String timepoint;

    public StopTime(final String tripid, final String atime, final String dtime, final String stopid, final String seq, final String headsign, final String ptype, final String dtype, final String dist, final String tpoint) {
	trip_id = stripquotes(tripid);
	arrival_time = stripquotes(atime);
	departure_time = stripquotes(dtime);
	stop_id = stripquotes(stopid);
	stop_sequence = stripquotes(seq);
	stop_headsign = stripquotes(headsign);
	pickup_type = stripquotes(ptype);
	drop_off_type = stripquotes(dtype);
	shape_dist_travelled = stripquotes(dist);
	timepoint = stripquotes(tpoint);
    }

    public String getTripID() {
	return trip_id;
    }

    public String getArrival() {
	return arrival_time;
    }

    public String getDeparture() {
	return departure_time;
    }

    public String getStopID() {
	return stop_id;
    }

    public String getStopSeq() {
	return stop_sequence;
    }

    public String getHeadsign() {
	return stop_headsign;
    }

    public String getPickupType() {
	return pickup_type;
    }

    public String getDropoffType() {
	return drop_off_type;
    }

    public String getDistance() {
	return shape_dist_travelled;
    }

    public String getTimepoint() {
	return timepoint;
    }

    @Override
    public String toString() {
	return trip_id + "\n"
	    + "  arrival_time  = " + arrival_time + "\n"
	    + "  departure_time  = " + departure_time + "\n"
	    + "  stop_id  = " + stop_id + "\n"
	    + "  stop_sequence  = " + stop_sequence + "\n"
	    + "  stop_headsign  = " + stop_headsign + "\n"
	    + "  pickup_type  = " + pickup_type + "\n"
	    + "  drop_off_type  = " + drop_off_type + "\n"
	    + "  shape_dist_travelled  = " + shape_dist_travelled + "\n"
	    + "  timepoint  = " + timepoint + "\n";
    }

    /*
     * Helper to strip quotes from the incoming strings.
     */
    private String stripquotes(final String instring) {
	return instring.replace("\"", "");
    }
}
