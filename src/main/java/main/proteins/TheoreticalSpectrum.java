package main.proteins;

public class TheoreticalSpectrum {
	private static final double PEAK_DISTANCE_THRESHOLD = 1.0;
	protected static final double WATER_MASS = 18.0;
	protected static final double AMONIA_MASS = 17.0;

	public enum PeakType {
		NOT_USED, NORMAL, LESS_WATER, LESS_AMMONIA, LESS_BOTH
	}

	protected String seq;
	protected TheoreticalSpectralPeak b[];
	protected TheoreticalSpectralPeak bLessWater[];
	protected TheoreticalSpectralPeak bLessAmmonia[];
	protected TheoreticalSpectralPeak bLessBoth[];

	protected TheoreticalSpectralPeak y[];
	protected TheoreticalSpectralPeak yLessWater[];
	protected TheoreticalSpectralPeak yLessAmmonia[];
	protected TheoreticalSpectralPeak yLessBoth[];

	protected boolean bUsed[];
	protected boolean bLessWaterUsed[];
	protected boolean bLessAmmoniaUsed[];
	protected boolean bLessBothUsed[];

	protected boolean yUsed[];
	protected boolean yLessWaterUsed[];
	protected boolean yLessAmmoniaUsed[];
	protected boolean yLessBothUsed[];

	public TheoreticalSpectrum(String seq) {
		this.seq = seq;

		b = new TheoreticalSpectralPeak[seq.length() - 1];
		bLessWater = new TheoreticalSpectralPeak[seq.length() - 1];
		bLessAmmonia = new TheoreticalSpectralPeak[seq.length() - 1];
		bLessBoth = new TheoreticalSpectralPeak[seq.length() - 1];

		y = new TheoreticalSpectralPeak[seq.length() - 1];
		yLessWater = new TheoreticalSpectralPeak[seq.length() - 1];
		yLessAmmonia = new TheoreticalSpectralPeak[seq.length() - 1];
		yLessBoth = new TheoreticalSpectralPeak[seq.length() - 1];

		bUsed = new boolean[seq.length() - 1];
		bLessWaterUsed = new boolean[seq.length() - 1];
		bLessAmmoniaUsed = new boolean[seq.length() - 1];
		bLessBothUsed = new boolean[seq.length() - 1];

		yUsed = new boolean[seq.length() - 1];
		yLessWaterUsed = new boolean[seq.length() - 1];
		yLessAmmoniaUsed = new boolean[seq.length() - 1];
		yLessBothUsed = new boolean[seq.length() - 1];

		resetUsed(seq);
	}

	private void resetUsed(String seq) {
		for (int i = 0; i < seq.length() - 1; i++) {
			bUsed[i] = false;
			bLessWaterUsed[i] = false;
			bLessAmmoniaUsed[i] = false;
			bLessBothUsed[i] = false;

			yUsed[i] = false;
			yLessWaterUsed[i] = false;
			yLessAmmoniaUsed[i] = false;
			yLessBothUsed[i] = false;
		}
	}

	public void calculate() {
		for (int i = 1; i < seq.length(); i++) {
			char[] bString = seq.substring(0, i).toCharArray();
			char[] yString = seq.substring(i, seq.length()).toCharArray();
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

	public double findClosestTheoreticalPeak(double experimentalPeak) {
		double tolerance = 10.0;
		double closestMatch = 0.0;
		double distance = experimentalPeak - closestMatch;
		boolean closestPeakB = false;
		PeakType closestPeakTypeUsed = PeakType.NOT_USED;
		int closestPeakUsedI = -1;

		for (int i = 0; i < seq.length() - 1; i++) {

			if (Math.abs(b[i].getAvg() - experimentalPeak) < distance
					&& bUsed[i] == false) {
				closestMatch = b[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = true;
				closestPeakTypeUsed = PeakType.NORMAL;
				closestPeakUsedI = i;
			}

			if (Math.abs(bLessWater[i].getAvg() - experimentalPeak) < distance
					&& bLessWaterUsed[i] == false) {
				closestMatch = bLessWater[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = true;
				closestPeakTypeUsed = PeakType.LESS_WATER;
				closestPeakUsedI = i;
			}

			if (Math.abs(bLessAmmonia[i].getAvg() - experimentalPeak) < distance
					&& bUsed[i] == false) {
				closestMatch = bLessAmmonia[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = true;
				closestPeakTypeUsed = PeakType.LESS_AMMONIA;
				closestPeakUsedI = i;
			}

			if (Math.abs(bLessBoth[i].getAvg() - experimentalPeak) < distance
					&& bUsed[i] == false) {
				closestMatch = bLessBoth[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = true;
				closestPeakTypeUsed = PeakType.LESS_BOTH;
				closestPeakUsedI = i;
			}

			if (Math.abs(y[i].getAvg() - experimentalPeak) < distance
					&& yUsed[i] == false) {
				closestMatch = y[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = false;
				closestPeakTypeUsed = PeakType.NORMAL;
				closestPeakUsedI = i;
			}

			if (Math.abs(yLessWater[i].getAvg() - experimentalPeak) < distance
					&& yLessWaterUsed[i] == false) {
				closestMatch = yLessWater[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = false;
				closestPeakTypeUsed = PeakType.LESS_WATER;
				closestPeakUsedI = i;
			}

			if (Math.abs(yLessAmmonia[i].getAvg() - experimentalPeak) < distance
					&& yLessAmmoniaUsed[i] == false) {
				closestMatch = yLessAmmonia[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = false;
				closestPeakTypeUsed = PeakType.LESS_AMMONIA;
				closestPeakUsedI = i;
			}

			if (Math.abs(yLessBoth[i].getAvg() - experimentalPeak) < distance
					&& yLessBothUsed[i] == false) {
				closestMatch = yLessBoth[i].getAvg();
				distance = Math.abs(experimentalPeak - closestMatch);
				closestPeakB = false;
				closestPeakTypeUsed = PeakType.LESS_BOTH;
				closestPeakUsedI = i;
			}
		}

		if (closestPeakUsedI != -1) {
			if (Math.abs(experimentalPeak - closestMatch) <= PEAK_DISTANCE_THRESHOLD) {
				if (closestPeakB) {
					if (closestPeakTypeUsed == PeakType.NORMAL)
						bUsed[closestPeakUsedI] = true;
					else if (closestPeakTypeUsed == PeakType.LESS_AMMONIA)
						bLessAmmoniaUsed[closestPeakUsedI] = true;
					else if (closestPeakTypeUsed == PeakType.LESS_WATER)
						bLessWaterUsed[closestPeakUsedI] = true;
					else if (closestPeakTypeUsed == PeakType.LESS_BOTH)
						bLessBothUsed[closestPeakUsedI] = true;
				} else {
					if (closestPeakTypeUsed == PeakType.NORMAL)
						yUsed[closestPeakUsedI] = true;
					else if (closestPeakTypeUsed == PeakType.LESS_AMMONIA)
						yLessAmmoniaUsed[closestPeakUsedI] = true;
					else if (closestPeakTypeUsed == PeakType.LESS_WATER)
						yLessWaterUsed[closestPeakUsedI] = true;
					else if (closestPeakTypeUsed == PeakType.LESS_BOTH)
						yLessBothUsed[closestPeakUsedI] = true;
				}
				return closestMatch;
			}
		}

		return 0.0;
	}

	public double scoreSingleExperimentalPeak(double[] peaks, int scorePeakIndex) {
		double closestPeak = findClosestTheoreticalPeak(peaks[scorePeakIndex]);
		if (closestPeak < peaks[scorePeakIndex])
			return closestPeak / peaks[scorePeakIndex];
		else if (closestPeak > peaks[scorePeakIndex])
			return peaks[scorePeakIndex] / closestPeak;
		else
			return 1.0;
	}

	public double scoreAllPeaks(double[] peaks) {

		int theoreticalPeaksUsed = 0;
		for (int i = 0; i < seq.length() - 1; i++) {
			// check b peaks			
			if (bUsed[i] == true)
				theoreticalPeaksUsed++;
			if (bLessAmmoniaUsed[i] == true)
				theoreticalPeaksUsed++;
			if (bLessWaterUsed[i] == true)
				theoreticalPeaksUsed++;
			if (bLessBothUsed[i] == true)
				theoreticalPeaksUsed++;
			// check y peaks
			if (yUsed[i] == true)
				theoreticalPeaksUsed++;
			if (yLessAmmoniaUsed[i] == true)
				theoreticalPeaksUsed++;
			if (yLessWaterUsed[i] == true)
				theoreticalPeaksUsed++;
			if (yLessBothUsed[i] == true)
				theoreticalPeaksUsed++;
		}
		int numTheoreticalPeaks = (seq.length() - 1) * 8;
		double theoreticalMatches = ((double) theoreticalPeaksUsed)
				/ ((double) numTheoreticalPeaks);
		
		resetUsed(seq);
		double sumOfPeakScores = 0.0;
		for (int i = 0; i < peaks.length; i++) {
			double score = scoreSingleExperimentalPeak(peaks, i);
			sumOfPeakScores += score;
		}
		double experimentalMatches = sumOfPeakScores / ((double) peaks.length);

		return ((0.25)*experimentalMatches + (0.75)*theoreticalMatches);

	}
	
	public double getParentMass(){
		return getAvgMass(seq.toCharArray());
	}

}
