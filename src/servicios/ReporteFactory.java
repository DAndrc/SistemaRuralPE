package servicios;

/** Patrón Factory Method: crea el tipo de reporte pedido sin que el cliente conozca las clases concretas. */
public class ReporteFactory {

	public static Reporte crearReporte(String tipo) {
		return crearReporte(tipo, null);
	}

	public static Reporte crearReporte(String tipo, GestionPacientes gestion) {
		if (tipo != null && tipo.equalsIgnoreCase("Atenciones")) {
			return new ReporteAtenciones(gestion);
		} else if (tipo != null && tipo.equalsIgnoreCase("Inventario")) {
			return new ReporteInventario();
		}
		throw new IllegalArgumentException("Tipo de Reporte Desconocido: " + tipo);
	}
}
