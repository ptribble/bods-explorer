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

public final class ReadStops {

    private static final String STOPS_FILE = "stops.txt";

    private List<BusStop> slist = new ArrayList<>();
    private Map<String, BusStop> smap = new HashMap<>();

    public ReadStops(final String fname) {
	this(new File(fname));
    }

    public ReadStops(final File f) {
	final File f1 = f.isDirectory() ? new File(f, STOPS_FILE) : f;
	try {
	    for (final String line : Files.readAllLines(f1.toPath())) {
		final String[] pstr = line.split(",", 10);
		if (pstr.length == 10) {
		    // there's a comma embedded in the name
		    final BusStop stemp = new BusStop(pstr[0], pstr[1], pstr[2] + pstr[3], pstr[4], pstr[5], pstr[6], pstr[7], pstr[8], pstr[9]);
			slist.add(stemp);
			smap.put(pstr[0], stemp);

		} else if (pstr.length == 9) {
		    if (!"stop_id".equals(pstr[0])) {
			final BusStop stemp = new BusStop(pstr[0], pstr[1], pstr[2], pstr[3], pstr[4], pstr[5], pstr[6], pstr[7], pstr[8]);
			slist.add(stemp);
			smap.put(pstr[0], stemp);
		    }
		} else {
		    System.err.println(pstr.length + line);
		}
	    }
	} catch (IOException ioe) {
	}
    }

    public List<BusStop> allStops() {
	return slist;
    }

    public BusStop getStopById(final String id) {
	return smap.get(id);
    }

    public String getStats() {
	return "stops has " + slist.size() + " entries.";
    }

    private void printAll() {
	for (final BusStop s : slist) {
	    printStop(s);
	}
    }

    private void printShort() {
	for (final BusStop s : slist) {
	    System.out.println(s.getID() + " = " + s.getName());
	}
    }

    private void printStop(final String name) {
	System.out.println(smap.get(name));
    }

    private void printStop(final BusStop s) {
	System.out.println(s);
    }

    private void printCode(final String code) {
	final String lcode = code.toLowerCase();
	for (final BusStop s : slist) {
	    if (s.getCode().equalsIgnoreCase(lcode)) {
		printStop(s);
	    }
	}
    }

    private static void usage() {
	System.err.println("Usage: ReadStops [-s|-c code|-i id] -d dir");
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
		    final File ftest = new File(args[i + 1], STOPS_FILE);
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
	final ReadStops reader = new ReadStops(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -s short list, id + name
	 * -i id
	 * -c code
	 */
	i = 0;
	while (i < args.length) {
	    if ("-i".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing stop");
		    }
		    reader.printStop(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing stop");
		}
	    } else if ("-c".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing code");
		    }
		    reader.printCode(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing stop");
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
