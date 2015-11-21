package main.proteins;

/**
 * Created by Leo on 11/6/15.
 */
public class RandomAminoAcidGenerator {
    private static String candiates = "ACDEFGHIKLMNPQRSTVWY";
    private static int len = 20;


    public static char generate()
    {
        int pos = RandomNumberGenerator.generate(0, len);
        return candiates.charAt(pos);
    }
}
