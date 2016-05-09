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
    private int[] key;

    public Des(String keyHex) {
        this.key = this.binMath.fromHexStringToBin(keyHex, B_64);
    }

    public void doDes(String messageInserted) {
        int[] message = this.binMath.fromHexStringToBin(messageInserted, B_64);

        // 1. initial permutation
        int[] messPermuted = this.tables.getIP_Message(message);
        // 2. divide array into two parts - left half and right half
        int[] messL = ArrayUtils.subarray(messPermuted, 0, messPermuted.length / 2);
        int[] messR = ArrayUtils.subarray(messPermuted, messPermuted.length / 2, messPermuted.length);
        System.out.println("\n\t2. DIVIDED:\n" + getString(messL, 8) + "\n" + getString(messR, 8));

        List keys = doKey();

        // ITERATIONS
        for (int i = 0; i < 16; i++) {
            int[] eMessR = this.tables.getE_Message(messR);
            System.out.println(getString(eMessR, 6));

        }
    }

    public List<int[]> doKey() {
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
