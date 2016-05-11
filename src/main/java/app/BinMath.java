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

    public int[] fromBinStringToBin(String value) {
        int[] valueTable = new int[value.length()];

        return valueTable;
    }

    public int[] fromHexStringToBin(String value, int length) {
        String valueBin = "";
        int[] valueTable = new int[length];
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == ' ') {
                valueBin += ' ';
            } else {
                String binTemp = hexToBin(value.charAt(i) + "");
                while (binTemp.length() < 4) {
                    binTemp = "0" + binTemp;
                }
                valueBin += binTemp;
            }
        }
        int keyIndex = 0;

        for (int i = 0; i < valueBin.length(); i++) {
            try {
                int bit = Integer.parseInt(valueBin.charAt(i) + "");
                valueTable[keyIndex] = bit;
                keyIndex++;
            } catch (NumberFormatException nfex) {
                if (valueBin.charAt(i) != ' ') {
                    System.out.println("Wrong input!");
                    break;
                }
            }
        }
        return valueTable;
    }

    public String hexToBin(String hex) {
        int i = Integer.parseInt(hex, 16);
        String bin = Integer.toBinaryString(i);
        return bin;
    }

    public String intToString(int number) {
        String binary = Integer.toBinaryString(number);
        while (binary.length() < 4) {
            binary = "0" + binary;
        }
        return binary;
    }

    public int[] doLeftShift(int[] table, int shift) {
        int[] shiftTable = (shift == 1) ? leftShift(table) : doubleLeftShift(table);
        return shiftTable;
    }

    private int[] leftShift(int[] table) {
        int lastIndex = table.length - 1;
        int[] shiftedTable = new int[table.length];
        for (int i = lastIndex; i > 0; i--) {
            shiftedTable[i - 1] = table[i];
        }
        shiftedTable[table.length - 1] = table[0];
        return shiftedTable;
    }

    private int[] doubleLeftShift(int[] table) {
        int lastIndex = table.length - 1;
        int[] shiftedTable = new int[table.length];
        for (int i = lastIndex; i > 1; i--) {
            shiftedTable[i - 2] = table[i];
        }
        shiftedTable[table.length - 2] = table[0];
        shiftedTable[table.length - 1] = table[1];
        return shiftedTable;
    }

    public int xor(int x, int y) {
        if (x == 0 && y == 0) {
            return 0;
        }
        if (x == 0 && y == 1) {
            return 1;
        }
        if (x == 1 && y == 0) {
            return 1;
        }
        if (x == 1 && y == 1) {
            return 0;
        }
        return 10;
    }
}
