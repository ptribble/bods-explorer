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

import uk.co.petertribble.gtfs.util.SimplePoint;

public final class MapStops {

    private ReadStops stopReader;

    public MapStops(final String fname) {
	this(new File(fname));
    }

    public MapStops(final File f) {
	stopReader = new ReadStops(f);
    }

    private void printAll() {
	for (final GtfsStop s : stopReader.allStops()) {
	    printStop(s);
	}
    }

    /*
     * Print out all the bus stops inside the given box.
     */
    private void printBox(final String sy1, final String sy2,
			  final String sx1, final String sx2) {
	/*
	 * We need the actual positions of the 4 edges, we can get this
	 * by looking at two opposite corners.
	 */
	final SimplePoint sp1 = new SimplePoint("dummy1", sy1, sx1);
	final SimplePoint sp2 = new SimplePoint("dummy2", sy2, sx2);
	final double dy1 = sp1.getY();
	final double dy2 = sp2.getY();
	final double dx1 = sp1.getX();
	final double dx2 = sp2.getX();
	for (final GtfsStop s : stopReader.allStops()) {
	    final SimplePoint sp = new SimplePoint(s.getID(), s.getLat(), s.getLon());
	    if (sp.getY() > dy1 && sp.getY() < dy2
		    && sp.getX() > dx1 && sp.getX() < dx2) {
		System.out.println(sp);
	    }
	}
    }

    /*
     * Plot the density of bus stops inside the given box as a heat map.
     */
    private void plotBox(final String sy1, final String sy2,
			  final String sx1, final String sx2) {
	/*
	 * We need the actual positions of the 4 edges, we can get this
	 * by looking at two opposite corners.
	 */
	final SimplePoint sp1 = new SimplePoint("dummy1", sy1, sx1);
	final SimplePoint sp2 = new SimplePoint("dummy2", sy2, sx2);
	final double dy1 = sp1.getY();
	final double dy2 = sp2.getY();
	final double dx1 = sp1.getX();
	final double dx2 = sp2.getX();
	// make more flexible later
	final int irange = 128;
	final int[][] values = new int[128][128];
	for (final GtfsStop s : stopReader.allStops()) {
	    final SimplePoint sp = new SimplePoint(s.getID(), s.getLat(), s.getLon());
	    if (sp.getY() > dy1 && sp.getY() < dy2
		    && sp.getX() > dx1 && sp.getX() < dx2) {
		final int ix = (int) (irange * ((sp.getX() - dx1) / (dx2 - dx1)));
		final int iy = (int) (irange * ((sp.getY() - dy1) / (dy2 - dy1)));
		// System.out.println(iy + " - " + ix);
		/*
		 * We think of [latitude,longitude]
		 * but the graph is [x,y]=[longitude,latitude]
		 */
		values[ix][iy]++;
	    }
	}
	new HeatFrame(128, 5, 1, values);
    }

    private void printStop(final GtfsStop s) {
	final SimplePoint sp =
	    new SimplePoint(s.getID(), s.getLat(), s.getLon());
	System.out.println(sp);
    }

    private static void usage() {
	System.err.println("Usage: MapStops [-x minlong -X maxlong -y minlat -Y maxlat [-G]] -d dir");
	System.exit(2);
    }

    private static void usage(final String err) {
	System.err.println("ERROR: " + err);
	usage();
    }

    public static void main(final String[] args) throws Exception {
	String fname = null;
	boolean havewhat = false;
	boolean dograph = false;

	/*
	 * The possible range of latitude(y) and longitude(x)
	 */
	String sy1 = null;
	String sy2 = null;
	String sx1 = null;
	String sx2 = null;

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
		    final File ftest = new File(args[i + 1]);
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
	final MapStops mapper = new MapStops(fname);

	/*
	 * Now we look for a range being specified.
	 * Note that the values could be negative (for anything west of
	 * greenwich or south of the equator).
	 *
	 * -x - left edge longitude
	 * -X - right edge longitude
	 * -y - lower edge latitude
	 * -Y - upper edge latitude
	 *
	 * -G - plot graph
	 */

	i = 0;
	while (i < args.length) {
	    if ("-y".equals(args[i])) {
		if (i + 1 < args.length) {
		    sy1 = args[i + 1];
		    i++;
		    havewhat = true;
		} else {
		    usage("missing ymin");
		}
	    } else if ("-Y".equals(args[i])) {
		if (i + 1 < args.length) {
		    sy2 = args[i + 1];
		    i++;
		    havewhat = true;
		} else {
		    usage("missing ymax");
		}
	    } else if ("-x".equals(args[i])) {
		if (i + 1 < args.length) {
		    sx1 = args[i + 1];
		    i++;
		    havewhat = true;
		} else {
		    usage("missing xmin");
		}
	    } else if ("-X".equals(args[i])) {
		if (i + 1 < args.length) {
		    sx2 = args[i + 1];
		    i++;
		    havewhat = true;
		} else {
		    usage("missing xmax");
		}
	    } else if ("-G".equals(args[i])) {
		dograph = true;
	    } else if ("-d".equals(args[i])) {
		i++;
	    }
	    i++;
	}
	if (havewhat) {
	    if (sy1 != null && sy2 != null & sx1 != null & sx2 != null) {
		if (dograph) {
		    mapper.plotBox(sy1, sy2, sx1, sx2);
		} else {
		    mapper.printBox(sy1, sy2, sx1, sx2);
		}
	    } else {
		usage("missing range");
	    }
	} else {
	    mapper.printAll();
	}
    }
}
