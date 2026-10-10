package org.educa.service;

import generated.Producto;
import generated.Productos;
import jakarta.xml.bind.JAXBException;
import org.educa.dao.ProductoDAO;
import org.educa.dao.ProductoDAOImlp;
import org.educa.entity.ProductoEntity;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class ProductoService {

    ProductoDAO productoDAO=new ProductoDAOImlp();

    public List<ProductoEntity> readFile(String fileXml) throws JAXBException {
        //TODO: Implementar

        Productos productos = productoDAO.getProductos(fileXml);
        List<Producto> listaProductos = productos.getProducto();
        List<ProductoEntity> resultado = new ArrayList<>();
        BigDecimal cien=new BigDecimal(100);

        for(Producto p: listaProductos){

            BigDecimal porcentaje= cien.subtract(p.getDescuento());
            BigDecimal PrecioFinal=p.getPrecio().multiply(porcentaje);
            BigDecimal coste=p.getCostes().getCostesEnvio().add(p.getCostes().getCostesAlmacenaje());
            BigDecimal beneficio=PrecioFinal.subtract(coste);


            ProductoEntity entity=new ProductoEntity();

            entity.setProducto(p);
            entity.setPrecioFinal(PrecioFinal);
            entity.setCost(coste);
            entity.setProfit(beneficio);

            resultado.add(entity);

        }

        return resultado;
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        //TODO: Implementar

        List<ProductoEntity> productos = readFile(fileXml);
        File fich = new File(fileXml);

        String nombreFich = fich.getName();
        String nombreSinExtension = nombreFich.replace(".xml", "");
        String fecha = nombreSinExtension.substring(nombreSinExtension.indexOf('_') + 1);

        BigDecimal beneficioTotal= new BigDecimal(0);
        for(ProductoEntity producto: productos){
            beneficioTotal=beneficioTotal.add(producto.getProfit());
        }

        SummaryEntity summary=new SummaryEntity(fecha, productos.size(), beneficioTotal,
                fich.getAbsolutePath(), nombreSinExtension, fich.length());

        File archivo=new File(path, "result_"+ fecha + ".txt");

        try (FileWriter writer = new  FileWriter(archivo)) {
            writer.write(summary.toPrint());
        }

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
