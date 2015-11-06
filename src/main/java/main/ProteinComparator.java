package main;

/**
 * Created by Leo on 11/6/15.
 */
public class ProteinComparator {
    public static int diff(Protein p1, Protein p2)
    {
        if (p1 == null || p1.getAminoAcidsequence() == null)
        {
            throw new NullPointerException("p1 or p1.getAminoAcidsequence() is null");
        }


        if (p2 == null || p2.getAminoAcidsequence() == null)
        {
            throw new NullPointerException("p2 or p2.getAminoAcidsequence() is null");
        }

        if (p1.getAminoAcidsequence().length() != p2.getAminoAcidsequence().length())
        {
            throw new RuntimeException("The length of two protein sequence should not be different.");
        }

        int len = p1.getAminoAcidsequence().length();
        String s1 = p1.getAminoAcidsequence();
        String s2 = p2.getAminoAcidsequence();

        int diff = 0;
        for (int i = 0; i < len; i++)
        {
            if (s1.charAt(i) != s2.charAt(i))
            {
                diff++;
            }
        }

        return diff;
    }


    public static int diff(String p1, String p2)
    {
        if (p1 == null)
        {
            throw new NullPointerException("p1 is null");
        }


        if (p2 == null)
        {
            throw new NullPointerException("p2 null");
        }

        if (p1.length() != p2.length())
        {
            throw new RuntimeException("The length of two protein sequence should not be different.");
        }

        int len = p1.length();


        int diff = 0;
        for (int i = 0; i < len; i++)
        {
            if (p1.charAt(i) != p2.charAt(i))
            {
                diff++;
            }
        }

        return diff;
    }
}
