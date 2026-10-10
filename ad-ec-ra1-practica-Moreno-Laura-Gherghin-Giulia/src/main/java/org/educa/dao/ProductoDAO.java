package org.educa.dao;

import generated.Productos;
import jakarta.xml.bind.JAXBException;

     /**
     * Lee el XML y lo convierte en objetos Java.
     *
     * @param fileXml ruta del fichero XML
     * @return objeto raíz con la lista de productos
     * @throws JAXBException si el XML no se puede leer o no es válido
     */
public interface ProductoDAO {
    Productos getProductos(String fileXML) throws JAXBException;
}
