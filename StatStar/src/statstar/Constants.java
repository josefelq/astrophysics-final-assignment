// ***************************************************************************
// 
//  Archivo: Constants.java    
//
//
//  Proposito: Esta clase se encarga de tener todas las constantes habidas y por
//  haber.
//
//****************************************************************************
package statstar;

import static java.lang.Math.pow;

public final class Constants {
    
    private Constants(){}
    
 
    public static final float tiny_sp = 3.4e-38F;
    public static final double tiny_dp = 1.7e-308;
    public static final double tiny_qp = tiny_dp;
    public static final int sig_fig_sp = 7;
    public static final int sig_fig_dp = 15;
    public static final int sig_fig_qp = sig_fig_dp;
    public static final float eps_sp = (float) 1E-6;
    public static final double eps_dp = 1E-15;
    public static final double eps_qp = eps_dp;
    
    
    public static final float biggest_sp = 3.4e38F;
    public static final double biggest_dp = 1.7e308;
    public static final double biggest_qp = biggest_dp;
    public static final int biggest_i2 = 32767;
    public static final int biggest_i4 = 2147483647;
    public static final long biggest_i8 = 9223372036854775807L;
    
    //Valores relacionados con pi y e
    
    public static final double pi = 3.14159265358979323846264338327950;
    public static final double two_pi = 2*pi;
    public static final double four_pi = 4*pi;
    public static final double four_pi_o3 = four_pi/3;
    public static final double pi_over_2 = pi/2;
    
    public static final double natural_e = 2.71828182845904523536028747135266;
    
    //Radianes a grados y grados a radianes
    public static final double degrees_to_radians = pi/180;
    public static final double radians_to_degrees = 180/pi;
    
    //Constantes de la fisica
    public static final float G = 6.673e-11F;
    public static final double c = 2.99792458e08;
    public static final double mu_0 = four_pi*1e-07;
    public static final double epsilon_0 = 1/(mu_0*c*c);
    
    public static final double e_C = 1.602176462e-19;
    public static final double eV = e_C;
    public static final double keV = eV*1.0e3;
    public static final double MeV = eV*1.0e6;
    public static final double GeV = eV*1.0e9;
    
    public static final double h = 6.62606876e-34;
    public static final double hbar = h/two_pi;
    
    public static final double k_B = 1.3806503e-23;
    
    public static final double sigma = 2*pow(pi,5)*pow(k_B,4)/(15*c*c*pow(h,3));
    public static final double a_rad = 4*sigma/c;
    public static final double a_rad_o3 = a_rad/3;
    public static final double four_ac_o3 = 4*a_rad_o3*c;
    
    public static final double m_e = 9.10938188e-31;
    public static final double m_p = 1.67262158e-27;
    public static final double m_n = 1.67492716e-27;
    public static final double m_H = 1.673532499e-27;
    public static final double u = 1.66053873e-27;
    
    public static final double N_A = 6.02214199e23;
    public static final double R_gas = 8.314472;
    
    public static final double a_0 = four_pi*epsilon_0*hbar*hbar/(m_e*e_C*e_C);
    
    public static final double R_infty = m_e*pow(e_C,4)/(64*pow(pi,3)*epsilon_0*epsilon_0*pow(hbar,3)*c);
    public static final double R_H = m_p/(m_e + m_p)*R_infty;
    
    //Constantes de tiempo
    public static final int hr = 3600;
    public static final int day = 24*hr;
    public static final double J_yr = 365.25*day;
    public static final double yr = 3.15581450e7;
    public static final double T_yr = 3.155692519e7;
    public static final double G_yr = 3.1556952e7;
    
    //Constantes astronomicas de distancia
    public static final double AU = 1.4959787066e11;
    public static final double pc = 206264.806*AU;
    public static final double ly = c*J_yr;
    
    //Constantes solares
    public static final float M_Sun = 1.9891e30F;
    public static final float S_Sun = 1.365e3F;
    public static final float L_Sun = (float) (four_pi*AU*AU*S_Sun);
    public static final float R_Sun = 6.95508e8F;
    public static final float Te_Sun =  (float) pow((double)(L_Sun/(four_pi*R_Sun*R_Sun*sigma)),0.25);
    
    //Magnitudes solares
    public static final float Mbol_Sun = 4.74F;
    public static final float MU_Sun = 5.67F;
    public static final float MB_Sun = 5.47F;
    public static final float MV_Sun = 4.82F;
    public static final float Mbol_Sun_ap = -26.83F;
    public static final float MU_Sun_ap = -25.91F;
    public static final float MB_Sun_ap = -26.10F;
    public static final float MV_Sun_ap = -26.75F;
    public static final float BC_Sun = -0.08F;
    
    //Constantes terrestres
    public static final float M_Earth = 5.9736e24F;
    public static final float R_Earth = 6.378136e6F;
    
    //Conversiones
    public static final float cm = (float) 1e-2;
    public static final float gram = (float) 1e-3;
    public static final float erg = (float) 1e-7;
    public static final float dyne = (float) 1e-5;
    public static final double esu = 3.335640952e-10;
    public static final double statvolt = 2.997924580e2;
    public static final float gauss = (float) 1e-4;
    public static final float angstrom = (float) 1e-10;
    public static final float jansky = (float) 1e-26;
    
}
