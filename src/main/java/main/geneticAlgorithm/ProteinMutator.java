package main.geneticAlgorithm;


import main.proteins.RandomAminoAcidGenerator;
import main.proteins.RandomNumberGenerator;

/**
 * Created by Leo on 11/21/15.
 */
public class ProteinMutator implements Runnable {

    Protein protein = null;

    public ProteinMutator(Protein protein)
    {
        this.protein = protein;
    }

    public void run() {
        char newAminoAcidChar = RandomAminoAcidGenerator.generate();
        String originalSeq = protein.getAminoAcidsequence();
        int seqLen = originalSeq.length();
        int mutatePos = RandomNumberGenerator.generate(0, seqLen);

        if (mutatePos < 0 || mutatePos >= seqLen)
        {
            throw new RuntimeException("mutatePos [" + mutatePos + "] should be between 0 and " + (seqLen - 1));
        }

        char originalAminoAcidChar = originalSeq.charAt(mutatePos);

        //Make sure new and old is different
        while(originalAminoAcidChar == newAminoAcidChar || (originalAminoAcidChar == 'I' && newAminoAcidChar =='L')
                || (originalAminoAcidChar == 'L' && newAminoAcidChar == 'I'))
        {
            newAminoAcidChar = RandomAminoAcidGenerator.generate();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(originalSeq.substring(0, mutatePos));
        sb.append(newAminoAcidChar);
        if (mutatePos < seqLen - 1) {
            sb.append(originalSeq.substring(mutatePos + 1));
        }

        String newSeq = sb.toString();

        protein.setAminoAcidsequence(newSeq);

        if (mutatePos != protein.getLatestMutatePos())
        {
            protein.setLatestMutatePos(mutatePos);
            refershMutationTimes(false);
        }
        else
        {
            refershMutationTimes(true);
        }
    }

    private void refershMutationTimes(boolean samePosition)
    {
        char o[] = protein.getOrigin().getAminoAcidsequence().toCharArray();
        char n[] = protein.getAminoAcidsequence().toCharArray();

        int k = 0;
        for (int i = 0; i < o.length; i++)
        {
            if (o[i] != n[i])
            {
                k++;
            }
        }
        protein.setMutationTimes(k);
    }
}
