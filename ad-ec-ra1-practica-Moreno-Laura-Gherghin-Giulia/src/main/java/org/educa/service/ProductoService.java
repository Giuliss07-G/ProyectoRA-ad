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

    /**
     * Lee el XML del inventario y calcula, para cada producto, su precio final
     * (precio con el descuento aplicado), su coste (envío + almacenaje) y su beneficio
     * (precio final - coste).
     *
     * @param fileXml ruta del fichero XML con el inventario
     * @return lista de productos con los cálculos ya realizados
     * @throws JAXBException si el XML no se puede leer o no cumple el esquema
     */
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

    /**
     * Genera un fichero de texto con el resumen del inventario: fecha, número de
     * productos, beneficio total y datos del fichero XML (ruta, nombre y tamaño).
     * Se guarda como {@code result_<mesAño>.txt}, por ejemplo {@code result_junio2026.txt}.
     *
     * @param path    carpeta donde se guardará el resumen
     * @param fileXml ruta del fichero XML del inventario
     * @throws JAXBException si el XML no se puede leer
     * @throws IOException   si no se puede escribir el fichero de salida
     */
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
        List<ProductoEntity> productos = readFile(fileXml);
        BigDecimal cien = new BigDecimal(100);

        File fich = new File(fileXml);
        String nombreSinExtension = fich.getName().replace(".xml", "");
        String fecha = nombreSinExtension.substring(nombreSinExtension.indexOf('_') + 1);

        Workbook libro = new XSSFWorkbook();
        Sheet hoja = libro.createSheet("Productos");

        Font negrita = libro.createFont();
        negrita.setBold(true);


        CellStyle estiloCabecera = libro.createCellStyle();
        estiloCabecera.setFont(negrita);
        estiloCabecera.setAlignment(HorizontalAlignment.CENTER);
        estiloCabecera.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle codigoVerde = libro.createCellStyle();
        codigoVerde.setFont(negrita);
        codigoVerde.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        codigoVerde.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle textoVerde = libro.createCellStyle();
        textoVerde.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        textoVerde.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle eurosVerde = libro.createCellStyle();
        eurosVerde.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        eurosVerde.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        eurosVerde.setAlignment(HorizontalAlignment.RIGHT);
        eurosVerde.setDataFormat(libro.createDataFormat().getFormat("#,##0.00 \"€\""));

        CellStyle porcentajeVerde = libro.createCellStyle();
        porcentajeVerde.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        porcentajeVerde.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        porcentajeVerde.setAlignment(HorizontalAlignment.RIGHT);
        porcentajeVerde.setDataFormat(libro.createDataFormat().getFormat("0.00%"));


        CellStyle codigoAmarillo = libro.createCellStyle();
        codigoAmarillo.setFont(negrita);
        codigoAmarillo.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        codigoAmarillo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle textoAmarillo = libro.createCellStyle();
        textoAmarillo.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        textoAmarillo.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        CellStyle eurosAmarillo = libro.createCellStyle();
        eurosAmarillo.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        eurosAmarillo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        eurosAmarillo.setAlignment(HorizontalAlignment.RIGHT);
        eurosAmarillo.setDataFormat(libro.createDataFormat().getFormat("#,##0.00 \"€\""));

        CellStyle porcentajeAmarillo = libro.createCellStyle();
        porcentajeAmarillo.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        porcentajeAmarillo.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        porcentajeAmarillo.setAlignment(HorizontalAlignment.RIGHT);
        porcentajeAmarillo.setDataFormat(libro.createDataFormat().getFormat("0.00%"));


    }
}
