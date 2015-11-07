package main.java.main.geneticAlgorithm;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ProteinDatabase {
	private List<Protein> database;
	
	
	public ProteinDatabase(){
		setDatabase(new ArrayList<Protein>());
	}
	
	public static void main(String[] args){
		ProteinDatabase ident = new ProteinDatabase();
		ident.parseInput("main/proteins.fasta");
		//ident.calcSpectrums();
	}
	
	/*public void calcSpectrums(){		
		TheoreticalSpectrum spect = new TheoreticalSpectrum(database.get(0).getAminoAcidsequence());
		spect.calculate();
		System.out.println();
	}*/
	
	public void parseInput(String fileName) {
		URL url = this.getClass().getResource(fileName);
		try {
			File file = new File(url.toURI());
			Scanner scan = new Scanner(file);
			Protein parsing = null;
			int onData = 0;
			while (scan.hasNext()) {
				String line = scan.nextLine();
				if (line.startsWith(">")) {
					if (parsing != null) {
						getDatabase().add(parsing);
					}
					parsing = new Protein();
				} else {
					parsing.append(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Protein> getDatabase() {
		return database;
	}

	public void setDatabase(List<Protein> database) {
		this.database = database;
	}

}
