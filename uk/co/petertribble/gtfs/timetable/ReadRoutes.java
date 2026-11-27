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

public final class ReadRoutes {

    private static final String ROUTES_FILE = "routes.txt";

    private List<GtfsRoute> rlist = new ArrayList<>();
    private Map<String, GtfsRoute> rmap = new HashMap<>();

    public ReadRoutes(final String fname) {
	this(new File(fname));
    }

    public ReadRoutes(final File f) {
	final File f1 = f.isDirectory() ? new File(f, ROUTES_FILE) : f;
	try {
	    for (final String line : Files.readAllLines(f1.toPath())) {
		final String[] pstr = line.split(",");
		if (!"route_id".equals(pstr[0])) {
		    final GtfsRoute rtemp = new GtfsRoute(pstr[0], pstr[1], pstr[2], pstr[3], pstr[4]);
		    rlist.add(rtemp);
		    rmap.put(pstr[0], rtemp);
		}
	    }
	} catch (IOException ioe) {
	}
    }

    public List<GtfsRoute> allRoutes() {
	return rlist;
    }

    /**
     * This gives a mapping of lists of routes to operators.
     *
     * @return a map of lists of routes keyed by operator id
     */
    public Map<String, List<GtfsRoute>> getOperatorMap() {
	final Map<String, List<GtfsRoute>> opmap = new HashMap<>();
	for (final GtfsRoute r : rlist) {
	    final String opid = r.getOPID();
	    final List<GtfsRoute> tlist = opmap.containsKey(opid)
		? opmap.get(opid)
		: new ArrayList<>();
	    tlist.add(r);
	    if (!opmap.containsKey(opid)) {
		opmap.put(opid, tlist);
	    }
	}
	return opmap;
    }

    public GtfsRoute getRouteById(final String id) {
	return rmap.get(id);
    }

    public String getStats() {
	return "routes has " + rlist.size() + " entries.";
    }

    private void printAll() {
	for (final GtfsRoute r : rlist) {
	    printRoute(r);
	}
    }

    private void printShort() {
	for (final GtfsRoute r : rlist) {
	    System.out.println(r.getID() + " = " + r.getShortName());
	}
    }

    private void printRoute(final String name) {
	System.out.println(rmap.get(name));
    }

    private void printRoute(final GtfsRoute r) {
	System.out.println(r);
    }

    private static void usage() {
	System.err.println("Usage: ReadRoutes [-r route|-s] -d dir");
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
		    final File ftest = new File(args[i + 1], ROUTES_FILE);
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
	final ReadRoutes reader = new ReadRoutes(fname);

	/*
	 * In the second pass we look for what to print. Skip the -d
	 * we handled earlier.
	 *
	 * Lowercase arg selects specific instances.
	 * -r just the given route
	 * -s short list, id + name
	 */
	i = 0;
	while (i < args.length) {
	    if ("-r".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing route");
		    }
		    reader.printRoute(args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing route");
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
