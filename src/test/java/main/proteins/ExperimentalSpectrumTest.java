package main.proteins;

import static org.junit.Assert.assertEquals;

import main.proteins.TheoreticalSpectrum;

import org.junit.Test;

public class ExperimentalSpectrumTest {

	@Test
	public void testCalculate() {
		TheoreticalSpectrum ts = new TheoreticalSpectrum(
				"");
		ts.calculate();
		// for(int i = 0; i < 3; i++){
		// System.out.println(ts.b[i].getAvg());
		// System.out.println(ts.bLessAmmonia[i].getAvg());
		// System.out.println(ts.bLessWater[i].getAvg());
		// System.out.println(ts.bLessBoth[i].getAvg());
		// System.out.println(ts.y[i].getAvg());
		// System.out.println(ts.yLessAmmonia[i].getAvg());
		// System.out.println(ts.yLessWater[i].getAvg());
		// System.out.println(ts.yLessBoth[i].getAvg());
		// }
		double tolerance = 0.01;
		assertEquals(ts.b[0].getAvg(), 131.04, tolerance);
		assertEquals(ts.bLessAmmonia[0].getAvg(), 114.04, tolerance);
		assertEquals(ts.bLessWater[0].getAvg(), 113.04, tolerance);
		assertEquals(ts.bLessBoth[0].getAvg(), 96.04, tolerance);
		assertEquals(ts.y[0].getAvg(), 27112.56, tolerance);
		assertEquals(ts.yLessAmmonia[0].getAvg(), 27095.56, tolerance);
		assertEquals(ts.yLessWater[0].getAvg(), 27094.56, tolerance);
		assertEquals(ts.yLessBoth[0].getAvg(), 27077.56, tolerance);
		assertEquals(ts.b[1].getAvg(), 188.06, tolerance);
		assertEquals(ts.y[1].getAvg(), 27055.54, tolerance);
		assertEquals(ts.b[2].getAvg(), 259.09, tolerance);
		assertEquals(ts.y[2].getAvg(), 26984.50, tolerance);
	}

	@Test
	public void testScoreAllPeaks() {
		TheoreticalSpectrum ts = new TheoreticalSpectrum("MGAAASIQ");
		ts.calculate();
//		for (int i = 0; i < 7; i++) {
//			System.out.println(ts.b[i].getAvg());
//			System.out.println(ts.bLessAmmonia[i].getAvg());
//			System.out.println(ts.bLessWater[i].getAvg());
//			System.out.println(ts.bLessBoth[i].getAvg());
//			System.out.println(ts.y[i].getAvg());
//			System.out.println(ts.yLessAmmonia[i].getAvg());
//			System.out.println(ts.yLessWater[i].getAvg());
//			System.out.println(ts.yLessBoth[i].getAvg());
//		}
		double tolerance = 0.0001;
		
		double[] peaks5 = { 3.18, 83.99, 117.17, 175.11, 225.53, 333.22, 402.09 };
		assertEquals(ts.scoreAllPeaks(peaks5), 0.8336, tolerance);
		
		double[] peaks4 = { 83.99, 117.17, 175.11, 225.53, 333.22, 402.09 };
		assertEquals(ts.scoreAllPeaks(peaks4), 0.9725, tolerance);
		
		double[] peaks3 = { 117.17, 175.11, 225.53, 333.22, 402.09 };
		assertEquals(ts.scoreAllPeaks(peaks3), 0.9865, tolerance);
		
		double[] peaks = { 114.15, 171.99, 224.52, 331.74, 400.69 };
		assertEquals(ts.scoreAllPeaks(peaks), 0.99718, tolerance);
		
		double[] peaks2 = { 114.05, 171.2, 224.52, 330.74, 401.19 };
		assertEquals(ts.scoreAllPeaks(peaks2), 0.99911, tolerance);
	}
	
	@Test
	public void testScoreSinglePeak() {
		TheoreticalSpectrum ts = new TheoreticalSpectrum("MGAAASIQ");
		ts.calculate();
		double tolerance = 0.0001;
		
		double[] peaks = { 114.05, 171.2, 224.52, 330.74, 401.19 };
		assertEquals(ts.scoreSingleExperimentalPeak(peaks, 2), 0.9983, tolerance);
	}

}
