package main.geneticAlgorithm;

import main.proteins.TheoreticalSpectrum;

/**
 * Created by Leo on 11/21/15.
 */
public class FitnessCalculation implements Runnable {

    Protein protein = null;

    public FitnessCalculation(Protein protein)
    {
        this.protein = protein;
    }

    public void run() {
        protein.calcFitness();
    }
}

