package com.palscash.common.crypto;

import junit.framework.TestCase;

public class TestCurves extends TestCase {

	public void test() {

		new Curves();
		
		assertEquals(97, Curves.getAvailableCurves().size());
		assertEquals("prime239v1", Curves.getCurveName("23"));
		assertEquals("c2pnb272w1", Curves.getCurveName("1"));
		
		assertEquals("prime239v1", Curves.getCurveName(23));
		assertEquals("c2pnb272w1", Curves.getCurveName(1));

		assertEquals(23, Curves.getCurveIndex("prime239v1"));
		assertEquals(1, Curves.getCurveIndex("c2pnb272w1"));
		assertEquals(-1, Curves.getCurveIndex("curve"));
		
		assertEquals("xx1", Curves.getCurveIndexAsReadable("c2pnb272w1"));
		assertEquals("x23", Curves.getCurveIndexAsReadable("prime239v1"));
		
	}

}