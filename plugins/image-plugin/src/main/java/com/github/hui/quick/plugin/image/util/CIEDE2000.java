package com.github.hui.quick.plugin.image.util;

import java.awt.Color;
import java.lang.Math;

public class CIEDE2000 {
    public static double calculateCIEDE2000(double L1, double a1, double b1, double L2, double a2, double b2) {
        double L_bar = (L1 + L2) / 2;
        double a_bar = (a1 + a2) / 2;
        double b_bar = (b1 + b2) / 2;
        double C1 = Math.sqrt(a1 * a1 + b1 * b1);
        double C2 = Math.sqrt(a2 * a2 + b2 * b2);
        double C_bar = (C1 + C2) / 2;

        double h1 = Math.atan2(b1, a1) * (180 / Math.PI);
        double h2 = Math.atan2(b2, a2) * (180 / Math.PI);
        if (h1 < 0) h1 += 360;
        if (h2 < 0) h2 += 360;

        double Delta_L = L2 - L1;
        double Delta_C = C2 - C1;
        double Delta_h = h2 - h1;
        if (Delta_h > 180) Delta_h -= 360;
        if (Delta_h < -180) Delta_h += 360;

        double h_bar = (h1 + h2) / 2;
        double Delta_H;
        if (C1 == 0 || C2 == 0) {
            Delta_H = 0;
        } else {
            Delta_H = 2 * Math.sqrt(C1 * C2) * Math.sin((Delta_h / 2) * Math.PI / 180);
        }

        double S_L = 1 + 0.015 * Math.pow(L_bar - 50, 2) / Math.sqrt(20 + Math.pow(L_bar - 50, 2));
        double S_C = 1 + 0.045 * C_bar;
        double R_C = Math.sqrt(Math.pow(C_bar, 7) / (Math.pow(C_bar, 7) + Math.pow(25, 7)));
        double T = 1 - 0.17 * Math.cos((h_bar - 30) * Math.PI / 180) +
                0.24 * Math.cos(2 * h_bar * Math.PI / 180) +
                0.32 * Math.cos((3 * h_bar + 6) * Math.PI / 180) -
                0.20 * Math.cos((4 * h_bar - 63) * Math.PI / 180);
        double S_H = S_C * (T + (1 - T) * Math.exp(-1 * Math.pow(Delta_H / (S_C * 2), 2)));

        double Delta_E = Math.sqrt(Math.pow(Delta_L / S_L, 2) +
                Math.pow(Delta_C / S_C, 2) +
                Math.pow(Delta_H / S_H, 2));
        return Delta_E;
    }

    private static double[] convertRGBtoCIELAB(int r, int g, int b) {
        double R = r / 255.0;
        double G = g / 255.0;
        double B = b / 255.0;

        if (R <= 0.04045) R = R / 12.92;
        else R = Math.pow((R + 0.055) / 1.055, 2.4);
        if (G <= 0.04045) G = G / 12.92;
        else G = Math.pow((G + 0.055) / 1.055, 2.4);
        if (B <= 0.04045) B = B / 12.92;
        else B = Math.pow((B + 0.055) / 1.055, 2.4);

        double X = 0.4124564 * R + 0.3575761 * G + 0.1804375 * B;
        double Y = 0.2126729 * R + 0.7151522 * G + 0.0721750 * B;
        double Z = 0.0193339 * R + 0.1191920 * G + 0.9503041 * B;

        X = X * 100;
        Y = Y * 100;
        Z = Z * 100;

        double X_n = 95.047;
        double Y_n = 100.0;
        double Z_n = 108.883;

        double x_r = X / X_n;
        double y_r = Y / Y_n;
        double z_r = Z / Z_n;

        double epsilon = 0.008856;
        double kappa = 903.3;

        double f_x = (x_r > epsilon) ? Math.cbrt(x_r) : (7.787 * x_r + 16.0 / 116.0);
        double f_y = (y_r > epsilon) ? Math.cbrt(y_r) : (7.787 * y_r + 16.0 / 116.0);
        double f_z = (z_r > epsilon) ? Math.cbrt(z_r) : (7.787 * z_r + 16.0 / 116.0);

        double L = (y_r > epsilon) ? (116.0 * Math.cbrt(y_r) - 16.0) : (kappa * y_r);
        double a = 500.0 * (f_x - f_y);
        double c = 200.0 * (f_y - f_z);

        return new double[]{L, a, c};
    }

    public static double calculateCIEDE2000(Color color1, Color color2) {
        double[] lab1 = convertRGBtoCIELAB(color1.getRed(), color1.getGreen(), color1.getBlue());
        double[] lab2 = convertRGBtoCIELAB(color2.getRed(), color2.getGreen(), color2.getBlue());
        return calculateCIEDE2000(lab1[0], lab1[1], lab1[2], lab2[0], lab2[1], lab2[2]);
    }

    public static void main(String[] args) {
        Color color1 = new Color(255, 0, 0);
        Color color2 = new Color(0, 255, 0);
        double result3 = calculateCIEDE2000(color1, color2);
        System.out.println("测试 3：不同颜色，预期约 80，得到 " + result3);
    }
}