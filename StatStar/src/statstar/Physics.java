// ***************************************************************************
// 
//  Archivo: Physics.java    
//
//
//  Proposito: Esta clase se encarga de tener todas las functiones que hacen los 
//  procedimientos fisicos requeridos para construir los modulos estelares.
//
//****************************************************************************
package statstar;

import static java.lang.Math.exp;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import static statstar.Composition.CNO;
import static statstar.Composition.Helium;
import static statstar.Constants.a_rad_o3;
import static statstar.Constants.m_H;
import static statstar.Constants.k_B;

public class Physics {
    
    public static double PTgradient(double Pm, double P, double Tm, double T){
    
        double dlnPdlnT = ((Tm + T)/(Pm + P))*((Pm - P)/(Tm - T));
        if (dlnPdlnT > 99.9) dlnPdlnT = 99.9;

        return (dlnPdlnT);
        }
    
    public static double Specific_Heat_Ratio(){
    
        final double monatomic = 5/3.;
        return (monatomic);  
    }
    
    public static double Density(double T, double P, double mu, int step_size_condition){
    
        final double a_o3 = a_rad_o3;
        final double k = k_B;
        
        double P_gas =  P - a_o3*pow(T, 4);
        if (P_gas <= 0 && T > 0) {                          
            switch (step_size_condition){
                case 0:
                P_gas = P;
                break;
                case 1:
                P_gas = 0.001*P;
                break;
                case 2:
                P_gas = 0.0001*P;
                break;
                }
        }

        double rho;
        if (T > 0 && P_gas > 0) rho = P_gas*mu*m_H/(k*T);  
        else rho = -1;
         if (rho < 0) {
             System.out.println("A negative density was computed!");
             System.out.println("Sorry but I am not programmed to handle this new physics :-)");
             System.out.println("Terminating calculation with: ");
             System.out.println("         T     = " + T);
             System.out.println("         P     = " + P);
             System.out.println("         P_gas = " + P_gas);  
         }
          return (rho);
    }
    
    public static double Opacity(double T, double rho, double X, double Z){
    
        final double A_bf = 4.34e21;
        final double A_ff = 3.68e18;
        final double A_es = 0.02;
        final double A_Hm = 7.9e-34;
        final double g_ff = 1;
    
        double tog_bf = 0.708*pow(rho*(1 + X), 0.2);                   

        double kappa_bf = (A_bf/tog_bf)*Z*(1 + X)*rho/pow(T, 3.5);      
        double kappa_ff = A_ff*g_ff*(1 - Z)*(1 + X)*rho/pow(T, 3.5);    
        double kappa_es = A_es*(1 + X);                                 

        double kappa_Hminus;
        
        if ((T > 3000 && T < 6000) && (rho > 1e-10 && rho < 1e-5) && (Z > 0.001 && Z < 0.03))
            kappa_Hminus = A_Hm*(Z/0.02)*sqrt(rho)*pow(T, 9);           
        else kappa_Hminus = 0;

        return (kappa_bf + kappa_ff + kappa_es + kappa_Hminus);    
    }
    
    public static double Optical_Depth_Change(double kappa, double kappam, double rho, double rhom, double r, double rm){
        
        return (-(kappa*rho + kappam*rhom)*(r - rm)/2);                
    }
    
    public static double Nuclear(double T, double rho, double X, double Z){

    final double fpp = 1, f3a = 1;                                  
    final double onethird = 1/3.;
    final double twothirds = 2*onethird;
    final double fourthirds = 4*onethird;
    final double fivethirds = 5*onethird;
    final double A_pp = 0.241, A_CNO = 8.67e20, A_He = 50.9;        

    double T6 = T*1.0e-06;
    double T8 = T*1.0E-08;

    double psipp = 1 + 1.412e8*(1/X - 1)*exp(-49.98*pow(T6, -onethird));
    double Cpp = 1 + 0.0123*pow(T6, onethird) + 0.0109*pow(T6, twothirds) + 0.000938*T6;
    double eps_pp = A_pp*rho*X*X*fpp*psipp*Cpp*pow(T6, -twothirds)*exp(-33.80*pow(T6, -onethird)); //Eq. (10.46)

    double XCNO = CNO(Z);
    double CCNO = 1 + 0.0027*pow(T6, onethird) - 0.00778*pow(T6, twothirds) - 0.000149*T6;
    double eps_CNO = A_CNO*rho*X*XCNO*CCNO*pow(T6, -twothirds)*exp(-152.28*pow(T6, -onethird));     //Eq. (10.58)

    double Y = Helium(X, Z);
    double eps_He = A_He*rho*rho*pow(Y, 3)/pow(T8, 3)*f3a*exp(-44.027/T8);                          //Eq. (10.62)

    return (eps_pp + eps_CNO + eps_He);
    }
}

