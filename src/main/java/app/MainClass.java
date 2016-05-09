/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

/**
 *
 * @author mloda
 */
public class MainClass {

    private static final String location = "C:\\Users\\mloda\\Desktop\\";

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        RWBinaryFile file = new RWBinaryFile();

        String chosen = "1";
        show("CHOOSE: 1. Code, 2. Decode, 0. End.");
        chosen = br.readLine();
        while (!chosen.equals("0")) {
            //show("Actual location of searching files: " + location);
            //show("Insert input file name: ");
            //String name = br.readLine();
            //int[][] content = file.loadFile(location + name);
            show("Insert meassage: ");
            String message = br.readLine();

            show("Insert (hex) key: ");
            String key = br.readLine();
            Des des = new Des(key);
            des.doDes(message);

            show("\nCHOOSE: 1. Code, 2. Decode, 0. End.");
            chosen = br.readLine();
        }
    }

    private static void show(Object message) {
        System.out.println(String.valueOf(message));
    }
}
