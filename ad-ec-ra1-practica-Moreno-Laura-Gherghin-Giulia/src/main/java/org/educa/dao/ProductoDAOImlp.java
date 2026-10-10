package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import java.io.File;

public class ProductoDAOImlp implements ProductoDAO {
    public Productos getProductos(String fileXML) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Productos.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (Productos) unmarshaller.unmarshal(new File(fileXML));
    }
}
