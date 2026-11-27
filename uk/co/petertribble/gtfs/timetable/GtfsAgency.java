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

/**
 * Represents a bus operator.
 */
public final class GtfsAgency {

    private final String agency_id;
    private final String agency_name;
    private final String agency_url;
    private final String agency_timezone;
    private final String agency_lang;
    private final String agency_phone;
    private final String agency_noc;

    public GtfsAgency(final String id, final String name, final String url, final String tz, final String lang, final String phone, final String noc) {
	agency_id = stripquotes(id);
	agency_name = stripquotes(name);
	agency_url = stripquotes(url);
	agency_timezone = stripquotes(tz);
	agency_lang = stripquotes(lang);
	agency_phone = stripquotes(phone);
	agency_noc = stripquotes(noc);
    }

    public String getID() {
	return agency_id;
    }

    public String getNAME() {
	return agency_name;
    }

    public String getURL() {
	return agency_url;
    }

    public String getTIMEZONE() {
	return agency_timezone;
    }

    public String getLANG() {
	return agency_lang;
    }

    public String getPHONE() {
	return agency_phone;
    }

    public String getNOC() {
	return agency_noc;
    }

    @Override
    public String toString() {
	return agency_id + "\n"
	    + "  name = " + agency_name + "\n"
	    + "  url = " + agency_url + "\n"
	    + "  timezone = " + agency_timezone + "\n"
	    + "  lang = " + agency_lang + "\n"
	    + "  phone = " + agency_phone + "\n"
	    + "  noc = " + agency_noc + "\n";
    }

    /*
     * Helper to strip quotes from the incoming strings.
     */
    private String stripquotes(final String instring) {
	return instring.replace("\"", "");
    }
}
