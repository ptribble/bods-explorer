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
import java.util.regex.Pattern;

public final class ReadCalendar {

    private static final String CALENDAR_FILE = "calendar.txt";

    private static final Pattern PCSV = Pattern.compile(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

    private List<GtfsCalendar> rlist = new ArrayList<>();
    private Map<String, GtfsCalendar> rmap = new HashMap<>();

    public ReadCalendar(final String fname) {
	this(new File(fname));
    }

    public ReadCalendar(final File f) {
	final File f1 = f.isDirectory() ? new File(f, CALENDAR_FILE) : f;
	try {
	    for (final String line : Files.readAllLines(f1.toPath())) {
		final String[] pstr = PCSV.split(line, 10);
		if (!"service_id".equals(pstr[0])) {
		    final GtfsCalendar rtemp = new GtfsCalendar(pstr[0], pstr[1], pstr[2], pstr[3], pstr[4], pstr[5], pstr[6], pstr[7], pstr[8], pstr[9]);
		    rlist.add(rtemp);
		    rmap.put(pstr[0], rtemp);
		}
	    }
	} catch (IOException ioe) {
	}
    }

    public List<GtfsCalendar> allCalendars() {
	return rlist;
    }

    public GtfsCalendar getCalendar(final String id) {
	return rmap.get(id);
    }

    public String getStats() {
	return "calendar has " + rlist.size() + " entries.";
    }

    private void printAll() {
	for (final GtfsCalendar r : rlist) {
	    printCalendar(r);
	}
    }

    private void printShort() {
	for (final GtfsCalendar r : rlist) {
	    System.out.println(r.getID() + " = " + r.daysRunning());
	}
    }

    private void printCalendar(final String name) {
	System.out.println(rmap.get(name));
    }

    private void printCalendar(final GtfsCalendar r) {
	System.out.println(r);
    }

    private static void usage() {
	System.err.println("Usage: ReadCalendar [-i id|-s] -d dir");
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
		    final File ftest = new File(args[i + 1], CALENDAR_FILE);
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
	final ReadCalendar reader = new ReadCalendar(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -i just the given id
	 * -s short list, id + days
	 */
	i = 0;
	while (i < args.length) {
	    if ("-i".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing id");
		    }
		    reader.printCalendar(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing id");
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
