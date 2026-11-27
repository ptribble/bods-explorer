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

public final class ReadStopTimes {

    private static final String STOPTIMES_FILE = "stop_times.txt";

    private List<GtfsStopTime> slist = new ArrayList<>();
    /**
     * A Map of trips with the List of stops for each trip.
     */
    private Map<String, List<GtfsStopTime>> smap = new HashMap<>();

    public ReadStopTimes(final String fname) {
	this(new File(fname));
    }

    public ReadStopTimes(final File f) {
	final File f1 = f.isDirectory() ? new File(f, STOPTIMES_FILE) : f;
	try {
	    for (final String line : Files.readAllLines(f1.toPath())) {
		final String[] pstr = line.split(",", 10);
		if (pstr.length == 10) {
		    if (!"trip_id".equals(pstr[0])) {
			final GtfsStopTime stemp = new GtfsStopTime(pstr[0], pstr[1], pstr[2], pstr[3], pstr[4], pstr[5], pstr[6], pstr[7], pstr[8], pstr[9]);
			slist.add(stemp);
			// now accumulate all the stops for a given trip
			final List<GtfsStopTime> ilist = smap.containsKey(pstr[0])
			    ? smap.get(pstr[0])
			    : new ArrayList<>();
			ilist.add(stemp);
			if (!smap.containsKey(pstr[0])) {
			    smap.put(pstr[0], ilist);
			}
		    }
		} else {
		    System.err.println(pstr.length + line);
		}
	    }
	} catch (IOException ioe) {
	}
    }

    public List<GtfsStopTime> allStops() {
	return slist;
    }

    public List<GtfsStopTime> getStopsForTrip(final String trip) {
	return smap.get(trip);
    }

    public String getStats() {
	return "stop_times has " + slist.size() + " entries in "
	       + smap.size() + " trips.";
    }

    private void printAll() {
	for (final GtfsStopTime s : slist) {
	    printStop(s);
	}
    }

    private void printTrip(final String trip) {
	if (smap.containsKey(trip)) {
	    for (final GtfsStopTime s : smap.get(trip)) {
		printStop(s);
	    }
	} else {
	    System.err.println("Invalid trip");
	}
    }

    private void printStop(final GtfsStopTime s) {
	System.out.println(s);
    }

    private static void usage() {
	System.err.println("Usage: ReadStopTimes [-t trip] -d dir");
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
	    if ("-help".equals(args[i]) || "--help".equals(args[i])) {
		usage();
	    }
	    if ("-d".equals(args[i])) {
		if (i + 1 < args.length) {
		    final File ftest = new File(args[i + 1], STOPTIMES_FILE);
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
	final ReadStopTimes reader = new ReadStopTimes(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -t stops for the given trip
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
		    usage("missing stop");
		}
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
