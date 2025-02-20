package es.torredelrey.t5_p2.vista;

import es.torredelrey.t5_p2.modelo.Clientes;
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


public class GenerarClientes {
    
    public static void GenerarPDF(List<Clientes> lista){
        Date date=new Date();
        try {
            JasperPrint print;
            InputStream imagen;
            imagen=GenerarClientes.class.getResourceAsStream("/imagen/images.jpeg");
           
            HashMap param=new HashMap();
            param.put("titulo", "DATOS DE LOS CLIENTES");
            param.put("FECHA", date);
            param.put("imagen", imagen);
            
            
            JRDataSource datasource=new JRBeanArrayDataSource(lista.toArray());
            String report="T5_P2_ireportClientes.jasper";
            print=JasperFillManager.fillReport(report, param, datasource);
            JasperExportManager.exportReportToPdfFile(print, "ListaClientes.pdf");
            JasperViewer.viewReport(print);
            
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
