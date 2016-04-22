/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 *
 * @author mloda
 */
public class RWBinaryFile {
    public int[][] loadFile(String filePath) throws IOException {
        int[][] result = null;
        byte[] file = Files.readAllBytes(Paths.get(filePath));

        String[] stringParts = null;
        stringParts = new String[file.length];
        result = new int[file.length][];

        for (int i = 0; i < stringParts.length; i++) {
            if (file[i] < 0) {
                file[i] = (byte) (file[i] + 256);
            }
            stringParts[i] = ("0000000" + Integer.toBinaryString(0xFF & file[i])).replaceAll(".*(.{8})$", "$1");
        }

        int[] tempTable = new int[8];
        for (int i = 0; i < stringParts.length; i++) {
            for (int j = 0; j < 8; j++) {
                tempTable[j] = (Integer.parseInt(stringParts[i].charAt(j) + ""));
            }
            result[i] = tempTable;
            tempTable = new int[8];
        }
        return result;
    }

    public void write(int[][] input, String outputFileName) throws IOException {
        System.out.println("Writing binary file...");

        byte[] bytes = new byte[input.length];

        try {
            FileOutputStream output = null;
            for (int i = 0; i < input.length; i++) {
                int val = 0;
                int pos = 0;
                for (int j = input[i].length - 1; j >= 0; j--, pos++) {
                    double multiply = Math.pow(2, pos);
                    multiply *= input[i][j];
                    val += (int) multiply;
                }
                bytes[i] = (byte) val;
            }

            try {
                output = new FileOutputStream(outputFileName);
                output.write(bytes);
            } finally {
                output.close();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Ups... gotta file error. Nothing was wrote.");
        }
    }
}
