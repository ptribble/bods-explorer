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

public final class ReadAgency {

    private static final String AGENCY_FILE = "agency.txt";

    private List<Agency> alist = new ArrayList<>();
    private Map<String, Agency> amap = new HashMap<>();

    public ReadAgency(final String fname) {
	this(new File(fname));
    }

    public ReadAgency(final File f) {
	final File f1 = f.isDirectory() ? new File(f, AGENCY_FILE) : f;
	try {
	    for (final String line : Files.readAllLines(f1.toPath())) {
		final String[] pstr = line.split(",");
		if (!"agency_id".equals(pstr[0])) {
		    final Agency atemp = new Agency(pstr[0], pstr[1], pstr[2],
					    pstr[3], pstr[4], pstr[5], pstr[6]);
		    alist.add(atemp);
		    amap.put(pstr[0], atemp);
		}
	    }
	} catch (IOException ioe) {
	}
    }

    public List<Agency> allAgencies() {
	return alist;
    }

    public Agency getAgencyById(final String id) {
	return amap.get(id);
    }

    public List<Agency> matchAgenciesByName(final String sname) {
	final String lname = sname.toLowerCase();
	final List<Agency> tlist = new ArrayList<>();
	for (final Agency operator : alist) {
	    if (operator.getNAME().toLowerCase().contains(lname)) {
		tlist.add(operator);
	    }
	}
	return tlist;
    }

    public String getStats() {
	return "agency has " + alist.size() + " entries.";
    }

    private void printAll() {
	for (final Agency operator : alist) {
	    printOperator(operator);
	}
    }

    private void printShort() {
	for (final Agency operator : alist) {
	    System.out.println(operator.getID() + " = " + operator.getNAME());
	}
    }

    private void printMatchingOperators(final String name) {
	for (final Agency operator : matchAgenciesByName(name)) {
	    printOperator(operator);
	}
    }

    private void printOperator(final String name) {
	System.out.println(amap.get(name));
    }

    private void printOperator(final Agency operator) {
	System.out.println(operator);
    }

    private static void usage() {
	System.err.println("Usage: ReadAgency [-s|-n name|-o operator_id] -d|-f file");
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
	 * for the mandatory filename.
	 */
	int i = 0;
	while (i < args.length) {
	    if ("-d".equals(args[i])) {
		if (i + 1 < args.length) {
		    final File ftest = new File(args[i + 1], AGENCY_FILE);
		    if (!ftest.exists()) {
			usage("file doesn't exist");
		    }
		    fname = ftest.getPath();
		} else {
		    usage();
		}
	    }
	    if ("-f".equals(args[i])) {
		if (i + 1 < args.length) {
		    fname = args[i + 1];
		    final File ftest = new File(fname);
		    if (!ftest.exists()) {
			usage("file doesn't exist");
		    }
		} else {
		    usage();
		}
	    }
	    i++;
	}
	if (fname == null) {
	    usage("missing filename");
	}
	final ReadAgency reader = new ReadAgency(fname);

	/*
	 * In the second pass we look for what to print. Skip the -f/-d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -s short list, id + name
	 * -o operator, by id
	 * -n operator, by substring match on name
	 */
	i = 0;
	while (i < args.length) {
	    if ("-o".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing operator");
		    }
		    reader.printOperator(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing operator");
		}
	    } else if ("-n".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing name");
		    }
		    reader.printMatchingOperators(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing name");
		}
	    } else if ("-s".equals(args[i])) {
		reader.printShort();
		havewhat = true;
	    } else if ("-d".equals(args[i])) {
		i++;
	    } else if ("-f".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (!havewhat) {
	    reader.printAll();
	}
    }
}
