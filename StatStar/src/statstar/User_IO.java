// ***************************************************************************
// 
//  Archivo: User_IO.java    
//
//
//  Proposito: Esta clase se encarga de tener todas las functiones que reciben 
//  los datos del usuario
//
//****************************************************************************
package statstar;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.Scanner;
import static statstar.Composition.Helium;
import static statstar.Constants.L_Sun;
import static statstar.Constants.M_Sun;
import static statstar.Constants.R_Sun;
import static statstar.Constants.four_pi;
import static statstar.Constants.sigma;

public class User_IO {
    
    public static void Initial_Model(double Msolar, double Lsolar, double Rsolar, double Ms, double Ls, double Rs, double Teff, double X, double Y, double Z, char all_new){
 
        Scanner scan = new Scanner(System.in);
        char y_n = 0;

        boolean All_parameters = true;
        boolean Fix_parameters = true;
        boolean io_ok = false;

    while (All_parameters) {
        if (all_new == 'Y' || all_new == 'y' || all_new == 'A' || all_new == 'a') {
            do {
                System.out.print("Enter the mass (in solar units)       :  Msolar = ");
                if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                }            
                else{
                    
                    Msolar = scan.nextDouble();
                    if(Msolar <= 0){
                        System.out.println("Invalid value entered - please try again");
                        scan.nextLine();
                    }
                    else{
                        StatStar.Msolar = Msolar;
                        scan.nextLine();
                        io_ok = true;
                    }
                }
                
            } while (!io_ok);

            io_ok = false;
            
            do {
                System.out.print("Enter the effective temperature (in K):  Teff   = ");
                if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                }            
                else{
                    
                    Teff = scan.nextDouble();
                    if(Teff <= 0){
                        System.out.println("Invalid value entered - please try again");
                        scan.nextLine();
                    }
                    else{
                        StatStar.Teff = Teff;
                        io_ok = true;
                        scan.nextLine();
                    }
                } 
            } while (!io_ok);

            io_ok = false;
            
            do {
                System.out.print("Enter the luminosity (in solar units) :  Lsolar = ");
                if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                }            
                else{
                    
                    Lsolar = scan.nextDouble();
                    if(Lsolar <= 0){
                        System.out.println("Invalid value entered - please try again");
                        scan.nextLine();
                    }
                    else{
                        StatStar.Lsolar = Lsolar;
                        io_ok = true;
                        scan.nextLine();
                    }
                } 
            } while (!io_ok);

            io_ok = false;
            
            do {
                do {
                    System.out.print("Enter the mass fraction of hydrogen   :  X      = ");
                    if(!scan.hasNextDouble()){
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                }            
                else{
                    
                    X = scan.nextDouble();
                    if(X < 0 || X > 1){
                        System.out.print("\n0 <= X <= 1 is required");
                        scan.nextLine();
                        }
                    else{
                        StatStar.X = X;
                        io_ok = true;
                        scan.nextLine();
                        }
                    } 
                } while (!io_ok);

                io_ok = false;
                
                do {
                    System.out.print("Enter the mass fraction of metals     :  Z      = ");
                    if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                    }            
                    else{
                    
                    Z = scan.nextDouble();
                    if(Z < 0 || Z > 1){
                        System.out.print("\n0 <= Z <= 1 is required");
                        scan.nextLine();
                    }
                    else{
                        StatStar.Z = Z;
                        io_ok = true;
                        scan.nextLine();
                    }
                    } 
                } while (!io_ok);

                Y = Helium(X, Z);
                if (Y < 0 || Y > 1) {
                    System.out.print("Note that 0 <= X + Z <= 1 is required\n");
                    System.out.println("  Please reenter composition values");
                io_ok = false;
                }
                StatStar.Y = Y;
            } while (!io_ok);
        }

        
        Ms = Msolar*M_Sun;
        Ls = Lsolar*L_Sun;
        Rs = sqrt(Ls/(four_pi*sigma*pow(Teff, 4)));     
        Rsolar = Rs/R_Sun;    
        
        

       
        while (Fix_parameters) {
            System.out.println();
            System.out.print("Your model parameters are:               " + Msolar + " solar" + "\n");
            System.out.print("                                         ");
            System.out.print("Teff   = " + Teff + " K    " + "\n");
            System.out.print("                                         ");
            System.out.print("L      = " + Lsolar + " solar" + "\n");
            System.out.print("                                         ");
            System.out.print("R      = " + Rsolar + " solar" + "\n");
            System.out.print("                                         ");
            System.out.print("X      = " + X + "\n");
            System.out.print("                                         ");
            System.out.print("Y      = " + Y + "\n");
            System.out.print("                                         ");
            System.out.println("Z      = " + Z);
         
            io_ok = false;
            System.out.print("\n" + "Are these values ok (y/n)? ");
            do {
                if(!scan.hasNext()){
                    
                     System.out.print("Please answer Yes (y) or No (n):  ");
                     scan.nextLine();
                }            
                else{
                    
                    y_n = scan.next().charAt(0);
                    if(!(y_n == 'Y' || y_n == 'y' || y_n == 'N' || y_n == 'n')){
                        System.out.print("Please answer Yes (y) or No (n):  ");
                        scan.nextLine();
                    }
                    else{
                        io_ok = true;
                        scan.nextLine();
                    }
                }
            } while (!io_ok);
            
            if (y_n == 'Y' || y_n == 'y') {
                All_parameters = false;
                Fix_parameters = false;}
            else {
                all_new = 'N';
                StatStar.all_new = all_new;
                Change_Model(Msolar, Lsolar, Rsolar, Ms, Ls, Rs, Teff, X, Y, Z, all_new);
                if (all_new == 'E' || all_new == 'e') {
                    All_parameters = false;
                    Fix_parameters = false;}
                if (all_new == 'A' || all_new == 'a') Fix_parameters = false;}
        }
    }
        StatStar.Ms = Ms;
        StatStar.Ls = Ls;
        StatStar.Rs = Rs;
        StatStar.Rsolar = Rsolar;
        StatStar.X = X;
        StatStar.Y = Y;
        StatStar.Z = Z;
        StatStar.Msolar = Msolar;
        StatStar.Teff = Teff;
        StatStar.Lsolar = Lsolar;  
    }
    
    public static void Change_Model(double Msolar, double Lsolar, double Rsolar, double Ms, double Ls, double Rs, double Teff, double X, double Y, double Z, char all_new){

        Scanner scan = new Scanner(System.in);
        char y_n = 0;
        boolean io_ok = false;

        if (all_new == 'Y' || all_new == 'y') {
            System.out.println("\n" + "Would you like to run another model?\n" 
                + "Your previous results will be overwritten in the output file. (y/n): ");
        do {
               if(!scan.hasNext()){
                    
                     System.out.print("Please answer Yes (y) or No (n):  ");
                     scan.nextLine();
                }            
                else{       
                    y_n = scan.next().charAt(0);
                    if(!(y_n == 'Y' || y_n == 'y' || y_n == 'N' || y_n == 'n')){
                        System.out.print("Please answer Yes (y) or No (n):  ");
                        scan.nextLine();
                    }
                    else{
                        io_ok = true;
                        scan.nextLine();
                    }
                }
        } while (!io_ok);

    
        if (y_n == 'Y' || y_n == 'y') {
            System.out.print("\nWhich variable would you like to change?\n");
            System.out.print( "     M = Mass                          Current value = " + Msolar + " solar\n");
            System.out.print("     T = effective Temperature         Current value = " + Teff + " K    \n");
            System.out.print("     L = Luminosity                    Current value = " + Lsolar + " solar\n");
            System.out.print( "     X = hydrogen mass mraction (X)    Current value = " + X + "\n");
            System.out.print("     Z = metals mass fraction (Z)      Current value = " + Z + "\n");
            System.out.print("     A = select an All new set of model parameters\n");
            System.out.println("     E = Exit the calculation\n");
   
            System.out.print("Select a letter: ");
            
            io_ok = false;
            do {
                if(!scan.hasNext()){
                    
                     System.out.print("Please respond with one of the options listed:  ");
                     scan.nextLine();
                }            
                else{       
                    all_new = scan.next().charAt(0);
                    if (!(all_new == 'M' || all_new == 'm' ||
                     all_new == 'T' || all_new == 't' ||
                     all_new == 'L' || all_new == 'l' ||
                     all_new == 'X' || all_new == 'x' ||
                     all_new == 'Z' || all_new == 'z' ||
                     all_new == 'A' || all_new == 'a' ||
                     all_new == 'E' || all_new == 'e')){
                        System.out.print("Please respond with one of the options listed:  ");
                        scan.nextLine();
                    }
                    else{
                        StatStar.all_new = all_new;
                        io_ok = true;
                        scan.nextLine();
                    }
                }
            } while (!io_ok);
        }
    }
    else {
        y_n = 'Y';
        System.out.print("\nWhich variable would you like to change?\n");
        System.out.print("     M = Mass\n");
        System.out.print("     T = effective Temperature\n");
        System.out.print("     L = Luminosity\n");
        System.out.print("     X = hydrogen mass fraction (X)\n");
        System.out.print("     Z = metals mass fraction (Z)\n");
        System.out.print("     A = select an All new set of model parameters\n");
        System.out.println("     E = Exit the calculation\n");
    
        System.out.print("Select a letter: ");
        io_ok = false;
        do {
            if(!scan.hasNext()){
                    
                     System.out.print("Please respond with one of the options listed:  ");
                     scan.nextLine();
                }            
                else{       
                    all_new = scan.next().charAt(0);
                    if (!(all_new == 'M' || all_new == 'm' ||
                     all_new == 'T' || all_new == 't' ||
                     all_new == 'L' || all_new == 'l' ||
                     all_new == 'X' || all_new == 'x' ||
                     all_new == 'Z' || all_new == 'z' ||
                     all_new == 'A' || all_new == 'a' ||
                     all_new == 'E' || all_new == 'e')){
                        System.out.print("Please respond with one of the options listed:  ");
                        scan.nextLine();
                    }
                    else{
                        StatStar.all_new = all_new;
                        io_ok = true;
                        scan.nextLine();
                    }
                }
            } while (!io_ok);

        if (all_new == 'E' || all_new == 'e') y_n = 'N';
    }

    
    if (y_n == 'Y' || y_n == 'y') {
        switch (all_new) {
            case 'M':
            case 'm':
                io_ok = false;
                do {
                    System.out.print("Enter the mass (in solar units)       :  Msolar = ");
                    if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                    }            
                    else{
                    
                    Msolar = scan.nextDouble();
                    if(Msolar <= 0){
                        System.out.println("Invalid value entered - please try again");
                        scan.nextLine();
                    }
                    else{
                        scan.nextLine();
                        Ms = Msolar*M_Sun;
                        StatStar.Ms = Ms;
                        io_ok = true;
                    }
                }
                } while (!io_ok);
             
                break;
           
            case 'T':
            case 't':
                io_ok = false;
                do {
                    System.out.print("Enter the effective temperature (in K):  Teff   = ");
                    if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                    }            
                    else{
                    
                    Teff = scan.nextDouble();
                    if(Teff <= 0){
                        System.out.println("Invalid value entered - please try again");
                        scan.nextLine();
                    }
                    else{
                        io_ok = true;
                        scan.nextLine();
                        Rs = sqrt(Ls/(four_pi*sigma*pow(Teff, 4)));    
                        Rsolar = Rs/R_Sun;
                        StatStar.Teff = Teff;
                        StatStar.Rs = Rs;
                        StatStar.Rsolar = Rsolar;
                    }
                }                                  
                } while (!io_ok);
               
                break;

            case 'L':
            case 'l':
                io_ok = false;
                do {
                    System.out.print("Enter the luminosity (in solar units) :  Lsolar = ");
                    if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                    }            
                    else{
                    
                        Lsolar = scan.nextDouble();
                        if(Lsolar <= 0){
                            System.out.println("Invalid value entered - please try again");
                            scan.nextLine();
                    }
                    else{
                        io_ok = true;
                        scan.nextLine();
                        Ls = Lsolar*L_Sun;
                        Rs = sqrt(Ls/(four_pi*sigma*pow(Teff, 4)));     
                        Rsolar = Rs/R_Sun;
                        StatStar.Lsolar = Lsolar;
                        StatStar.Rs = Rs;
                        StatStar.Rsolar = Rsolar;
                        StatStar.Ls = Ls;
                    }
                }                         
                } while (!io_ok);
               
                break;

            case 'X':
            case 'x':
                io_ok = false;
                do {
                    System.out.print("Enter the mass fraction of hydrogen   :  X      = ");
                    if(!scan.hasNextDouble()){
                        System.out.println("Invalid value entered - please try again");
                        scan.nextLine();
                    }            
                    else{
                    
                        X = scan.nextDouble();
                        if(X < 0 || X > 1){
                            System.out.print("\n0 <= X <= 1 is required");
                            scan.nextLine();
                            }
                    else{
                            StatStar.X = X;
                            io_ok = true;
                            scan.nextLine();
                        }
                    } 
                } while (!io_ok);
                           
                Y = Helium(X, Z);
                if (Y < 0 || Y > 1) {
                    do {
                        System.out.println("Note that 0 <= X + Z <= 1 is required");
                        io_ok = false;
                        do {
                            System.out.print("  Please try again:                      X      = ");
                            if(!scan.hasNextDouble()){
                                System.out.println("Invalid value entered - please try again");
                                scan.nextLine();
                            }            
                            else{
                    
                                X = scan.nextDouble();
                            if(X < 0 || X > 1){
                                System.out.print("\n0 <= X <= 1 is required");
                                scan.nextLine();
                            }
                            else{
                                    StatStar.X = X;
                                    io_ok = true;
                                    scan.nextLine();
                                }
                            } 
                        } while (!io_ok);
                        
                        Y = Helium(X, Z);
                        StatStar.Y = Y;
                    } while (Y < 0 || Y > 1);
                }
                all_new = 'n';
                break;

            case 'Z':
            case 'z':
                io_ok = false;
                do {
                    System.out.print("Enter the mass fraction of metals     :  Z      = ");
                    if(!scan.hasNextDouble()){
                    
                     System.out.println("Invalid value entered - please try again");
                     scan.nextLine();
                    }            
                    else{
                    
                    Z = scan.nextDouble();
                    if(Z < 0 || Z > 1){
                        System.out.print("\n0 <= Z <= 1 is required");
                        scan.nextLine();
                    }
                    else{
                        StatStar.Z = Z;
                        io_ok = true;
                        scan.nextLine();
                    }
                    } 
                }while (!io_ok);

                Y = Helium(X, Z);
                if (Y < 0 || Y > 1) {
                    do {
                        io_ok = false;
                        System.out.println("Note that 0 <= X + Z <= 1 is required" );
                        do {
                            System.out.print("  Please try again:                      Z      = ");
                            if(!scan.hasNextDouble()){
                    
                                System.out.println("Invalid value entered - please try again");
                                scan.nextLine();
                            }            
                            else{
                    
                                Z = scan.nextDouble();
                            if(Z < 0 || Z > 1){
                                System.out.print("\n0 <= Z <= 1 is required");
                                scan.nextLine();
                            }
                            else{
                                StatStar.Z = Z;
                                io_ok = true;
                                scan.nextLine();
                            }
                        }
                        }while (!io_ok);
                        Y = Helium(X, Z);
                        StatStar.Y = Y;
                    } while (Y < 0 || Y > 1);
                }
            
                break;

            case 'E':
            case 'e':
                y_n = 'N';
                break;

            case 'A':
            case 'a':
                y_n = 'Y';
                break;

            default:
                all_new = 'y';
        }
    }
    else {
            all_new = 'E';          
    }
        StatStar.Ms = Ms;
        StatStar.Ls = Ls;
        StatStar.Rs = Rs;
        StatStar.Rsolar = Rsolar;
        StatStar.X = X;
        StatStar.Y = Y;
        StatStar.Z = Z;
        StatStar.Msolar = Msolar;
        StatStar.Teff = Teff;
        StatStar.Lsolar = Lsolar;  
    }
    
    public static void Too_Many_Zones(int i, double Msolar, double Ms, double M_r, double Lsolar, double Ls, double L_r, double r, double Rs, double Rsolar, double Teff, double X, double Y, double Z, double P_0, double T_0, double rho_0, double kappa_0, double epsilon_0, char rc_flag){

        System.out.print("\n");
        System.out.print("The maximum number of zones has been exceeded for this model - Sorry\n");
        System.out.print("The conditions at the time the model was terminated were: \n");
        System.out.print("    Surface Conditions:            Last Zone Calculated:\n");
        System.out.print("    -------------------            ---------------------\n");
        System.out.print("    M    = " + Msolar + " solar     M_r/Ms  = " + (M_r/Ms) + "\n");
        System.out.print("    Teff = " + Teff + " K         L_r/LS  = " + (L_r/Ls) + "\n");
        System.out.print("    L    = " + Lsolar + " solar     r/Rs    = " + (r/Rs) + "\n");
        System.out.print("    R    = " + Rsolar + " solar     P       = " + P_0 + " N/m^2\n");
        System.out.print("    X    = " + X + "           T       = " + T_0 + " K\n");
        System.out.print("    Y    = " + Y + "           rho     = " + rho_0 + " kg/m^3\n");
        System.out.print("    Z    = " + Z + "           kappa   = " + kappa_0 + " m^2/kg\n");
        System.out.println("                                   epsilon = " + epsilon_0 + " W/kg");

    if (rc_flag == 'r') System.out.println("                                  The core is RADIATIVE");
    else    System.out.println("                                  The core is CONVECTIVE");

}


    public static void Final_Results(int i, double Msolar, double Ms, double M_r, double Lsolar, double Ls, double L_r, double r, double Rs, double Rsolar, double Teff, double X, double Y, double Z, double P, double T, double rho, double kappa, double epsilon, double P_0, double T_0, double rho_0, double kappa_0, double epsilon_0, char rc_flag){

        System.out.print("\n\n");
        System.out.print("*********************THE INTEGRATION HAS BEEN COMPLETED*********************\n");
        System.out.print("    Surface Conditions:            Last Zone Calculated:\n");
        System.out.print("    -------------------            ---------------------\n");
        System.out.print("    M    = " + Msolar + " solar     M_r/Ms  = " + (M_r/Ms) + "\n");
        System.out.print("    Teff = " + Teff + " K         L_r/LS  = " + (L_r/Ls) + "\n");
        System.out.print("    L    = " + Lsolar + " solar     r/Rs    = " + (r/Rs) + "\n");
        System.out.print( "    R    = " + Rsolar + " solar     P       = " + P_0 + " N/m^2\n");
        System.out.print("    X    = " + X + "           T       = " + T_0 + " K\n");
        System.out.print("    Y    = " + Y + "           rho     = " + rho_0 + " kg/m^3\n");
        System.out.print("    Z    = " + Z + "           kappa   = " + kappa_0 + " m^2/kg\n");
        System.out.println("                                   epsilon = " + epsilon_0 + " W/kg");

    if (rc_flag == 'r')  System.out.println("                                   The core is RADIATIVE");
    else           System.out.println("                                   The core is CONVECTIVE");

    System.out.print("\nFor your information, the conditions in the last zone above the core are:\n");
    System.out.print("                                   P       = " + P + " N/m^2\n");
    System.out.print( "                                   T       = " + T + " K\n");
    System.out.print("                                   rho     = " + rho + " kg/m^3\n");
    System.out.print("                                   kappa   = " + kappa + " m^2/kg\n");
    System.out.println("                                   epsilon = " + epsilon + " W/kg");
   
    System.out.println("\nThe number of mass shells in this model: " + i 
            + "\nThe details of the model are available in ZAMSmodel.txt");
    }
}
