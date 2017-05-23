// ***************************************************************************
// 
//  Archivo: Boundary_Conditions.java   
//
//
//  Proposito: Esta clase se encarga de tener todas las functiones que hacen la 
//  integracion de la superficie.
//
//****************************************************************************
package statstar;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static statstar.Composition.Helium;
import static statstar.Composition.Mean_Molecular_Weight;
import static statstar.Constants.G;
import static statstar.Constants.m_H;
import static statstar.Constants.pi;
import static statstar.Constants.a_rad;
import static statstar.Constants.a_rad_o3;
import static statstar.Constants.c;
import static statstar.Constants.four_pi_o3;
import static statstar.Constants.k_B;
import static statstar.Constants.two_pi;
import static statstar.Physics.Density;
import static statstar.Physics.Nuclear;
import static statstar.Physics.Opacity;
import static statstar.Physics.PTgradient;
import static statstar.Physics.Specific_Heat_Ratio;
import static statstar.Stellar_Structure_Equations.dLdr;
import static statstar.Stellar_Structure_Equations.dMdr;

public class Boundary_Conditions {
    
    public static void Surface(int i, double Ms, double Ls, double Mm, double Lm, double rm, double Pm, double Tm, double X, double Z, double dr, double r, double P, double T, double M_r, double L_r, double rho, double kappa, double epsilon, double dlnPdlnT, double gamma, char rc_flag, int step_size_condition, boolean good_surface){

        final double a = a_rad;
        final double k = k_B;
        final double g_ff = 1;              
        final double A_bf = 4.34e21;       
        final double A_ff = 3.68e18;        
        final double maximum = 1.0e-08;     
        final int j_max = 50;

        r = rm + dr;
        double Y  = Helium(X, Z);
        double mu = Mean_Molecular_Weight(X, Y, Z);
        gamma = Specific_Heat_Ratio();
        
        double gamma_ratio = gamma/(gamma - 1);

        int j = 0;
        boolean exit_outerzone = false;
        while (!exit_outerzone){
        
            rc_flag = 'r';
            T = G*Ms*(mu*m_H/(4.25*k))*(1/r - 1/rm);     
            
            double tog_bf;
            if (i < 2) tog_bf = 0.01;                       
            else tog_bf = 2.82*pow((rho*(1 + X)), 0.2);     

            double Aop = (A_bf/tog_bf)*Z*(1+X) + A_ff*g_ff*(1-Z)*(1+X);     
            P = sqrt((1/4.25)*(16*pi/3)*(G*Ms/Ls)*(a*c*k/(Aop*mu*m_H)))*pow(T, 4.25);
           

       
        dlnPdlnT = PTgradient(Pm, P, Tm, T);
        
        double kPadiabatic;
        if (dlnPdlnT < gamma_ratio && i > 2) {
            rc_flag = 'c';
            
            kPadiabatic = Pm/pow(Tm, gamma_ratio);
            T = G*Ms*(mu*m_H/(k*gamma_ratio))*(1/r - 1/rm); 
            P = kPadiabatic*pow(T, gamma_ratio);        
        }
        
        
        rho = Density(T, P, mu, step_size_condition);
        
        if (rho < 0) {
            good_surface = false;
            exit_outerzone = true;
        }
        if (exit_outerzone) break;

        kappa = Opacity(T, rho, X, Z);
        
        epsilon = Nuclear(T, rho, X, Z);
       

       
        M_r = Mm + dMdr(r, rho)*dr;
        
        L_r = Lm + dLdr(r, rho, epsilon)*dr;
        
        if (Math.abs((Ms - M_r)/Ms) < maximum && Math.abs((Ls - L_r)/Ls) < maximum) {
            good_surface = true;
            exit_outerzone = true;
        }
        if (exit_outerzone) break;

      
        j++;
        if (j > j_max) {
            System.out.println("Unable to converge in Function Surface --- Exiting");
            good_surface = false;
            exit_outerzone = true;
        }
        if (exit_outerzone) break;

        dr /= 2;
        
        r = rm + dr;
        
    }

    if (!good_surface) {
        System.out.println("The last values obtained by Function Surface were: ");
        System.out.println("     M_r = " + M_r + "   dM_r/Ms = " + (Ms - M_r)/Ms);
        System.out.println("     L_r = " + L_r + "   dL_r/Ls = " + (Ls - L_r)/Ls);
        
    }
    StatStar.dr = dr;
    StatStar.r = r;
    StatStar.ok_surface = good_surface;
    StatStar.M_r = M_r;
    StatStar.L_r = L_r;
    StatStar.kappa = kappa;
    StatStar.epsilon = epsilon;
    StatStar.rho = rho;
    StatStar.rc_flag = rc_flag;
    StatStar.T = T;
    StatStar.P = P;
    StatStar.dlnPdlnT = dlnPdlnT;
    StatStar.gamma = gamma;
    }

    public static void Core(double M_r, double L_r, double P, double T, double X, double Z, double r, double P_0, double T_0, double rho_0, double kappa_0, double epsilon_0, char rc_flag, double dlnPdlnT, boolean good_T){

        final double a_o3 = a_rad_o3;
        final double k = k_B;
        final double converged = 1.0e-08;
        final int i_max = 50;

        rho_0 = M_r/(four_pi_o3*pow(r, 3));  
        
        P_0 = P + (two_pi/3)*G*rho_0*rho_0*r*r;
        
        epsilon_0 = L_r/M_r;
        

   
        double Y   = Helium(X, Z);
        double mu  = Mean_Molecular_Weight(X, Y, Z);

        if (rho_0 > 0) {
            int i = 0;
            T_0 = T;
            
            double dT;
            good_T = true;
            
            do {
                i++;
                double f = rho_0*k*T_0/(mu*m_H) + a_o3*pow(T_0, 4) - P_0;   
                double dfdT = rho_0*k/(mu*m_H) + 4*a_o3*pow(T_0, 3);       
            
                dT = -f/dfdT;
                T_0 += dT;

                if (i > i_max) {
                    System.out.println("Unable to converge on core temperature in Function Core --- Exiting");         
                    good_T = false;
                }
                } while (i <= i_max && Math.abs(dT/T_0) >= converged);
        }
        else {
            T_0 = -T;
            good_T = false;
        }

            if (good_T) {
                kappa_0  = Opacity(T_0, rho_0, X, Z);
                
                dlnPdlnT = PTgradient(P, P_0, T, T_0);
                
                double gamma    = Specific_Heat_Ratio();
                if (dlnPdlnT < (gamma/(gamma - 1))){
                    rc_flag = 'c';
                    
                } 
                    
                else{
                    rc_flag = 'r';
                } 
            }
            else {
                kappa_0  = -99.9;
                dlnPdlnT = -99.9;
                rc_flag  = '*';
            }
            StatStar.rho_0 = rho_0;
            StatStar.P_0 = P_0;
            StatStar.epsilon_0 = epsilon_0;
            StatStar.T_0 = T_0;
            StatStar.ok_core = good_T;
            StatStar.kappa_0 = kappa_0;
            StatStar.dlnPdlnT = dlnPdlnT;
            StatStar.rc_flag = rc_flag;
    }
}
