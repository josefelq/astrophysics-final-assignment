// ***************************************************************************
// 
//  Archivo: Stellar_Structure_Equations.java   
//
//
//  Proposito: Esta clase se encarga de tener las funciones basicas de las estructuras
//  estelares.
//
//****************************************************************************
package statstar;

import static java.lang.Math.pow;
import static statstar.Constants.G;
import static statstar.Constants.four_pi;
import static statstar.Constants.four_ac_o3;
import static statstar.Constants.m_H;
import static statstar.Constants.k_B;
import static statstar.Composition.Helium;
import static statstar.Composition.Mean_Molecular_Weight;
import static statstar.Physics.Density;
import static statstar.Physics.Nuclear;
import static statstar.Physics.Opacity;
import static statstar.Physics.PTgradient;
import static statstar.Physics.Specific_Heat_Ratio;

public class Stellar_Structure_Equations {
    
    public static double Structure_Eqns(int i, double r, double X, double Z, double Pm, double Tm, int step_size_condition, char rc_flag, double[] S, boolean ok){
    
        ok = true;
        double P   = S[0];
        double M_r = S[1];
        double L_r = S[2];
        double T   = S[3];

        double Y   = Helium(X, Z);
        double mu  = Mean_Molecular_Weight(X, Y, Z);
        double rho = Density(T, P, mu, step_size_condition);

        if (rho < 0) {
            System.out.println("Density calculation error in FUNCTION Structure_Eqns");
            ok = false;
        }

        double dfdr = 0;
        switch (i) {
            case 0:
            if (ok) dfdr = dPdr(M_r, rho, r);
            else dfdr = 0;
            break;

        case 1:
            if (ok) dfdr = dMdr(r, rho);
            else dfdr = 0;
            break;

        case 2:
            if (ok) {
                double epsilon  = Nuclear(T, rho, X, Z);
                dfdr = dLdr(r, rho, epsilon);
            }
            else dfdr = 0;
            break;

        case 3:
            if (ok) {
                double kappa    = Opacity(T, rho, X, Z);
                double gamma    = Specific_Heat_Ratio();
                double dlnPdlnT = PTgradient(Pm, P, Tm, T);
                dfdr     = dTdr(kappa, rho, T, L_r, r, mu, M_r, gamma, dlnPdlnT, rc_flag);}
            else dfdr = 0;
            break;
            }
            StatStar.ok_Runge = ok;
    return (dfdr);
    }
    
    public static double dPdr(double M_r, double rho, double r){
        
    return (-G*M_r*rho/(r*r));                      //Eq. (10.6)
    }
    
    public static double dMdr(double r, double rho){

    return (four_pi*r*r*rho);                       //Eq. (10.7)
    }

//Luminosity Gradient
    public static double dLdr(double r, double rho, double epsilon){

    return (four_pi*r*r*rho*epsilon);               //Eq. (10.36)
    }
    
    public static double dTdr(double kappa, double rho, double T, double L_r, double r, double mu, double M_r, double gamma, double dlnPdlnT, char rc_flag){
 
        final double k = k_B;
    
        double gamma_ratio = gamma/(gamma - 1);
        double dTdrfun;
        if (dlnPdlnT > gamma_ratio) {                                       
            dTdrfun = -(kappa*rho/pow(T, 3))*(L_r/(four_pi*r*r))/four_ac_o3;
            rc_flag = 'r';
           
        }
        else {
            dTdrfun = -(1/gamma_ratio)*(mu*m_H/k)*(G*M_r/(r*r));            
            rc_flag = 'c';
        }
        StatStar.rc_flag = rc_flag;
    return (dTdrfun);
    }
}
