package servicios;

import java.util.stream.Collectors;

import modelo.InventarioMedicamentos;

public class ReporteInventario extends Reporte {

	@Override
	public String generar() {
		String detalle = InventarioMedicamentos.getInstancia().getMedicamentos().stream()
				.map(m -> m.getNombre() + ": " + m.getStockActual() + " u. (mínimo " + m.getStockMinimo() + ")"
						+ (m.estaEnAlertaStock() ? " [STOCK BAJO]" : ""))
				.collect(Collectors.joining("; "));
		return "Reporte de Inventario (" + getFechaGeneracion() + "): "
				+ (detalle.isEmpty() ? "sin medicamentos registrados." : detalle);
	}
}
