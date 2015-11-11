package main.geneticAlgorithm;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ExperimentalSpectrum {

	protected double mass[];
	protected int intensities[];
	private int intensityThreshold;

	public ExperimentalSpectrum(String spectrumFile) {
		// FIXME implement a better spectrum parser here
		parseInput(spectrumFile);
	}

	public void parseInput(String fileName) {
		List<Double> massPeaks = new ArrayList<Double>();
		List<Integer> intensities = new ArrayList<Integer>();
		URL url = ExperimentalSpectrum.class.getClassLoader().getResource(
				fileName);
		try {
			File file = new File(url.toURI());
			Scanner scan = new Scanner(file);
			scan.nextLine();
			scan.nextLine();
			scan.nextLine();
			while (scan.hasNext()) {
				String line = scan.nextLine();
				String[] columns = line.split("\\s+");
				if (columns.length == 2) {
					try {
						Integer intensity = new Integer(columns[1]);
						if (intensity >= intensityThreshold) {
							massPeaks.add(new Double(columns[0]));
							intensities.add(intensity);
						}
					} catch (Exception e) {
						//ignore
					}
				}
			}
			scan.close();
			Double[] peaks = massPeaks.toArray(new Double[] {});
			mass = new double[peaks.length];
			for(int i = 0; i < peaks.length; i++){
				mass[i] = peaks[i];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public double[] getMass() {
		return mass;
	}

	public void setMass(double mass[]) {
		this.mass = mass;
	}

	public int[] getIntensities() {
		return intensities;
	}

	public void setIntensities(int intensities[]) {
		this.intensities = intensities;
	}

	public int getIntensityThreshold() {
		return intensityThreshold;
	}

	public void setIntensityThreshold(int intensityThreshold) {
		this.intensityThreshold = intensityThreshold;
	}

}
