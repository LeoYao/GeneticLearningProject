package main;

import static org.junit.Assert.*;
import org.junit.Test;

public class TheoreticalSpectrumTest {

	@Test
	public void testCalculate() {
		TheoreticalSpectrum ts = new TheoreticalSpectrum(
				"MGAAASIQTTVNTLSERISSKLEQEANASAQTKCDIEIGNFY" +
				"IRQNHGCNLTVKNMCSADADAQLDAVLSAATETYSGLTPEQK" +
				"AYVPAMFTAALNIQTSVNTVVRDFENYVKQTCNSSAVVDNKL" +
				"KIQNVIIDECYGAPGSPTNLEFINTGSSKGNCAIKALMQLTT" +
				"KATTQIAPKQVAGTGVQFYMIVIGVIILAALFMYYAKRMLFT" +
				"STNDKIKLILANKENVHWTTYMDTFFRTSPMVIATTDMQN");
		ts.calculate();
		for(int i = 0; i < 3; i++){
			System.out.println(ts.b[i].getAvg());
			System.out.println(ts.bLessAmmonia[i].getAvg());
			System.out.println(ts.bLessWater[i].getAvg());
			System.out.println(ts.bLessBoth[i].getAvg());
			System.out.println(ts.y[i].getAvg());
			System.out.println(ts.yLessAmmonia[i].getAvg());
			System.out.println(ts.yLessWater[i].getAvg());
			System.out.println(ts.yLessBoth[i].getAvg());
		}
		double tolerance = 0.01;
		assertEquals(ts.b[0].getAvg(), 131.04, tolerance);
		assertEquals(ts.bLessAmmonia[0].getAvg(), 114.04, tolerance);
		assertEquals(ts.bLessWater[0].getAvg(), 113.04, tolerance);
		assertEquals(ts.bLessBoth[0].getAvg(), 96.04, tolerance);
	}

}
