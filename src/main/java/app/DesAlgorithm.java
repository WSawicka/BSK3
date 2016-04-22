/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.util.Arrays;
import org.apache.commons.lang.ArrayUtils;

/**
 * 10000000 10000000 11110000 11111101 10100010 11010111 11111100 01111000
 * 00000000 00000001 00000010 00000011 00000100 00000101 00000110 00000111
 *
 * @author mloda
 */
public class DesAlgorithm {

    private int B_64 = 64;
    private int B_56 = 56;
    private int[] key;
    private int[] ipTable = // initial permutation table
            {57, 49, 41, 33, 25, 17, 9,
             1,  58, 50, 42, 34, 26, 18,
             10, 2,  59, 51, 43, 35, 27,
             19, 11, 3,  60, 52, 44, 36,
             63, 55, 47, 39, 31, 23, 15,
             7,  62, 54, 46, 38, 30, 22, 
             14, 6,  61, 53, 45, 37, 29,
             21, 13, 5,  28, 20, 12, 4};

    public DesAlgorithm(String key) {
        this.key = new int[this.B_64];
        int keyIndex = 0;
        for (int i = 0; i < key.length(); i++) {
            try {
                int bit = Integer.parseInt(key.charAt(i) + "");
                this.key[keyIndex] = bit;
                keyIndex++;
            } catch (NumberFormatException nfex) {
                if (key.charAt(i) != ' ') {
                    System.out.println("Wrong key!");
                    break;
                }
            }
        }
    }

    private int[] getPermutedKey() {
        int[] keyP = new int[this.B_64];
        for (int i = 0; i < this.B_56; i++) {
            int a = this.ipTable[i];
            keyP[i] = this.key[this.ipTable[i] - 1];
        }
        return keyP;
    }

    public void doDes() {
        // 1. initialpermutation
        int[] keyPermuted = getPermutedKey();

        // 2. divide array into two parts - left half and right half
        int[] keyLH = ArrayUtils.subarray(keyPermuted, 0, keyPermuted.length / 2);
        int[] keyRH = ArrayUtils.subarray(keyPermuted, keyPermuted.length / 2, keyPermuted.length);

        // 3. create 16 subkeys using shifting
    }
}
