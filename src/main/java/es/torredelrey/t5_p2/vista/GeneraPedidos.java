/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.torredelrey.t5_p2.vista;

import es.torredelrey.t5_p2.modelo.Clientes;
import es.torredelrey.t5_p2.modelo.Empleados;
import static es.torredelrey.t5_p2.vista.GenerarClientes.GenerarPDF;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Juanfran
 */
public class GeneraPedidos {
    public static void GenerarPDF(List<Empleados> lista){
        Date date=new Date();
        try {
            JasperPrint print;
            InputStream imagen1;
            InputStream imagen2;
            String titulo = "Pedidos";
            imagen1=GeneraPedidos.class.getResourceAsStream("/imagen/pedido.jpg");
            HashMap param=new HashMap();
            param.put("fechaInforme", date);
            param.put("titulo", titulo);
            param.put("imagen1", imagen1);
            
            
            JRDataSource datasource=new JRBeanArrayDataSource(lista.toArray());
            String report="pedidosSubinforme.jasper";
            print=JasperFillManager.fillReport(report, param, datasource);
            JasperExportManager.exportReportToPdfFile(print, "ListaPedidos.pdf");
            JasperViewer.viewReport(print);
            
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
