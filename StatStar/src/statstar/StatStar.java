// ***************************************************************************
// 
//  Archivo: StatStar.java   
//
//
//  Proposito: Este archivo se encarga de unir todas las clases para crear un modelo
//  ZAMS. 
//
//     ********************
//     **ESTE ES EL MAIN***
//     ********************
//
//****************************************************************************
package statstar;

import java.text.DecimalFormat;
import static java.lang.System.exit;
import static statstar.User_IO.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.FileOutputStream;
import static statstar.Boundary_Conditions.*;
import static statstar.Physics.*;
import static statstar.Composition.*;
import static statstar.Stellar_Structure_Equations.*;
import static statstar.ODE_Integrator.*;


public class StatStar {

    public static double Msolar;
    public static double Lsolar;
    public static double Rsolar;
    public static double Ms;
    public static double Ls;
    public static double Rs;
    public static double Teff;
    public static double X;
    public static double Y;
    public static double Z;
    public static double dr;
    public static double r;
    public static double P;
    public static double T;
    public static double M_r;
    public static double L_r;
    public static double rho;
    public static double kappa;
    public static double epsilon;
    public static double dlnPdlnT;
    public static double gamma;
    public static double P_0;
    public static double T_0;
    public static double rho_0;
    public static double epsilon_0;
    public static double kappa_0;
    public static char all_new;
    public static char rc_flag;
    public static boolean ok_Runge;
    public static boolean ok_surface;
    public static boolean ok_core;
    public static void main(String[] args) throws FileNotFoundException {
       
        DecimalFormat format = new DecimalFormat("0.###E0");
        final double dr_over_r = 0.001;                
        final double M_fraction_limit = 0.01;           
        final double L_fraction_limit = 0.10;           
        final double r_fraction_limit = 0.02;          
        final int    maximum_zones = 10000;             
        final int    n_surface = 1; 
        String result;
    
        
        boolean adjust_step_size = true;                   
    
        final int n = 4;                               
        double dfdr0[] = new double[n];                               

        double Mm, Lm, rm , Pm, Tm, Xm , Zm ;
        double rhom, kappam, taum, epsilonm, tau = 0;  
        int step_size_condition;

//---------------------------------------------------------------------

                                    //Los parametros de inicializacion

        all_new = 'Y';
        
        while (!(all_new == 'E' || all_new == 'e')){

            int i = 0;                                  
      
        Initial_Model(Msolar, Lsolar, Rsolar, Ms, Ls, Rs, Teff, X, Y, Z, all_new);
        
        if (all_new == 'E' || all_new == 'e') exit(0);
        
        //Crear el archivo de texto
        try{
            
            File file = new File("ZAMSmodel.txt");
            FileOutputStream fos = new FileOutputStream(file, false);
            PrintWriter outdata = new PrintWriter(fos);
        
      

        //Escribir los resultados en el archivos
        outdata.println("                                             " + "A ZAMS Stellar Model\n" 
           + "                                             " + "--------------------\n");
 
        outdata.println("                                             " + "M    = " + Msolar + " solar"
        + "\n" +  "                                             " + "L    = " + Lsolar + " solar" + "\n"
        + "                                             " + "R    = " + Rsolar + " solar" + "\n"
        + "                                             " + "Teff = " + Teff + " K    " + "\n"
        + "                                             " + "X    = " + X + "\n"
        + "                                             " + "Y    = " + Y + "\n"
        + "                                             " + "Z    = " + Z + "\n\n\n");
        //Los valores anteriores
        Pm   = 0;
        Tm   = 0;
        Xm   = X;
        Zm   = Z;
        rm   = Rs;
        taum = 0;
        rhom = 0;
        kappam = 0;
        epsilonm = 0;
        dlnPdlnT = 99.9;
        rc_flag = 'r';

        outdata.println(" zone      r               tau          1-M_r/Ms         L_r              T              P              rho        "
        + "     kap            eps        dlnPdlnT");
        outdata.println(i + "        " + format.format(rm) + "        " + format.format(0) + "        " + format.format(0) + "        " + format.format(Ls) + "        " + 
                "        " + format.format(Tm) + "        " + format.format(Pm) + "        " + format.format(rhom) + "        " + format.format(kappam) + "        " + 
               format.format(epsilonm) + "        " + rc_flag + "  " + dlnPdlnT);
      
               

        Mm = Ms;
        Lm = Ls;
        dr = -dr_over_r*Rs;
        step_size_condition = 0;
        
        do{
            i++;

            //Actualizar valores
            if (i > 1){
                Mm = M_r;
                Lm = L_r;
                rm = r;
                Pm = P;
                Tm = T;
                Xm = X;
                Zm = Z;
                taum = tau;
                rhom = rho;
                kappam = kappa;
                epsilonm = epsilon;
            }
        
            Surface(i, Mm, Lm, Mm, Lm, rm, Pm, Tm, X, Z, dr, r, P, T, M_r, L_r, rho, kappa, epsilon, dlnPdlnT, gamma, rc_flag, step_size_condition, ok_surface);
            
            if (!ok_surface) break;

            
            tau = taum + Optical_Depth_Change(kappa, kappam, rho, rhom, r, rm);
            
  
            
            outdata.println(i + "        " + format.format(r)  + "        " + format.format(tau) + "         " + format.format((1 - M_r/Ms)) +
                    "        " + format.format(L_r) + "        " + format.format(T) + "        " + format.format(P) + "        " + format.format(rho) + "        " +
                    format.format(kappa) + "        " + format.format(epsilon) + "        " + rc_flag + " " + format.format(dlnPdlnT));
         
            
        } while (i < n_surface);

        double mu;
        ok_surface = true;
        if (ok_surface){
            //Metiendo los primeros valores derivados para realizar los calculos
            Y        = Helium(X, Z);
            mu       = Mean_Molecular_Weight(X, Y, Z);
            gamma    = Specific_Heat_Ratio();
            dlnPdlnT = PTgradient(Pm, P, Tm, T);
            
            dfdr0[0] = dPdr(M_r, rho, r);
            dfdr0[1] = dMdr(r, rho);
            dfdr0[2] = dLdr(r, rho, epsilon);
            dfdr0[3] = dTdr(kappa, rho, T, L_r, r, mu, M_r, gamma, dlnPdlnT, rc_flag);

            //loop de integracion
            boolean exit_main = false;
            while (!exit_main){
                i++;
                
                //Actualizando datos
                Mm = M_r;
                Lm = L_r;
                Pm = P;
                Tm = T;
                Xm = X;
                Zm = Z;
                rm = r;
                taum = tau;
                rhom = rho;
                kappam = kappa;
                epsilonm = epsilon;
        
                double PMLT0[] = new double[]{Pm, Mm, Lm, Tm};
                double PMLT[] = new double[4];
                RK_4(n, rm, dr, PMLT0, PMLT, dfdr0, ok_Runge, X, Z, Pm, Tm, step_size_condition, rc_flag);
                if (!ok_Runge) exit_main = true;
                
                if (ok_Runge){
                    //Resultados
                    P   = PMLT[0];
                    M_r = PMLT[1];
                    L_r = PMLT[2];
                    T   = PMLT[3];

                    //Computar los resultados para el proximo loop
                    Y   = Helium(X, Z);
                    mu  = Mean_Molecular_Weight(X, Y, Z);
                    rho = Density(T, P, mu, step_size_condition);
                    kappa    = Opacity(T, rho, X, Z);
                    dlnPdlnT = PTgradient(Pm, P, Tm, T);
                    epsilon  = Nuclear(T, rho, X, Z);      
                    dfdr0[0] = dPdr(M_r, rho, r);
                    dfdr0[1] = dMdr(r, rho);
                    dfdr0[2] = dLdr(r, rho, epsilon);
                    dfdr0[3] = dTdr(kappa, rho, T, L_r, r, mu, M_r, gamma, dlnPdlnT, rc_flag);
                    
                    tau = taum + Optical_Depth_Change(kappa, kappam, rho, rhom, rm + dr, rm);
                    
                    
                    outdata.println(i + "        " + 
                            format.format(r) + "        " + format.format(tau) + "        " + format.format((1 - M_r/Ms)) 
                            + "        " + format.format(L_r) + "        " + format.format(T) + "        " + format.format(P) + "        " + format.format(rho) 
                            + "        " + format.format(kappa) + "        " + format.format(epsilon) + "        " + rc_flag + "  " + format.format(dlnPdlnT));

                    if ((M_r/Ms < M_fraction_limit && L_r/Ls < L_fraction_limit && r/Rs < r_fraction_limit)
                        || T < 0 || P < 0) exit_main = true;
                    else if (i > maximum_zones) {
                        Too_Many_Zones(i, Msolar, Ms, M_r, Lsolar, Ls, L_r, r, Rs, Rsolar, Teff, X, Y, Z, P_0, T_0, rho_0, kappa_0, epsilon_0, rc_flag);
                        ok_Runge = false;
                        exit_main = true;}

                    if (!exit_main){
                    
                        if (adjust_step_size){
                            switch (step_size_condition){
                                case 0:
                                    if (M_r < 0.99*Ms) {
                                        dr = -Rs/100;
                                        step_size_condition = 1;}
                                    break;
                                case 1:
                                    if (Math.abs(dr) > 5*r) {
                                        dr /= 10;
                                        step_size_condition = 2;}
                                    break;
                            }
                        }
                    }
                }
                
                r += dr;
            }

           
            if (ok_Runge) {
                //Condiciones del core
                i++;
                Core(M_r, L_r, P, T, X, Z, r, P_0, T_0, rho_0, kappa_0, epsilon_0, rc_flag, dlnPdlnT, ok_core);
                if (!ok_core) {
                    System.out.println("\nWARNING:  There was a problem with the core extrapolation routine");}

                tau += Optical_Depth_Change(kappa_0, kappa, rho_0, rho, 0, r);
                outdata.println(i + " " + 0 + " " + tau + " " + (1 - M_r/Ms) + " " + L_r + " " + T_0 + " " + P_0 + " " + rho_0 + " " + kappa_0 + " " + epsilon_0 + "  " + rc_flag + " " + dlnPdlnT);
                
                //imprimir primeras y ultimas condiciones 
                Final_Results(i, Msolar, Ms, M_r, Lsolar, Ls, L_r, r, Rs, Rsolar, Teff, X, Y, Z,
                    P, T, rho, kappa, epsilon, P_0, T_0, rho_0, kappa_0, epsilon_0, rc_flag);
            }
        }

        //En caso de querer otro modelo
        all_new = 'Y';
        Change_Model(Msolar, Lsolar, Rsolar, Ms, Ls, Rs, Teff, X, Y, Z, all_new);
        outdata.close();
    }
       catch(FileNotFoundException ex){
                System.out.print(" Unable to open output file 'ZAMSmodel.txt' --- Terminating calculation" );
                System.out.print("\n\n");
                System.out.print("Enter any character to quit: ");
                exit(1);
        }
     }
        
    }
    
}
