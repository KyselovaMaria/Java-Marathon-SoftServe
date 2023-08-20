package jom.com.softserve.s6.task3;

import java.io.*;

public class MyUtils {

    public static void writeFile(String filename, String text) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            bw.write(convertStringToBinary(text));
        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }


    public static String convertStringToBinary(String input) {
        StringBuilder result = new StringBuilder();
        char[] chars = input.toCharArray();
        for (char aChar : chars) {
            result.append(
                    String.format("%7s", Integer.toBinaryString(aChar))
                            .replaceAll(" ", "0")
            );
        }
        return result.toString();
    }
}
