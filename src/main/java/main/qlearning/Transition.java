package main.qlearning;

public class Transition {

	private int mutationPostion;
	private double expectedReward;
	private char mutationChar;
	private boolean terminal;
	
	public Transition(int mutationPos, char mutation, double expectedReward, boolean terminal){
		mutationPostion = mutationPos;
		mutationChar = mutation;
		this.expectedReward = expectedReward;
		this.terminal = terminal;
		
	}

	public int getMutationPostion() {
		return mutationPostion;
	}

	public void setMutationPostion(int mutationPostion) {
		this.mutationPostion = mutationPostion;
	}

	public char getMutationChar() {
		return mutationChar;
	}

	public void setMutationChar(char mutationChar) {
		this.mutationChar = mutationChar;
	}

	public double getExpectedReward() {
		return expectedReward;
	}

	public void setExpectedReward(double expectedReward) {
		this.expectedReward = expectedReward;
	}

	public boolean isTerminal() {
		return terminal;
	}

	public void setTerminal(boolean terminal) {
		this.terminal = terminal;
	}
	
	public String toString(){
		return mutationPostion + "->" + mutationChar + " (" + expectedReward + ")";
	}
	
	public boolean sameTransition(Transition other){
		if(other.getMutationChar() == mutationChar && other.isTerminal() == terminal && other.getMutationPostion() == mutationPostion)
			return true;
		return false;
	}
	
}
