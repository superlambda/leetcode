package com.wuerth.phoenix.basic.etnax.utilities.sap.export.test;

import java.text.DecimalFormat;

import junit.framework.TestCase;

public class FormatTest extends TestCase {
	public void testWeightFormat() {
		DecimalFormat weightFormat = new DecimalFormat();
		weightFormat.setMaximumFractionDigits(3);
		weightFormat.setMinimumFractionDigits(3);
		assertEquals("10.302", weightFormat.format(10.3024));
		assertEquals("10.320", weightFormat.format(10.32));
		assertEquals("10.303", weightFormat.format(10.3025));
	}

	public static void main(String[] args) {
		junit.textui.TestRunner.run(FormatTest.class);
	}
}
