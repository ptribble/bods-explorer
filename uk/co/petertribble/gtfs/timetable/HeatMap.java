/*
 * SPDX-License-Identifier: CDDL-1.0
 *
 * CDDL HEADER START
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
 * CDDL HEADER END
 *
 * Copyright 2026 Peter Tribble
 *
 */

package uk.co.petertribble.gtfs.timetable;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

/**
 * The main board for pctlife.
 */
public final class HeatMap extends JPanel {

    private static final long serialVersionUID = 1L;

    private static final int COLOR_RANGE = 256;
    private Color[] dColor = new Color[COLOR_RANGE];

    /** The current size in cells of the board. */
    private final int boardSize;
    /** The current cell size in pixels. */
    private final int cellSize;
    /** The gap left between cells as a double. */
    private final double dcellGap;

    /**
     * An array of int[] for the data to display.
     */
    private final int[][] values;

    /**
     * The min value, used to scale the brightness of colors.
     */
    private int vmin;

    /**
     * The max value, used to scale the brightness of colors.
     */
    private int vmax;

    /**
     * Construct a new HeatMap instance of the given size. It's a grid
     * of squares (cells) with potentially a gap between cells.
     * The values array is assumed to be square
     *
     * @param nboardSize the size of the board
     * @param ncellSize the size of each cell
     * @param cellGap the gap left between cells
     * @param nvalues the data to display
     */
    public HeatMap(final int nboardSize, final int ncellSize,
		    final int cellGap, final int[][] nvalues) {
	super();
	boardSize = nboardSize;
	cellSize = ncellSize;
	dcellGap = cellGap;
	setColors();
	values = nvalues;

	final Dimension dboard = new Dimension(boardSize * (cellSize + cellGap),
					boardSize * (cellSize + cellGap));
	setSize(dboard);
	setMinimumSize(dboard);
	setPreferredSize(dboard);
	setBackground(Color.BLACK.brighter());

	setvalues();
    }

    /**
     * Calculate the range of the data, then paint.
     */
    private void setvalues() {
	vmin = 0;
	vmax = 0;
	for (int i = 0; i < boardSize; i++) {
	    for (int j = 0; j < boardSize; j++) {
		if (values[i][j] > vmax) {
		    vmax = values[i][j];
		}
		if (values[i][j] < vmin) {
		    vmin = values[i][j];
		}
	    }
	}
	// System.out.println(vmin);
	// System.out.println(vmax);
	repaint();
    }

    private void setColors() {
	for (int i = 0; i < COLOR_RANGE; i++) {
	    dColor[i] = new Color(i, 0, 0);
	}

    }

    @Override
    public void paint(final Graphics g) {
	final Graphics2D gr2 = (Graphics2D) g;
	final Dimension d = getSize();
	gr2.setPaint(Color.BLACK.brighter());
	gr2.fill(new Rectangle2D.Double(0.0d, 0.0d, d.width, d.height));
	final double dww = d.width / ((double) boardSize);
	final double dhh = d.height / ((double) boardSize);
	final double ddw = cellSize * dww / (cellSize + dcellGap);
	final double ddh = cellSize * dhh / (cellSize + dcellGap);
	/*
	 * i == x, increasing to the right
	 * j == y, increasing downwards
	 * so boardSize-j-1 inverts it to put the origin bottom left
	 * instead of top left
	 */
	for (int i = 0; i < boardSize; i++) {
	    for (int j = 0; j < boardSize; j++) {
		gr2.setPaint(dColor[(COLOR_RANGE-1)*(values[i][boardSize-j-1]-vmin)/(vmax-vmin)]);
		gr2.fill(new Rectangle2D.Double(dww * i, dhh * j, ddw, ddh));
	    }
	}
    }
}
