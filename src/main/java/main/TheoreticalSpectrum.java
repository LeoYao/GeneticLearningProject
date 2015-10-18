package main;

public class TheoreticalSpectrum {
	protected static final double WATER_MASS = 18.0;
	protected static final double AMONIA_MASS = 17.0;

	protected String seq;
	protected TheoreticalSpectralPeak b[];
	protected TheoreticalSpectralPeak bLessWater[];
	protected TheoreticalSpectralPeak bLessAmmonia[];
	protected TheoreticalSpectralPeak bLessBoth[];

	protected TheoreticalSpectralPeak y[];
	protected TheoreticalSpectralPeak yLessWater[];
	protected TheoreticalSpectralPeak yLessAmmonia[];
	protected TheoreticalSpectralPeak yLessBoth[];

	public TheoreticalSpectrum(String seq) {
		this.seq = seq;

		b = new TheoreticalSpectralPeak[seq.length() - 2];
		bLessWater = new TheoreticalSpectralPeak[seq.length() - 2];
		bLessAmmonia = new TheoreticalSpectralPeak[seq.length() - 2];
		bLessBoth = new TheoreticalSpectralPeak[seq.length() - 2];

		y = new TheoreticalSpectralPeak[seq.length() - 2];
		yLessWater = new TheoreticalSpectralPeak[seq.length() - 2];
		yLessAmmonia = new TheoreticalSpectralPeak[seq.length() - 2];
		yLessBoth = new TheoreticalSpectralPeak[seq.length() - 2];
	}

	public void calculate() {
		for (int i = 1; i < (seq.length() - 1); i++) {
			char[] bString = seq.substring(0, i).toCharArray();
			char[] yString = seq.substring(i + 1, seq.length() - 1)
					.toCharArray();
			b[i - 1] = new TheoreticalSpectralPeak(0.0, getAvgMass(bString),
					0.0);
			bLessWater[i - 1] = new TheoreticalSpectralPeak(0.0,
					b[i - 1].getAvg() - WATER_MASS, 0.0);
			bLessAmmonia[i - 1] = new TheoreticalSpectralPeak(0.0,
					b[i - 1].getAvg() - AMONIA_MASS, 0.0);
			bLessBoth[i - 1] = new TheoreticalSpectralPeak(0.0,
					b[i - 1].getAvg() - WATER_MASS - AMONIA_MASS, 0.0);

			y[i - 1] = new TheoreticalSpectralPeak(0.0, getAvgMass(yString),
					0.0);
			yLessWater[i - 1] = new TheoreticalSpectralPeak(0.0,
					y[i - 1].getAvg() - WATER_MASS, 0.0);
			yLessAmmonia[i - 1] = new TheoreticalSpectralPeak(0.0,
					y[i - 1].getAvg() - AMONIA_MASS, 0.0);
			yLessBoth[i - 1] = new TheoreticalSpectralPeak(0.0,
					y[i - 1].getAvg() - WATER_MASS - AMONIA_MASS, 0.0);
		}
	}

	public double getAvgMass(char[] protBuff) {
		double mass = 0.0;
		for (int i = 0; i < protBuff.length; i++) {
			if (protBuff[i] == 'A')
				mass += 71.03711;
			else if (protBuff[i] == 'C')
				mass += 103.00919;
			else if (protBuff[i] == 'D')
				mass += 115.02694;
			else if (protBuff[i] == 'E')
				mass += 129.04259;
			else if (protBuff[i] == 'F')
				mass += 147.06841;
			else if (protBuff[i] == 'G')
				mass += 57.02146;
			else if (protBuff[i] == 'H')
				mass += 137.05891;
			else if (protBuff[i] == 'I')
				mass += 113.08406;
			else if (protBuff[i] == 'K')
				mass += 128.09496;
			else if (protBuff[i] == 'L')
				mass += 113.08406;
			else if (protBuff[i] == 'M')
				mass += 131.04049;
			else if (protBuff[i] == 'N')
				mass += 114.04293;
			else if (protBuff[i] == 'P')
				mass += 97.05276;
			else if (protBuff[i] == 'Q')
				mass += 128.05858;
			else if (protBuff[i] == 'R')
				mass += 156.10111;
			else if (protBuff[i] == 'S')
				mass += 87.03203;
			else if (protBuff[i] == 'T')
				mass += 101.04786;
			else if (protBuff[i] == 'V')
				mass += 99.06841;
			else if (protBuff[i] == 'W')
				mass += 186.07931;
			else if (protBuff[i] == 'Y')
				mass += 163.06333;
		}
		return mass;
	}

	public double findClosestMatchingPeak(double experimentalPeak) {
		double tolerance = 10.0;
		double closestMatch = 0.0;
		double distance = experimentalPeak - closestMatch;
		for (int i = 1; i < (seq.length() - 1); i++) {

			if (Math.abs(b[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = b[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(bLessWater[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = bLessWater[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(bLessAmmonia[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = bLessAmmonia[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(bLessBoth[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = bLessBoth[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(y[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = y[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(yLessWater[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = yLessWater[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(yLessAmmonia[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = yLessAmmonia[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}

			if (Math.abs(yLessBoth[i - 1].getAvg() - experimentalPeak) < distance) {
				closestMatch = yLessBoth[i - 1].getAvg();
				distance = experimentalPeak - closestMatch;
			}
		}
		return closestMatch;
	}

	public double scoreSinglePeak(double[] peaks, int scorePeakIndex) {
		double closestPeak = findClosestMatchingPeak(peaks[scorePeakIndex]);
		return closestPeak / peaks[scorePeakIndex];
	}

	public double scoreAllPeaks(double[] peaks) {
		double sumOfPeakScores = 0.0;
		for(int i = 0; i < peaks.length; i++){
			sumOfPeakScores += scoreSinglePeak(peaks, i);
		}
		return sumOfPeakScores / ((double)peaks.length);
	}

}