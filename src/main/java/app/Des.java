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
 * key: 13 34 57 79 9B BC DF F1
 *
 * 00010011 00110100 01010111 01111001 10011011 10111100 11011111 11110001
 *
 * @author mloda
 */
public class Des {

    private BinMath binMath = new BinMath();
    private Tables tables = new Tables();
    private int B_64 = 64;
    private int B_56 = 56;
    private int B_48 = 48;
    private int B_32 = 32;
    private int[] key;

    public Des(String keyHex) {
        this.key = this.binMath.fromHexStringToBin(keyHex, B_64);
    }

    public String doDes(boolean cipher, String messageInserted) {
        List<int[]> keys = doKey();
        if(cipher != true) reverseList(keys);
        return cipher(messageInserted);
    }

    private String cipher(String messageInserted) {
        int[] message = this.binMath.fromHexStringToBin(messageInserted, B_64);

        // 1. initial permutation
        int[] messPermuted = this.tables.getIP_Message(message);

        // 2. divide array into two parts - left half and right half
        int[] messL = ArrayUtils.subarray(messPermuted, 0, messPermuted.length / 2);
        int[] messR = ArrayUtils.subarray(messPermuted, messPermuted.length / 2, messPermuted.length);
        List<int[]> keys = doKey();

        // 3. ITERATIONS
        for (int[] key1 : keys) {
            // e permutation
            int[] eMessR = this.tables.getE_Message(messR);

            // XOR of key and permuted message
            int[] xorResult = new int[eMessR.length];
            for (int i = 0; i < eMessR.length; i++) {
                xorResult[i] = binMath.xor(eMessR[i], key1[i]);
            }

            // s-box transformation
            String row = "";
            String column = "";
            int[] sTransformedMess = new int[32];
            int sTransformedIndex = 0;
            int sTableIndex = 0;
            for (int i = 0; i < xorResult.length; i = i + 6, sTableIndex++) {
                row = String.valueOf(xorResult[i]) + String.valueOf(xorResult[i + 5]);
                column = String.valueOf(xorResult[i + 1]) + String.valueOf(xorResult[i + 2])
                        + String.valueOf(xorResult[i + 3]) + String.valueOf(xorResult[i + 4]);

                int sTableNumber = this.tables.getSTables().get(sTableIndex)[Integer.parseInt(row, 2)][Integer.parseInt(column, 2)];
                String binary = this.binMath.intToString(sTableNumber);

                for (int bin = 0; bin < binary.length(); bin++) {
                    sTransformedMess[sTransformedIndex + bin] = Integer.parseInt(binary.charAt(bin) + "");
                }
                sTransformedIndex += binary.length();
            }

            // p permutation -> another table mask
            int[] pMessage = this.tables.getP_Message(sTransformedMess);

            // last xor to get R table
            int[] tempR = new int[messR.length];
            for (int i = 0; i < messL.length; i++) {
                tempR[i] = binMath.xor(messL[i], pMessage[i]);
            }

            // swapping
            messL = ArrayUtils.addAll(messR, null);
            messR = ArrayUtils.addAll(tempR, null);
        }

        // 4. merge and do last permutation
        int[] lastPermuted = this.tables.getIPInverse_Message(ArrayUtils.addAll(messR, messL));
        System.out.println(getString(lastPermuted, 8));
        String hex = binMath.fromBinStringToHexString(getString(lastPermuted, 0));
        return hex;
    }

    private List<int[]> doKey() {
        // 1. initial permutation
        int[] keyPermuted = this.tables.getPc1_Key(this.key);

        // 2. divide array into two parts - left half and right half
        int[] keyL = ArrayUtils.subarray(keyPermuted, 0, keyPermuted.length / 2);
        int[] keyR = ArrayUtils.subarray(keyPermuted, keyPermuted.length / 2, keyPermuted.length);

        // 3. create 16 subkeys using shifting
        List<int[]> subkeysL = new ArrayList<>();
        List<int[]> subkeysR = new ArrayList<>();

        int[] lastL = Arrays.copyOf(keyL, keyL.length);
        int[] lastR = Arrays.copyOf(keyR, keyR.length);
        for (int shift : this.tables.getShiftKeyTable()) {
            int[] newKeyL = this.binMath.doLeftShift(lastL, shift);
            int[] newKeyR = this.binMath.doLeftShift(lastR, shift);
            subkeysL.add(newKeyL);
            subkeysR.add(newKeyR);
            lastL = newKeyL;
            lastR = newKeyR;
        }

        // 4. merge key L and R; Permutation pc2
        List<int[]> mergedAndPermuted = new ArrayList<>();
        for (int i = 0; i < subkeysL.size(); i++) {
            int[] tempKey = new int[this.B_56];
            tempKey = ArrayUtils.clone(subkeysL.get(i));
            tempKey = ArrayUtils.addAll(tempKey, subkeysR.get(i));
            mergedAndPermuted.add(this.tables.getPc2_Key(tempKey));
        }
        return mergedAndPermuted;
    }

    private void reverseList(List list) {
        List temp = new ArrayList<>();
        for (int i = list.size() + 1; i > 0; i--) {
            temp.add(list.get(i));
        }
        list.clear();
        list.addAll(temp);
    }

    private String getString(int[] table, int partLength) {
        String out = "";

        for (int i = 0; i < table.length; i++) {
            if (partLength != 0) {
                if (i > 0 && (i % partLength) == 0) {
                    out += " ";
                }
            }
            out += table[i];
        }
        return out;
    }
}
