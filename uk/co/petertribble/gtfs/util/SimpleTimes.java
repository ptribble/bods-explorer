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
 * A utility to handle times as string.
 */
public final class SimpleTimes {

    /**
     * The number of seconds in an hour.
     */
    private static final int SECPERHOUR = 3600;

    /**
     * The number of seconds in a minute.
     */
    private static final int SECPERMIN = 60;

    /**
     * Convert a time written in the form HH:MM or HH:MM:SS to an int
     * that is the number of seconds since the start of day.
     *
     * This method does not bound the values to be within the normal
     * range. In particular, HH could be over 24 to represent the
     * following day.
     */
    public static int toSeconds(String s) {
	int secs = -1;
	String[] scomps = s.split(":");
	if (scomps.length == 2) {
	    secs = SECPERHOUR * Integer.parseInt(scomps[0])
		+ SECPERMIN * Integer.parseInt(scomps[1]);
	} else if (scomps.length == 3) {
	    secs = SECPERHOUR * Integer.parseInt(scomps[0])
		+ SECPERMIN * Integer.parseInt(scomps[1])
		+ Integer.parseInt(scomps[2]);
	}
	return secs;
    }
}
