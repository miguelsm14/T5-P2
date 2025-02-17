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
            InputStream imagen1;
            InputStream imagen2;
            //imagen1=GeneradorPdf.class.getResourceAsStream("/logoGrupo1.png");
            //imagen2=GeneradorPdf.class.getResourceAsStream("/logoGrupoPapelria.png");
            HashMap param=new HashMap();
//            param.put("titulo", "Informe de papelería");
//            param.put("fecha", date);
            //param.put("imagen1", imagen1);
            //param.put("imagen2", imagen2);
            
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
