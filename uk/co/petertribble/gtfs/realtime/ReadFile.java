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

package uk.co.petertribble.gtfs.realtime;

import java.io.File;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import com.google.transit.realtime.GtfsRealtime.FeedEntity;
import com.google.transit.realtime.GtfsRealtime.FeedMessage;

/**
 * Print out the contents of a gtfs-rt file.
 */
public final class ReadFile {

    private static final String RT_FILE = "gtfsrt.bin";

    private static void printAll(final FeedMessage feed) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    System.out.println(entity);
	}
    }

   private static void printAlert(final FeedMessage feed) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    if (entity.hasAlert()) {
		System.out.println(entity.getAlert());
	    }
	}
    }

    private static void printTripModifications(final FeedMessage feed) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    if (entity.hasTripModifications()) {
		System.out.println(entity.getTripModifications());
	    }
	}
    }

    private static void printTripUpdates(final FeedMessage feed) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    if (entity.hasTripUpdate()) {
		System.out.println(entity.getTripUpdate());
	    }
	}
    }

   private static void printVehicles(final FeedMessage feed) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    if (entity.hasVehicle()) {
		System.out.println(entity.getVehicle());
	    }
	}
    }

    private static void printVehicles(final FeedMessage feed,
				      final String route) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    if (entity.hasVehicle()) {
		if (entity.getVehicle().hasTrip()) {
		    if (entity.getVehicle().getTrip().hasRouteId()) {
			if (entity.getVehicle().getTrip().getRouteId().equals(route)) {
			    System.out.println(entity.getVehicle());
			}
		    }
		}
	    }
	}
    }

    private static void printOneVehicle(final FeedMessage feed,
					final String id) {
	for (final FeedEntity entity : feed.getEntityList()) {
	    if (entity.hasVehicle()) {
		if (entity.getVehicle().hasVehicle()) {
		    if (entity.getVehicle().getVehicle().hasId()) {
			if (entity.getVehicle().getVehicle().getId().equals(id)) {
			    System.out.println(entity.getVehicle());
			}
		    }
		}
	    }
	}
    }

    private static void usage() {
	System.err.println("Usage: ReadFile [-V|-A|-U|-M|-r route|-v id] -d|-f|-z file");
	System.exit(2);
    }

    private static void usage(final String err) {
	System.err.println("ERROR: " + err);
	usage();
    }

    public static void main(final String[] args) throws Exception {
	String fname = null;
	boolean havewhat = false;
	ZipEntry zentry = null;
	ZipFile zfile = null;
	/*
	 * Two passes over the arguments. In the first pass we just look
	 * for the mandatory filename.
	 *
	 * If given -f, that's a .bin file
	 * If given -d, look for the standard .bin in that directory
	 * If given -z, look for the standard .bin inside that zipfile
	 */
	int i = 0;
	while (i < args.length) {
	    if ("-help".equals(args[i]) || "--help".equals(args[i])) {
		usage();
	    }
	    if ("-d".equals(args[i])) {
		if (i + 1 < args.length) {
		    final File ftest = new File(args[i + 1], RT_FILE);
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
	    if ("-z".equals(args[i])) {
		if (i + 1 < args.length) {
		    fname = args[i + 1];
		    final File ftest = new File(fname);
		    if (!ftest.exists()) {
			usage("zipfile doesn't exist");
		    }
		    try {
			zfile = new ZipFile(ftest);
			zentry = zfile.getEntry(RT_FILE);
			if (zentry == null) {
			    usage("invalid zipfile");
			}
		    } catch (ZipException ze) {
			usage("malformed zipfile");
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
	final URL url = new URL("file:///" + fname);
	final FeedMessage feed = zentry == null
	    ? FeedMessage.parseFrom(url.openStream())
	    : FeedMessage.parseFrom(zfile.getInputStream(zentry));
	/*
	 * In the second pass we look for what to print. Skip the -f/-d/-z
	 * we handled earlier.
	 *
	 * Uppercase arg selects a type.
	 * -A Alerts
	 * -M Trip Modifications
	 * -U Trip Updates
	 * -V Vehicles
	 * Lowercase arg selects specific instances.
	 * -r routeid
	 * -v vehicle id
	 */
	i = 0;
	while (i < args.length) {
	    if ("-A".equals(args[i])) {
		printAlert(feed);
		havewhat = true;
	    } else if ("-M".equals(args[i])) {
		printTripModifications(feed);
		havewhat = true;
	    } else if ("-U".equals(args[i])) {
		printTripUpdates(feed);
		havewhat = true;
	    } else if ("-V".equals(args[i])) {
		printVehicles(feed);
		havewhat = true;
	    } else if ("-r".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing route");
		    }
		    printVehicles(feed, args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing route");
		}
	    } else if ("-v".equals(args[i])) {
		if (i + 1 < args.length) {
		    if (args[i + 1].startsWith("-")) {
			usage("missing id");
		    }
		    printOneVehicle(feed, args[i + 1]);
		    i++;
		    havewhat = true;
		} else {
		    usage("missing id");
		}
	    } else if ("-d".equals(args[i])) {
		i++;
	    } else if ("-f".equals(args[i])) {
		i++;
	    } else if ("-z".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (!havewhat) {
	    printAll(feed);
	}
    }
}
