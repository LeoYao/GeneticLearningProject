
public class Protein {
	private int k; //num mutations
	private char[] seq;
	
	public Protein(char[] seq){
		setK(0);
		this.seq = seq;
	}

	public char[] getSeq() {
		return seq;
	}

	public void setSeq(char[] seq) {
		this.seq = seq;
	}

	public int getK() {
		return k;
	}

	public void setK(int k) {
		this.k = k;
	}

}
