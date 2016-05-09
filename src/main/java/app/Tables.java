/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import lombok.Getter;

/**
 *
 * @author mloda
 */
@Getter
public class Tables {

    private int B_64 = 64;
    private int B_56 = 56;
    private int B_48 = 48;
    private int B_32 = 32;

    private int[] pc1Table = // key
            {57, 49, 41, 33, 25, 17, 9,
                1, 58, 50, 42, 34, 26, 18,
                10, 2, 59, 51, 43, 35, 27,
                19, 11, 3, 60, 52, 44, 36,
                63, 55, 47, 39, 31, 23, 15,
                7, 62, 54, 46, 38, 30, 22,
                14, 6, 61, 53, 45, 37, 29,
                21, 13, 5, 28, 20, 12, 4};
    private int[] pc2Table = // key
            {14, 17, 11, 24, 1, 5,
                3, 28, 15, 6, 21, 10,
                23, 19, 12, 4, 26, 8,
                16, 7, 27, 20, 13, 2,
                41, 52, 31, 37, 47, 55,
                30, 40, 51, 45, 33, 48,
                44, 49, 39, 56, 34, 53,
                46, 42, 50, 36, 29, 32};
    private int[] ipTable = // initial permutation table
            {58, 50, 42, 34, 26, 18, 10, 2,
                60, 52, 44, 36, 28, 20, 12, 4,
                62, 54, 46, 38, 30, 22, 14, 6,
                64, 56, 48, 40, 32, 24, 16, 8,
                57, 49, 41, 33, 25, 17, 9, 1,
                59, 51, 43, 35, 27, 19, 11, 3,
                61, 53, 45, 37, 29, 21, 13, 5,
                63, 55, 47, 39, 31, 23, 15, 7};
    private int[] eTable = // E bit-selection table
            {32, 1, 2, 3, 4, 5,
                4, 5, 6, 7, 8, 9,
                8, 9, 10, 1, 12, 13,
                12, 13, 14, 15, 16, 17,
                16, 17, 18, 19, 20, 21,
                20, 21, 22, 23, 24, 25,
                24, 25, 26, 27, 28, 29,
                28, 29, 30, 31, 32, 1};
    private int[] shiftKeyTable = {1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1};

    public int[] getPc1_Key(int[] key) {
        int[] keyP = new int[this.B_56];
        for (int i = 0; i < this.B_56; i++) {
            int a = this.pc1Table[i];
            keyP[i] = key[this.pc1Table[i] - 1];
        }
        return keyP;
    }

    public int[] getPc2_Key(int[] key) {
        int[] keyP = new int[this.B_48];
        for (int i = 0; i < this.B_48; i++) {
            int a = this.pc2Table[i];
            keyP[i] = key[this.pc2Table[i] - 1];
        }
        return keyP;
    }

    public int[] getIP_Message(int[] key) {
        int[] keyP = new int[this.B_64];
        for (int i = 0; i < this.B_64; i++) {
            int a = this.ipTable[i];
            keyP[i] = key[this.ipTable[i] - 1];
        }
        return keyP;
    }

    public int[] getE_Message(int[] key) {
        int[] keyP = new int[this.B_48];
        for (int i = 0; i < this.B_48; i++) {
            int a = this.eTable[i];
            keyP[i] = key[this.eTable[i] - 1];
        }
        return keyP;
    }
}
