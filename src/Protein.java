
public class Protein {
	private int k; //num mutations
	private String aminoAcidsequence;
	
	public Protein(String seq){
		setK(0);
		this.aminoAcidsequence = seq;
	}
	
	public Protein(){
		k = 0;
		aminoAcidsequence = "";
	}

	public String getAminoAcidsequence() {
		return aminoAcidsequence;
	}

	public void setAminoAcidsequence(String seq) {
		this.aminoAcidsequence = seq;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}
	
	public void append(String append){
		this.aminoAcidsequence += append;
	}

}
