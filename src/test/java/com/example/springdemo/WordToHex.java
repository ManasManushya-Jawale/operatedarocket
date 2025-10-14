package com.example.springdemo;

import java.util.Scanner;

public class WordToHex {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the String: ");
        String str = sc.next();
        StringBuilder hexStr = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            int n = str.charAt(i);
            hexStr.append(Integer.toHexString(n));
        }

        System.out.println(hexStr.toString());
    }
}
