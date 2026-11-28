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

import java.io.File;
import java.util.List;

public final class TimeTable {

    private ReadAgency agencyReader;
    private ReadCalendar calendarReader;
    private ReadRoutes routeReader;
    private ReadStops stopReader;
    private ReadStopTimes stopTimeReader;
    private ReadTrips tripReader;

    public TimeTable(final String fname) {
	this(new File(fname));
    }

    public TimeTable(final File f) {
	agencyReader = new ReadAgency(f);
	calendarReader = new ReadCalendar(f);
	routeReader = new ReadRoutes(f);
	stopReader = new ReadStops(f);
	stopTimeReader = new ReadStopTimes(f);
	tripReader = new ReadTrips(f);
    }

    /*
     * Prints out a quick summary of the route.
     */
    private void printSummary(final String route) {
	final List<GtfsTrip> tlist = tripReader.getTripsForRoute(route);
	final GtfsRoute r = routeReader.getRouteById(route);
	if (r == null) {
	    System.out.println("No such route " + route);
	}
	final GtfsAgency routeop = agencyReader.getAgencyById(r.getOPID());
	System.out.println("Route " + route
			   + " is operated by " + routeop.getNAME()
			   + " with " + tlist.size() + " trips");
	for (GtfsTrip t : tlist) {
	    List<GtfsStopTime> stlist = stopTimeReader.getStopsForTrip(t.getTripID());
	    // work out start time
	    String tstart = null;
	    for (final GtfsStopTime st : stlist) {
		if ("0".equals(st.getStopSeq())) {
		    tstart = st.getDeparture();
		}
	    }
	    final GtfsCalendar calid = calendarReader.getCalendar(t.getServiceID());
	    final String sdays = calid == null ? "" : " on " + calid.daysRunning();
	    System.out.println("  Service " + r.getShortName()
			       + " dir " + t.getDirection()
			       + " to " + t.getHeadsign()
			       + sdays
			       + " at " + tstart
			       + " has " + stlist.size() + " stops");
	}
    }

    /*
     * Prints out the timetable.
     */
    private void printTimeTable(final String route) {
	final List<GtfsTrip> tlist = tripReader.getTripsForRoute(route);
	final GtfsRoute r = routeReader.getRouteById(route);
	if (r == null) {
	    System.out.println("No such route " + route);
	}
	final GtfsAgency routeop = agencyReader.getAgencyById(r.getOPID());
	System.out.println("Route " + route + " is operated by "
			   + routeop.getNAME());
	System.out.println(" has " + tlist.size() + " trips");
	for (GtfsTrip t : tlist) {
	    List<GtfsStopTime> stlist = stopTimeReader.getStopsForTrip(t.getTripID());
	    for (GtfsStopTime st : stlist) {
		System.out.println(st.getStopSeq() + " "
				   + st.getStopID() + " ("
				   + stopReader.getStopById(st.getStopID()).getName()
				   + ") at "
				   + st.getArrival() + "/" + st.getDeparture());
	    }
	}
    }

    private static void usage() {
	System.err.println("Usage: TimeTable args [-s] -r route -d dir");
	System.exit(2);
    }

    private static void usage(final String err) {
	System.err.println("ERROR: " + err);
	usage();
    }

    public static void main(final String[] args) throws Exception {
	String fname = null;
	String route = null;
	boolean summary = false;
	/*
	 * Two passes over the arguments. In the first pass we just look
	 * for the mandatory directory name.
	 */
	int i = 0;
	while (i < args.length) {
	    if ("-help".equals(args[i]) || "--help".equals(args[i])) {
		usage();
	    }
	    if ("-d".equals(args[i])) {
		if (i + 1 < args.length) {
		    final File ftest = new File(args[i + 1]);
		    if (!ftest.exists()) {
			usage("directory doesn't exist");
		    }
		    fname = ftest.getPath();
		} else {
		    usage();
		}
	    }
	    i++;
	}
	if (fname == null) {
	    usage("missing filename");
	}
	final TimeTable reader = new TimeTable(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -r the route to show
	 * -s just a summary (or debug output)
	 * TODO output format text/csv/html
	 */
	i = 0;
	while (i < args.length) {
	    if ("-s".equals(args[i])) {
		summary = true;
	    } else if ("-r".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing route");
		    }
		    route = args[i + 1];
		    i++;
		} else {
		    usage("missing route");
		}
	    } else if ("-d".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (route == null) {
	    usage("missing route");
	}
	if (summary) {
	    reader.printSummary(route);
	} else {
	    reader.printTimeTable(route);
	}
    }
}
