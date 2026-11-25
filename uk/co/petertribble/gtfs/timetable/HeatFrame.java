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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * The main driver frame for Heat Maps.
 */
public final class HeatFrame extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    /**
     * Menu button to exit the application.
     */
    private JMenuItem exitItem;

    /**
     * Display a new HeatFrame instance of the given size. It's a grid
     * of squares (cells) with potentially a gap between cells.
     * The values array is assumed to be square
     *
     * @param nboardSize the size of the board
     * @param ncellSize the size of each cell
     * @param cellGap the gap left between cells
     * @param nvalues the data to display
     */
    public HeatFrame(final int nboardSize, final int ncellSize,
		    final int cellGap, final int[][] nvalues) {
	super("HeatFrame");

	addWindowListener(new WindowExit());

	setContentPane(new HeatMap(nboardSize, ncellSize, cellGap, nvalues));

	final JMenu jmf = new JMenu("File");
	exitItem = new JMenuItem("Exit", KeyEvent.VK_X);
	exitItem.addActionListener(this);
	jmf.add(exitItem);

	final JMenuBar jmb = new JMenuBar();
	jmb.add(jmf);
	setJMenuBar(jmb);

	pack();
	setVisible(true);
    }

    class WindowExit extends WindowAdapter {
	@Override
	public void windowClosing(final WindowEvent wev) {
	    System.exit(0);
	}
    }

    @Override
    public void actionPerformed(final ActionEvent aev) {
	if (aev.getSource() == exitItem) {
	    System.exit(0);
	}
    }
}
