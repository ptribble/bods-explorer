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

package uk.co.petertribble.gtfs.util;

/**
 * A simple representation of a point defined by latitude and longitude.
 */
public final class SimplePoint {

    /**
     * The conversion factor between degrees and metres, assuming the
     * earth's radius to be 6371 km.
     */
    private static final double MSCALE = 1000.0 * 6371 * Math.PI / 180.0;

    /**
     * The conversion factor from degrees to radians.
     */
    private static final double RSCALE = Math.PI / 180.0;

    /**
     * An arbitrary id to identify this SimplePoint.
     */
    private String id;

    /**
     * The latitude of this SimplePoint.
     */
    private double latitude;

    /**
     * The longitude of this SimplePoint.
     */
    private double longitude;

    /**
     * Create a SimplePoint from the given id, latitude, and longitude.
     *
     * @param sid the textual id
     * @param slat the latitude as a string
     * @param slon the longitude as a string
     */
    public SimplePoint(final String sid, final String slat, final String slon) {
	this(sid, Double.parseDouble(slat), Double.parseDouble(slon));
    }

    /**
     * Create a SimplePoint from the given id, latitude, and longitude.
     *
     * @param nid the textual id
     * @param nlat the latitude as a double
     * @param nlon the longitude as a double
     */
    public SimplePoint(final String nid, final double nlat, final double nlon) {
	id = nid;
	latitude = nlat;
	longitude = nlon;
    }

    /**
     * Get the id of this SimplePoint.
     *
     * @return the id of this SimplePoint
     */
    public String getID() {
	return id;
    }

    /**
     * Get the x coordinate.
     *
     * @return the x coordinate
     */
    public double getX() {
	return MSCALE * Math.cos(RSCALE * latitude) * longitude;
    }

    /**
     * Get the y coordinate.
     *
     * @return the y coordinate
     */
    public double getY() {
	return MSCALE * latitude;
    }

    /**
     * Calculate the distance, in metres, betwnn this SimplePoint and
     * another SimplePoint.
     *
     * @param sp another SimplePoint to measure the distance from.
     *
     * @return the distance between the 2 points in metres.
     */
    public double distanceFrom(final SimplePoint sp) {
	return Math.sqrt((getX() - sp.getX()) * (getX() - sp.getX())
			 + (getY() - sp.getY()) * (getY() - sp.getY()));
    }

    @Override
    public String toString() {
	return "[" + latitude + "," + longitude + "]=["
	    + getY() + "," + getX() + "]";
    }
}
