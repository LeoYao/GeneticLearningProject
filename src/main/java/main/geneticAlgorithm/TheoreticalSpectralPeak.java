package main.geneticAlgorithm;

public class TheoreticalSpectralPeak {
	private double min;
	private double avg;
	private double max;
	
	public TheoreticalSpectralPeak(double min, double avg, double max){
		this.setMin(min);
		this.setAvg(avg);
		this.setMax(max);
	}

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getAvg() {
		return avg;
	}

	public void setAvg(double avg) {
		this.avg = avg;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}
	
}
