package com.tiendadeportivas.backend.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import com.tiendadeportivas.backend.model.Factura;
import com.tiendadeportivas.backend.model.Pedido;
import com.tiendadeportivas.backend.model.PedidoItem;

@Service
public class FacturaPdfService {

    // =====================================================
    // GENERAR PDF DE FACTURA
    // =====================================================

    public byte[] generarPdf(Factura factura) {

        if (factura == null) {
            throw new IllegalArgumentException(
                    "La factura no puede ser nula.");
        }

        Pedido pedido = factura.getPedido();

        if (pedido == null) {
            throw new IllegalStateException(
                    "La factura no tiene un pedido asociado.");
        }

        ByteArrayOutputStream salida = new ByteArrayOutputStream();

        Document documento = new Document();

        try {

            PdfWriter.getInstance(
                    documento,
                    salida);

            documento.open();

            // =================================================
            // TÍTULO
            // =================================================

            Font fuenteTitulo = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    20);

            Paragraph titulo = new Paragraph(
                    "UrbanSneakers",
                    fuenteTitulo);

            titulo.setAlignment(
                    Element.ALIGN_CENTER);

            documento.add(titulo);

            Font fuenteSubtitulo = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    16);

            Paragraph subtitulo = new Paragraph(
                    "FACTURA",
                    fuenteSubtitulo);

            subtitulo.setAlignment(
                    Element.ALIGN_CENTER);

            subtitulo.setSpacingAfter(20);

            documento.add(subtitulo);

            // =================================================
            // DATOS DE FACTURA
            // =================================================

            DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm");

            documento.add(new Paragraph(
                    "Número de factura: "
                            + factura.getNumeroFactura()));

            documento.add(new Paragraph(
                    "Fecha de emisión: "
                            + factura.getFechaEmision()
                                    .format(formatoFecha)));

            documento.add(new Paragraph(
                    "Pedido: "
                            + pedido.getIdPedido()));

            documento.add(new Paragraph(
                    "Método de pago: "
                            + factura.getMetodoPago()));

            documento.add(new Paragraph(" "));

            // =================================================
            // DATOS DEL CLIENTE
            // =================================================

            Font fuenteSeccion = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    12);

            documento.add(new Paragraph(
                    "Datos del cliente",
                    fuenteSeccion));

            documento.add(new Paragraph(
                    pedido.getNombre()
                            + " "
                            + pedido.getApellidos()));

            documento.add(new Paragraph(
                    "Email: "
                            + pedido.getEmail()));

            documento.add(new Paragraph(
                    "Teléfono: "
                            + pedido.getTelefono()));

            documento.add(new Paragraph(
                    "Dirección: "
                            + pedido.getDireccion()));

            documento.add(new Paragraph(
                    pedido.getCp()
                            + " "
                            + pedido.getCiudad()
                            + ", "
                            + pedido.getProvincia()));

            documento.add(new Paragraph(
                    pedido.getPais()));

            documento.add(new Paragraph(" "));

            // =================================================
            // PRODUCTOS
            // =================================================

            documento.add(new Paragraph(
                    "Productos",
                    fuenteSeccion));

            documento.add(new Paragraph(" "));

            PdfPTable tabla = new PdfPTable(6);

            tabla.setWidthPercentage(100);

            tabla.setWidths(
                    new float[] {
                            3.5f,
                            1.2f,
                            1.5f,
                            1.2f,
                            1.7f,
                            1.7f
                    });

            agregarCabecera(
                    tabla,
                    "Producto");

            agregarCabecera(
                    tabla,
                    "Talla");

            agregarCabecera(
                    tabla,
                    "Color");

            agregarCabecera(
                    tabla,
                    "Cantidad");

            agregarCabecera(
                    tabla,
                    "Precio");

            agregarCabecera(
                    tabla,
                    "Subtotal");

            for (PedidoItem item : pedido.getItems()) {

                tabla.addCell(
                        item.getNombreProducto());

                tabla.addCell(
                        item.getTalla());

                tabla.addCell(
                        item.getColor());

                tabla.addCell(
                        String.valueOf(
                                item.getCantidad()));

                tabla.addCell(
                        formatearPrecio(
                                item.getPrecioUnitario()));

                tabla.addCell(
                        formatearPrecio(
                                item.getSubtotalLinea()));
            }

            documento.add(tabla);

            documento.add(new Paragraph(" "));

            // =================================================
            // TOTALES
            // =================================================

            PdfPTable tablaTotales = new PdfPTable(2);

            tablaTotales.setWidthPercentage(45);

            tablaTotales.setHorizontalAlignment(
                    Element.ALIGN_RIGHT);

            agregarTotal(
                    tablaTotales,
                    "Subtotal",
                    pedido.getSubtotal());

            agregarTotal(
                    tablaTotales,
                    "IVA",
                    pedido.getIva());

            agregarTotal(
                    tablaTotales,
                    "Gastos de envío",
                    pedido.getEnvio());

            agregarTotal(
                    tablaTotales,
                    "TOTAL",
                    pedido.getTotal());

            documento.add(tablaTotales);

            documento.add(new Paragraph(" "));

            // =================================================
            // INFORMACIÓN FINAL
            // =================================================

            Paragraph pago = new Paragraph(
                    "Pago realizado mediante "
                            + factura.getMetodoPago()
                            + ".");

            pago.setSpacingBefore(15);

            documento.add(pago);

            Paragraph gracias = new Paragraph(
                    "Gracias por comprar en UrbanSneakers.");

            gracias.setAlignment(
                    Element.ALIGN_CENTER);

            gracias.setSpacingBefore(25);

            documento.add(gracias);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "No se pudo generar el PDF de la factura.",
                    e);

        } finally {

            if (documento.isOpen()) {
                documento.close();
            }
        }

        return salida.toByteArray();
    }

    // =====================================================
    // CABECERA DE TABLA
    // =====================================================

    private void agregarCabecera(
            PdfPTable tabla,
            String texto) {

        Font fuente = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                10);

        PdfPCell celda = new PdfPCell(
                new Paragraph(
                        texto,
                        fuente));

        celda.setHorizontalAlignment(
                Element.ALIGN_CENTER);

        celda.setPadding(6);

        tabla.addCell(celda);
    }

    // =====================================================
    // FILA DE TOTAL
    // =====================================================

    private void agregarTotal(
            PdfPTable tabla,
            String concepto,
            BigDecimal cantidad) {

        tabla.addCell(concepto);

        PdfPCell celdaCantidad = new PdfPCell(
                new Paragraph(
                        formatearPrecio(
                                cantidad)));

        celdaCantidad.setHorizontalAlignment(
                Element.ALIGN_RIGHT);

        tabla.addCell(celdaCantidad);
    }

    // =====================================================
    // FORMATEAR PRECIO
    // =====================================================

    private String formatearPrecio(
            BigDecimal precio) {

        if (precio == null) {
            return "0,00 €";
        }

        return String.format(
                "%.2f €",
                precio);
    }
}
