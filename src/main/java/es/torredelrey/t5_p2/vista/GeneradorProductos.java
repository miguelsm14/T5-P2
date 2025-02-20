/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.vista;

import es.torredelrey.t5_p2.modelo.Productos;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author David
 */
public class GeneradorProductos {
    private static InputStream torre;
    public static void leerInforme(List<Productos> listaProductos){
    try {
            JasperPrint print;
            HashMap parametro = new HashMap();
            torre = GeneradorProductos.class.getResourceAsStream("/imagenen/torre.jpg");
            parametro.put("fecha", "20/02/2025");
            parametro.put("torre", torre);
            //parametro.put("SUBREPORT_DIR", "\\T5_P1\\T5_P1\\");+
            
            JRDataSource dataSource = new JRBeanArrayDataSource(listaProductos.toArray());
            String report = "ListadoProducto.jasper";
            print = JasperFillManager.fillReport(report, parametro,dataSource);
            JasperExportManager.exportReportToPdfFile(print, "ListadoDeProductos.pdf");
            JasperViewer.viewReport(print);
        } catch (JRException ex) {
            Logger.getLogger(GeneradorProductos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
