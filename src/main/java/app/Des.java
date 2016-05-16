/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.ArrayUtils;

/**
 *
 * @author mloda
 */
public class Des {

    private BinMath binMath = new BinMath();
    private Tables tables = new Tables();
    private final int B_64 = 64;
    private final int B_56 = 56;
    private int[] key;

    public Des(String keyHex) {
        this.key = this.binMath.fromHexStringToBin(keyHex, B_64);
    }

    public String doDes(boolean cipher, String messageInserted) {
        List<int[]> keys = doKey();
        int[] message = this.binMath.fromHexStringToBin(messageInserted, B_64);
        if (cipher != true) {
            keys = reverseList(keys);
        }
        return cipher(message, keys);
    }

    public void doDesFile(int[][] content, String location, String name) throws IOException {
        List<int[]> keys = doKey();
        RWBinaryFile file = new RWBinaryFile();

        int[][] ciphered = new int[content.length][];
        int[][] deciphered = new int[ciphered.length][];
        
        int contentRow = 0;
        int outputRow = 0;
        while (contentRow < content.length) {
            int[] contentCopy = new int[0];
            for (int i = 0; i < 8; i++, contentRow++) {
                contentCopy = ArrayUtils.addAll(ArrayUtils.clone(content[contentRow]), contentCopy);
            }
            String out = cipher(contentCopy, keys);
            int[] temp = this.binMath.fromHexStringToBin(out, B_64);
            ciphered[outputRow] = ArrayUtils.clone(temp);
            outputRow++;
        }

        keys = reverseList(keys);
        
        int output2Row = 0;
        int cipheredRow = 0;
        while (output2Row < ciphered.length) {
            String out = "";
            out = cipher(ciphered[cipheredRow], keys);
            int[] temp = binMath.fromHexStringToBin(out, B_64);
            for (int indexTemp = 0; indexTemp < 64; indexTemp = indexTemp + 8) {
                deciphered[output2Row] = ArrayUtils.subarray(temp, indexTemp, indexTemp + 8);
                output2Row++;
            }
            cipheredRow++;
        }
        file.write(deciphered, location + "deciphered_" + name);
    }

    private String cipher(int[] message, List<int[]> keys) {
        int[] messPermuted = this.tables.getIP_Message(message);
        int[] messL = ArrayUtils.subarray(messPermuted, 0, messPermuted.length / 2);
        int[] messR = ArrayUtils.subarray(messPermuted, messPermuted.length / 2, messPermuted.length);

        for (int[] key1 : keys) {
            int[] eMessR = this.tables.getE_Message(messR);

            int[] xorResult = new int[eMessR.length];
            for (int i = 0; i < eMessR.length; i++) {
                xorResult[i] = binMath.xor(eMessR[i], key1[i]);
            }

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
            int[] pMessage = this.tables.getP_Message(sTransformedMess);

            int[] tempR = new int[messR.length];
            for (int i = 0; i < messL.length; i++) {
                tempR[i] = binMath.xor(messL[i], pMessage[i]);
            }
            messL = ArrayUtils.clone(messR);
            messR = ArrayUtils.clone(tempR);
        }
        int[] lastPermuted = this.tables.getIPInverse_Message(ArrayUtils.addAll(messR, messL));
        String hex = binMath.fromBinStringToHexString(getString(lastPermuted, 0));
        return hex;
    }

    private List<int[]> doKey() {
        int[] keyPermuted = this.tables.getPc1_Key(this.key);
        int[] keyL = ArrayUtils.subarray(keyPermuted, 0, keyPermuted.length / 2);
        int[] keyR = ArrayUtils.subarray(keyPermuted, keyPermuted.length / 2, keyPermuted.length);

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
        List<int[]> mergedAndPermuted = new ArrayList<>();
        for (int i = 0; i < subkeysL.size(); i++) {
            int[] tempKey = new int[this.B_56];
            tempKey = ArrayUtils.clone(subkeysL.get(i));
            tempKey = ArrayUtils.addAll(tempKey, subkeysR.get(i));
            mergedAndPermuted.add(this.tables.getPc2_Key(tempKey));
        }
        return mergedAndPermuted;
    }

    private List reverseList(List list) {
        List temp = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            temp.add(list.get(i));
        }
        return temp;
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
