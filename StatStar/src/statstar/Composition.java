// ***************************************************************************
// 
//  Archivo: Composition.java   
//
//
//  Proposito: Esta clase contiene las functiones que van a calcular la composicion
//  del gas.
//
//****************************************************************************
package statstar;

public class Composition {
    
   
    public static double Mean_Molecular_Weight(double X, double Y, double Z){
    
        return (1/(2*X + 3*Y/4 + Z/2));
    }
    
    public static double Helium(double X, double Z){
    
        return (1 - X - Z);
    }
    
    public static double CNO(double Z){
    
        return (Z/2);
    }
}
