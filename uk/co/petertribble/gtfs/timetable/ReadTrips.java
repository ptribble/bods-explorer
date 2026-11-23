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
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ReadTrips {

    private static final String TRIPS_FILE = "trips.txt";

    private List<Trip> slist = new ArrayList<>();
    /**
     * A Map of trip_id to the trip.
     */
    private Map<String, Trip> smap = new HashMap<>();
    /**
     * A Map of routes with the List of trips for each route.
     */
    private Map<String, List<Trip>> rmap = new HashMap<>();

    public ReadTrips(final String fname) {
	this(new File(fname));
    }

    public ReadTrips(final File f) {
	final File f1 = f.isDirectory() ? new File(f, TRIPS_FILE) : f;
	try {
	    for (final String line : Files.readAllLines(f1.toPath())) {
		final String[] pstr = line.split(",", 11);
		if (pstr.length == 11) {
		    // there are 2 commas embedded in the name
		    final Trip stemp = new Trip(pstr[0], pstr[1], pstr[2], pstr[3] + "," + pstr[4] + "," + pstr[5], pstr[6], pstr[7], pstr[8], pstr[9], pstr[10]);
			slist.add(stemp);
			smap.put(pstr[2], stemp);
			// now accumulate all the trips for a given route
			final List<Trip> ilist = rmap.containsKey(pstr[0])
			    ? rmap.get(pstr[0])
			    : new ArrayList<>();
			ilist.add(stemp);
			if (!rmap.containsKey(pstr[0])) {
			    rmap.put(pstr[0], ilist);
			}
		} else if (pstr.length == 10) {
		    // there's a comma embedded in the name
		    final Trip stemp = new Trip(pstr[0], pstr[1], pstr[2], pstr[3] + "," + pstr[4], pstr[5], pstr[6], pstr[7], pstr[8], pstr[9]);
			slist.add(stemp);
			smap.put(pstr[2], stemp);
			// now accumulate all the trips for a given route
			final List<Trip> ilist = rmap.containsKey(pstr[0])
			    ? rmap.get(pstr[0])
			    : new ArrayList<>();
			ilist.add(stemp);
			if (!rmap.containsKey(pstr[0])) {
			    rmap.put(pstr[0], ilist);
			}
		} else if (pstr.length == 9) {
		    if (!"route_id".equals(pstr[0])) {
			final Trip stemp = new Trip(pstr[0], pstr[1], pstr[2], pstr[3], pstr[4], pstr[5], pstr[6], pstr[7], pstr[8]);
			slist.add(stemp);
			smap.put(pstr[2], stemp);
			// now accumulate all the trips for a given route
			final List<Trip> ilist = rmap.containsKey(pstr[0])
			    ? rmap.get(pstr[0])
			    : new ArrayList<>();
			ilist.add(stemp);
			if (!rmap.containsKey(pstr[0])) {
			    rmap.put(pstr[0], ilist);
			}
		    }
		} else {
		    System.err.println(pstr.length + line);
		}
	    }
	} catch (IOException ioe) {
	}
    }

    public List<Trip> allTrips() {
	return slist;
    }

    /**
     * This gives a mapping of lists of trips to routes.
     *
     * @return a map of lists of trips keyed by route id
     */
    public Map<String, List<Trip>> getRouteMap() {
	Map<String, List<Trip>> routemap = new HashMap<>();
	for (final Trip t : slist) {
	    String rid = t.getRouteID();
	    List<Trip> tlist = routemap.containsKey(rid)
		? routemap.get(rid)
		: new ArrayList<>();
	    tlist.add(t);
	    if (!routemap.containsKey(rid)) {
		routemap.put(rid, tlist);
	    }
	}
	return routemap;
    }

    public Trip getTripById(final String id) {
	return smap.get(id);
    }

    public List<Trip> getTripsForRoute(final String route) {
	return rmap.get(route);
    }

    public String getStats() {
	return "trips has " + slist.size() + " entries in "
	       + rmap.size() + " routes.";
    }

    private void printAll() {
	for (final Trip s : slist) {
	    printTrip(s);
	}
    }

    private void printRoute(final String route) {
	if (rmap.containsKey(route)) {
	    for (final Trip s : rmap.get(route)) {
		printTrip(s);
	    }
	} else {
	    System.err.println("Invalid route");
	}
    }

    private void printShort() {
	for (final Trip s : slist) {
	    System.out.println(s.getServiceID() + " = " + s.getHeadsign());
	}
    }

    private void printTrip(final String name) {
	System.out.println(smap.get(name));
    }

    private void printTrip(final Trip s) {
	System.out.println(s);
    }

    private static void usage() {
	System.err.println("Usage: ReadTrips [-s|-t trip] -d dir");
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
	 * for the mandatory file location.
	 */
	int i = 0;
	while (i < args.length) {
	    if ("-d".equals(args[i])) {
		if (i + 1 < args.length) {
		    final File ftest = new File(args[i + 1], TRIPS_FILE);
		    if (!ftest.exists()) {
			usage("file doesn't exist");
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
	final ReadTrips reader = new ReadTrips(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -s short list, id + headsign
	 * -i id
	 */
	i = 0;
	while (i < args.length) {
	    if ("-t".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing trip");
		    }
		    reader.printTrip(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing trip");
		}
	    } else if ("-r".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing route");
		    }
		    reader.printRoute(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing trip");
		}
	    } else if ("-s".equals(args[i])) {
		reader.printShort();
		havewhat = true;
	    } else if ("-d".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (!havewhat) {
	    reader.printAll();
	}
    }
}
