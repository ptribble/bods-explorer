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
import java.util.Map;
import java.util.TreeSet;

public final class ReadAll {

    private ReadAgency agencyReader;
    private ReadCalendar calendarReader;
    private ReadRoutes routeReader;
    private ReadStops stopReader;
    private ReadStopTimes stopTimeReader;
    private ReadTrips tripReader;

    public ReadAll(final String fname) {
	this(new File(fname));
    }

    public ReadAll(final File f) {
	agencyReader = new ReadAgency(f);
	calendarReader = new ReadCalendar(f);
	routeReader = new ReadRoutes(f);
	stopReader = new ReadStops(f);
	stopTimeReader = new ReadStopTimes(f);
	tripReader = new ReadTrips(f);
    }

    /*
     * Prints out the number of things. If verbose, adds a breakdown
     * after each count.
     */
    private void printStats(final boolean verbose) {
	System.out.println(agencyReader.getStats());
	System.out.println(routeReader.getStats());
	if (verbose) {
	    printRouteStats();
	}
	System.out.println(tripReader.getStats());
	if (verbose) {
	    printTripStats();
	}
	System.out.println(stopTimeReader.getStats());
	System.out.println(stopReader.getStats());
	System.out.println(calendarReader.getStats());
    }

    /*
     * Prints out the number of routes per operator.
     * The output is sorted so that it's stable,
     */
    private void printRouteStats() {
	final Map<String, List<GtfsRoute>> opmap = routeReader.getOperatorMap();
	for (final String opid : new TreeSet<String>(opmap.keySet())) {
	    final String opname = agencyReader.getAgencyById(opid).getNAME();
	    System.out.println("    " + opid + " " + opmap.get(opid).size()
			       + " [" + opname + "]");
	}
    }

    /*
     * Prints out the number of trips per operator.
     * The output is sorted so that it's stable,
     */
    private void printTripStats() {
	final Map<String, List<GtfsRoute>> opmap = routeReader.getOperatorMap();
	final Map<String, List<GtfsTrip>> tripmap = tripReader.getRouteMap();
	for (final String opid : new TreeSet<String>(opmap.keySet())) {
	    final String opname = agencyReader.getAgencyById(opid).getNAME();
	    int tsize = 0;
	    for (final GtfsRoute route : opmap.get(opid)) {
		tsize += tripmap.get(route.getID()).size();
	    }
	    System.out.println("    " + opid + " " + tsize
			       + " [" + opname + "]");
	}
    }

    /*
     * Prints out the number of trips per route, with operator ids.
     */
    private void printRouteTripStats() {
	final Map<String, List<GtfsRoute>> opmap = routeReader.getOperatorMap();
	final Map<String, List<GtfsTrip>> tripmap = tripReader.getRouteMap();
	for (final String opid : new TreeSet<String>(opmap.keySet())) {
	    final String opname = agencyReader.getAgencyById(opid).getNAME();
	    for (final GtfsRoute route : opmap.get(opid)) {
		System.out.println("    " + opid + ":" + route.getID() + " "
				   + tripmap.get(route.getID()).size()
				   + " [" + opname + ", "
				   + route.getShortName() + "]");
	    }
	}
    }

    /*
     * Prints out departures per hour.
     */
    private void printDeparturesPerHour() {
	final List<GtfsStopTime> stlist = stopTimeReader.allStops();
	final int[] counter = new int[32];
	for (final GtfsStopTime st : stlist) {
	    final String s = st.getDeparture();
	    final String s2 = s.substring(0, s.indexOf(':'));
	    final int i = Integer.parseInt(s2);
	    counter[i]++;
	}
	for (int ii = 0; ii < 32; ii++) {
	    System.out.println(ii + " " + counter[ii]);
	}
    }

    /*
     * Prints out departures per hour, for a given operator.
     */
    private void printDeparturesPerHour(final String opid) {
	final List<GtfsStopTime> stlist = stopTimeReader.allStops();
	final int[] counter = new int[32];
	for (final GtfsStopTime st : stlist) {
	    //chain through to get the operator of this trip
	    final GtfsTrip ntrip = tripReader.getTripById(st.getTripID());
	    final GtfsRoute nroute = routeReader.getRouteById(ntrip.getRouteID());
	    final String ropid = nroute.getOPID();
	    //
	    if (opid.equals(ropid)) {
		final String s = st.getDeparture();
		final String s2 = s.substring(0, s.indexOf(':'));
		final int i = Integer.parseInt(s2);
		counter[i]++;
	    }
	}
	for (int ii = 0; ii < 32; ii++) {
	    System.out.println(ii + " " + counter[ii]);
	}
    }

    private static void usage() {
	System.err.println("Usage: ReadAll args -d dir");
	System.err.println("  -r shows route count per operator");
	System.err.println("  -t shows trip count per operator");
	System.err.println("  -rt prints trip count per route");
	System.err.println("  -v prints everything, verbosely");
	System.err.println("  -h prints stop counts per hour");
	System.err.println("  -ho prints stop counts per hour for the given operator");
	System.exit(2);
    }

    private static void usage(final String err) {
	System.err.println("ERROR: " + err);
	usage();
    }

    public static void main(final String[] args) throws Exception {
	String fname = null;
	boolean havewhat = false;
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
	final ReadAll reader = new ReadAll(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -r prints route count per operator
	 * -t prints trip count per operator
	 * -rt prints trip count per route
	 * -v prints everything, verbosely
	 * -h prints stop counts per hour
	 * -ho prints stop counts per hour for the given operator
	 */
	i = 0;
	while (i < args.length) {
	    if ("-r".equals(args[i])) {
		reader.printRouteStats();
		havewhat = true;
	    } else if ("-t".equals(args[i])) {
		reader.printTripStats();
		havewhat = true;
	    } else if ("-rt".equals(args[i])) {
		reader.printRouteTripStats();
		havewhat = true;
	    } else if ("-h".equals(args[i])) {
		reader.printDeparturesPerHour();
		havewhat = true;
	    } else if ("-ho".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing operator");
		    }
		    reader.printDeparturesPerHour(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing operator");
		}
	    } else if ("-v".equals(args[i])) {
		reader.printStats(true);
		havewhat = true;
	    } else if ("-d".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (!havewhat) {
	    reader.printStats(false);
	}
    }
}
