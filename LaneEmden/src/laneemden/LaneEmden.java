package laneemden;

import java.util.ArrayList;
import java.util.Scanner;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class LaneEmden extends ApplicationFrame {
    
    private static ArrayList xValues;
    
    private static ArrayList yValues;
    
    private static int n;
    
    private static double finSol;

    /**
     * A demonstration application showing an XY series containing a null value.
     *
     * @param title the frame title.
     */
    private LaneEmden(final String title) {

        super(title);
        final XYSeries series = new XYSeries("Solution is: "+finSol);
        
        for(int i=0;i<xValues.size();i++){
            
            series.add((double)xValues.get(i),(double)yValues.get(i));
        }
        final XYSeriesCollection data = new XYSeriesCollection(series);
        final JFreeChart chart = ChartFactory.createXYLineChart(
                "Lane Emden Equation solution for n="+n,
                "ξ",
                "θ",
                data,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(600, 500));
        setContentPane(chartPanel);

    }
    /**
     *
     * @param args ignored.
     */
    public static void main(final String[] args) {
        xValues=new ArrayList();
        yValues=new ArrayList();
        
        ArrayList accept=new ArrayList();
        accept.add(0);
        accept.add(1);
        accept.add(2);
        accept.add(3);
        accept.add(4);
        accept.add(5);
        
        System.out.print("Type polytropic index to calculate (values must be 0,1,2,3,4,5): ");
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        while(!accept.contains(n)){
            System.out.println("You typed an invalid polytropic index.");
            System.out.print("Type polytropic index to calculate (values must be 0,1,2,3,4,5): ");
            n = sc.nextInt();
        }
        
        
        solution();

        final LaneEmden demo = new LaneEmden("Fin");
        demo.pack();
        RefineryUtilities.centerFrameOnScreen(demo);
        demo.setVisible(true);

    }

    public static void solution() {
        double thet, dxdt, dt, xlast = 0, dxdtlast = 0, ep;

        thet = 1;
        dxdt = 0;
        ep = 6.0E-6;
        
        xValues.add(ep);
        yValues.add(thet);

        
        dt = 0.001;
        boolean negativeSolution = false;
        for (int i = 0; i < 999999999 && !negativeSolution; i++) {
            dxdt = dxdt - (2.0 * dxdt / ep + Math.pow(thet, n)) * dt;
            thet = thet + dxdt * dt;
            ep = ep + dt;
            xValues.add(ep);
            yValues.add(thet);
            if (thet < 0) {
                finSol = ep + (thet / (xlast - thet)) * dt;
                negativeSolution = true;
            } else {
                xlast = thet;
                dxdtlast = dxdt;
            }

        }
    }

}
