/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

/**
 *
 * @author mloda
 */
public class BinMath {

    public String hexToBin(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);
        return bin;
    }

    public int[] leftShift(int[] table) {
        int lastIndex = table.length - 1;
        int[] shiftedTable = new int[table.length];
        for (int i = lastIndex; i > 0; i--) {
            shiftedTable[i - 1] = table[i];
        }
        shiftedTable[table.length - 1] = table[0];
        return shiftedTable;
    }

    public int[] doubleLeftShift(int[] table) {
        int lastIndex = table.length - 1;
        int[] shiftedTable = new int[table.length];
        for (int i = lastIndex; i > 1; i--) {
            shiftedTable[i - 2] = table[i];
        }
        shiftedTable[table.length - 2] = table[0];
        shiftedTable[table.length - 1] = table[1];
        return shiftedTable;
    }
}
