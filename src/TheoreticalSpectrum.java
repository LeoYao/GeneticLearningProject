
public class TheoreticalSpectrum {
	private String seq;
	private double b[];
	private double bLessWater[];
	private double bLessAmmonia[];
	private double bLessBoth[];
	
	private double y[];
	private double yLessWater[];
	private double yLessAmmonia[];
	private double yLessBoth[];
	
	public TheoreticalSpectrum(String seq){
		this.seq = seq;
		
		b = new double[seq.length()];
		bLessWater = new double[seq.length()];
		bLessAmmonia = new double[seq.length()];
		bLessBoth = new double[seq.length()];
		
		y = new double[seq.length()];
		yLessWater = new double[seq.length()];
		yLessAmmonia = new double[seq.length()];
		yLessBoth = new double[seq.length()];
		for(int i = 0; i < seq.length(); i++){
			b[i] = 0.0;
			bLessWater[i] = 0.0;
			bLessAmmonia[i] = 0.0;
			bLessBoth[i] = 0.0;
			
			y[i] = 0.0;
			yLessWater[i] = 0.0;
			yLessAmmonia[i] = 0.0;
			yLessBoth[i] = 0.0;
		}
		
	}
	
	public void calculate(){
		
	}

}
