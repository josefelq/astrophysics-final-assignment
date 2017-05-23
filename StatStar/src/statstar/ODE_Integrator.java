// ***************************************************************************
// 
//  Archivo: ODE_Integrator.java  
//
//
//  Proposito: Esta clase se encarga de tener integrar y hacer calculos.
//
//****************************************************************************
package statstar;

import static java.lang.System.exit;
import java.util.Scanner;
import static statstar.Stellar_Structure_Equations.Structure_Eqns;

public class ODE_Integrator {
    
    public static void RK_4(int n, double x0, double h, double[] y0, double[] y4, double[] f0, boolean ok, double X, double Z, double Pm, double Tm, int step_size_condition, char rc_flag){


    Scanner scan = new Scanner(System.in);
    
    final int array_size = 4;
    double k1[] = new double[array_size];
    double k2[] = new double[array_size];
    double k3[] = new double[array_size];
    double k4[] = new double[array_size];
    double y0temp[] = new double[array_size]; 


    if (n > array_size) {
        char xpause;
        System.out.println("Problem in RK_4 ... n exceeds size of temporary arrays");
        System.out.println("Enter any character to exit: ");
        xpause = scan.next().charAt(0);
        exit(1);
    }

    int i;
    ok = true;
   
    for (i = 0; i < n; i++){
        k1[i] = h*f0[i];}

    for (i = 0; i < n; i++){
        y0temp[i] = y0[i] + k1[i]/2;}

    for (i = 0; i < n; i++){
        k2[i] = h*Structure_Eqns(i, x0 + h/2, X, Z, Pm, Tm, step_size_condition, rc_flag, y0temp, ok);
        if (!ok){
            StatStar.ok_Runge = ok;
            return; 
        } 
            
    }

    for (i = 0; i < n; i++){
        y0temp[i] = y0[i] + k2[i]/2;}

    for (i = 0; i < n; i++){
        k3[i] = h*Structure_Eqns(i, x0 + h/2, X, Z, Pm, Tm, step_size_condition, rc_flag, y0temp, ok);
        if (!ok){
            StatStar.ok_Runge = ok;
            return;
            } 
            
        }

    for (i = 0; i < n; i++){
        y0temp[i] = y0[i] + k3[i];}

    for (i = 0; i < n; i++){
        k4[i] = h*Structure_Eqns(i, x0 + h,   X, Z, Pm, Tm, step_size_condition, rc_flag, y0temp, ok);
        if (!ok){
        
            StatStar.ok_Runge = ok;
            return;
            } 
        }

   
    for (i = 0; i < n; i++){
        y4[i] = y0[i] + k1[i]/6 + k2[i]/3 + k3[i]/3 + k4[i]/6;}
    
      StatStar.ok_Runge = ok;

    }
}
