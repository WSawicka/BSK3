/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * 13 34 57 79 9B BC DF F1
 *
 * 00010011 00110100 01010111 01111001 10011011 10111100 11011111 11110001
 *
 * 1. 1111000 0110011 0010101 0101111 0101010 1011001 1001111 0001111
 *
 * @author mloda
 */
public class DesAlgorithm {

    private BinMath binMath = new BinMath();
    private int B_64 = 64;
    private int B_56 = 56;
    private int[] key;
    private int[] ipTable = // initial permutation table
            {57, 49, 41, 33, 25, 17, 9,
                1, 58, 50, 42, 34, 26, 18,
                10, 2, 59, 51, 43, 35, 27,
                19, 11, 3, 60, 52, 44, 36,
                63, 55, 47, 39, 31, 23, 15,
                7, 62, 54, 46, 38, 30, 22,
                14, 6, 61, 53, 45, 37, 29,
                21, 13, 5, 28, 20, 12, 4};
    private int[] shiftKeyTable = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};
    private int[] ipTableInverse = {};

    public DesAlgorithm(String keyHex) {
        String keyBin = "";
        for (int i = 0; i < keyHex.length(); i++) {
            if (keyHex.charAt(i) == ' ') {
                keyBin += ' ';
            } else {
                String binTemp = this.binMath.hexToBin(keyHex.charAt(i) + "");
                while (binTemp.length() < 4) {
                    binTemp = "0" + binTemp;
                }
                keyBin += binTemp;
            }
        }
        System.out.println("Key in binary:\n" + keyBin);

        this.key = new int[this.B_64];
        int keyIndex = 0;

        for (int i = 0; i < keyBin.length(); i++) {
            try {
                int bit = Integer.parseInt(keyBin.charAt(i) + "");
                this.key[keyIndex] = bit;
                keyIndex++;
            } catch (NumberFormatException nfex) {
                if (keyBin.charAt(i) != ' ') {
                    System.out.println("Wrong key!");
                    break;
                }
            }
        }
    }

    public void doDes() {
        // 1. initial permutation
        int[] keyPermuted = getPermutedKey();
        System.out.println("\n\t1. PERMUTED KEY: \n" + getString(keyPermuted, 7));

        // 2. divide array into two parts - left half and right half
        int[] keyLH = ArrayUtils.subarray(keyPermuted, 0, keyPermuted.length / 2);
        int[] keyRH = ArrayUtils.subarray(keyPermuted, keyPermuted.length / 2, keyPermuted.length);
        System.out.println("\n\t2. DIVIDED KEYS:" + getString(keyLH, 7) + "\n" + getString(keyRH, 7));

        // 3. create 16 subkeys using shifting
        List<int[]> subkeysLH = new ArrayList<>();
        List<int[]> subkeysRH = new ArrayList<>();

        int[] lastLH = Arrays.copyOf(keyLH, keyLH.length);
        int[] lastRH = Arrays.copyOf(keyRH, keyRH.length);
        for (int shift : this.shiftKeyTable) {
            if (shift == 1) {
                int[] newKeyLH = this.binMath.leftShift(lastLH);
                int[] newKeyRH = this.binMath.leftShift(lastRH);
                subkeysLH.add(newKeyLH);
                subkeysRH.add(newKeyRH);
                lastLH = newKeyLH;
                lastRH = newKeyRH;
            } else if (shift == 2) {
                int[] newKeyLH = this.binMath.doubleLeftShift(lastLH);
                int[] newKeyRH = this.binMath.doubleLeftShift(lastRH);
                subkeysLH.add(newKeyLH);
                subkeysRH.add(newKeyRH);
                lastLH = newKeyLH;
                lastRH = newKeyRH;
            }
        }
        
        System.out.println("\n\t3. SHIFTS:");
        for (int i=0;i<subkeysLH.size();i++) {
            System.out.println(getString(subkeysLH.get(i), 7));
            System.out.println(getString(subkeysRH.get(i), 7) + "\n");
        }
    }

    private int[] getPermutedKey() {
        int[] keyP = new int[this.B_56];
        for (int i = 0; i < this.B_56; i++) {
            int a = this.ipTable[i];
            keyP[i] = this.key[this.ipTable[i] - 1];
        }
        return keyP;
    }

    private String getString(int[] table, int partLength) {
        String out = "";

        for (int i = 0; i < table.length; i++) {
            if (i > 0 && (i % partLength) == 0) {
                out += " ";
            }
            out += table[i];
        }

        return out;
    }
}
