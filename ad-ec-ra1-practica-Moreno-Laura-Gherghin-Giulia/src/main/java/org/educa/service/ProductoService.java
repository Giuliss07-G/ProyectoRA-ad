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

        return null;
    }

    public void exportSummary(String path, String fileXml) throws JAXBException, IOException {
        //TODO: Implementar

        List<ProductoEntity> productos = readFile(fileXml);
        File fich = new File(fileXml);

        String nombreFich = fich.getName();
        String nombreSinExtension = nombreFich.replace(".xml", "");
        String fecha = nombreSinExtension.substring(nombreSinExtension.indexOf('_') + 1);

    }

    public void exportExcel(String path, String fileXml) throws JAXBException, IOException, ParseException {
        //TODO: Implementar
    }
}
