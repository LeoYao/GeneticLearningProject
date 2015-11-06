package main;

/**
 * Created by Leo on 11/6/15.
 */
public class ProteinMutator {

    public static Protein mutate(Protein protein, Protein ancestor)
    {
        char newAminoAcidChar = RandomAminoAcidGenerator.generate();
        String originalSeq = protein.getAminoAcidsequence();
        int seqLen = originalSeq.length();
        int mutatePos = RandomNumberGenerator.generate(0, seqLen);

        return mutate(protein, ancestor, mutatePos);
    }

    public static Protein mutate(Protein protein, Protein ancestor, int mutatePos)
    {
        char newAminoAcidChar = RandomAminoAcidGenerator.generate();
        String originalSeq = protein.getAminoAcidsequence();
        int seqLen = originalSeq.length();
        if (mutatePos < 0 || mutatePos >= seqLen)
        {
            throw new RuntimeException("mutatePos [" + mutatePos + "] should be between 0 and " + (seqLen - 1));
        }

        char originalAminoAcidChar = originalSeq.charAt(mutatePos);

        //Make sure new and old is different
        while(originalAminoAcidChar == newAminoAcidChar)
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

        int mutateTimes = ProteinComparator.diff(ancestor.getAminoAcidsequence(), newSeq);

        Protein newProtein = new Protein(newSeq, mutateTimes, mutatePos);

        return newProtein;
    }
}
