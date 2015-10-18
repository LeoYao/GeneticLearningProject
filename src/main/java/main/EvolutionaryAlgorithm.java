package main;

import java.util.List;


public class EvolutionaryAlgorithm {
	private List<Protein> database;
	private Protein[] population;
	private double[] fitness;
	private int populationSize;
	
	
	public EvolutionaryAlgorithm(){
		populationSize = 50;
		population = new Protein[populationSize];
		fitness  = new double[populationSize];		
//		for(int i = 0; i < populationSize; i++){
//			//population[i] = new Protein(new char[]{'G','S','F','D','A'});
//			population[i] = new Protein(new char[]{'A','S','A','F','A'});
//			fitness[i] = 0.0;
//		}
//		population[0] = new Protein(new char[]{'G','S','F','D','A'});
	}
	
	public static void main(String[] args){
		EvolutionaryAlgorithm ident = new EvolutionaryAlgorithm();
		System.out.println(ident.protMass(new char[]{'G','P','F','N','A'}));
//		SpectralData data = new SpectralData(486.22267);
//		ident.protIdentification(data);
	}
	
	public void protIdentification(TheoreticalSpectralPeak data){
		int generations = 10;
		for(int generation = 0; generation < generations; generation++){
			for(int popMember = 0; popMember < populationSize; popMember++){
				calculateFitness(data, popMember);				
			}
			
			double avgFitness = 0.0;
			for(int popMember = 0; popMember < populationSize; popMember++){
				avgFitness += fitness[popMember];
			}
			avgFitness /= ((double)populationSize);
			Protein[] survivors = new Protein[populationSize];
			int onSurvivor = 0;
			for(int popMember = 0; popMember < populationSize; popMember++){
				if(fitness[popMember] > avgFitness){
					survivors[onSurvivor] = population[popMember];
				}
			}
		}
	}

	private void calculateFitness(TheoreticalSpectralPeak data, int popMember) {
//		fitness[popMember] = Math.abs(protMass(population[popMember].get) - data.getAvg());
//		System.out.println("Fitness: " + fitness[popMember]);
	}
	
	public double protMass(char[] protBuff){
		double mass = 0.0;
		for(int i = 0; i < protBuff.length; i++){
			if(protBuff[i] == 'A') mass += 71.03711;
			else if(protBuff[i] == 'C') mass += 103.00919;
			else if(protBuff[i] == 'D') mass += 115.02694;
			else if(protBuff[i] == 'E') mass += 129.04259;
			else if(protBuff[i] == 'F') mass += 147.06841;
			else if(protBuff[i] == 'G') mass += 57.02146;
			else if(protBuff[i] == 'H') mass += 137.05891;
			else if(protBuff[i] == 'I') mass += 113.08406;
			else if(protBuff[i] == 'K') mass += 128.09496;
			else if(protBuff[i] == 'L') mass += 113.08406;
			else if(protBuff[i] == 'M') mass += 131.04049;
			else if(protBuff[i] == 'N') mass += 114.04293;
			else if(protBuff[i] == 'P') mass += 97.05276;
			else if(protBuff[i] == 'Q') mass += 128.05858;
			else if(protBuff[i] == 'R') mass += 156.10111;
			else if(protBuff[i] == 'S') mass += 87.03203;
			else if(protBuff[i] == 'T') mass += 101.04786;
			else if(protBuff[i] == 'V') mass += 99.06841;
			else if(protBuff[i] == 'W') mass += 186.07931;
			else if(protBuff[i] == 'Y') mass += 163.06333;
		}
		return mass;
	}

}
