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

public final class ReadAll {

    private ReadAgency agencyReader;
    private ReadRoutes routeReader;
    private ReadStops stopReader;
    private ReadStopTimes stopTimeReader;
    private ReadTrips tripReader;

    public ReadAll(final String fname) {
	this(new File(fname));
    }

    public ReadAll(final File f) {
	agencyReader = new ReadAgency(f);
	routeReader = new ReadRoutes(f);
	stopReader = new ReadStops(f);
	stopTimeReader = new ReadStopTimes(f);
	tripReader = new ReadTrips(f);
    }

    private void printStats() {
	System.out.println(agencyReader.getStats());
	System.out.println(routeReader.getStats());
	System.out.println(stopReader.getStats());
	System.out.println(stopTimeReader.getStats());
	System.out.println(tripReader.getStats());
    }

    private static void usage() {
	System.err.println("Usage: ReadAll [-t trip] -d dir");
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
	 * -s short list, id + name
	 * -i id
	 * -t stops for that trip
	 */
	i = 0;
	while (i < args.length) {
	    if ("-i".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing stop");
		    }
		    // reader.printStop(args[i+1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing stop");
		}
	    } else if ("-t".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing trip");
		    }
		    // reader.printTrip(args[i+1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing stop");
		}
	    } else if ("-s".equals(args[i])) {
		// reader.printShort();
		havewhat = true;
	    } else if ("-d".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (!havewhat) {
	    reader.printStats();
	}
    }
}
