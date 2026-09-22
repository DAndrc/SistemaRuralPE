package servicios;

public class ReporteFactory {
	public static Reporte crearReporte(String tipo) {
		if (tipo.equalsIgnoreCase("Atenciones")) {
			return new ReporteAtenciones();
		} 
		else if (tipo.equalsIgnoreCase("Inventario")) {
			return new ReporteInventario();
		}
		throw new IllegalArgumentException("Tipo de Reporte Desconocido: " + tipo);
	}
}