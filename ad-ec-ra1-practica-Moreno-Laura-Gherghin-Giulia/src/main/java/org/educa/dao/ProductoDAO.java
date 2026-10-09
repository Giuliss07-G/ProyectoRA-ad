package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;

public interface ProductoDAO {
    Productos getProductos(String fileXML) throws JAXBException;
}
