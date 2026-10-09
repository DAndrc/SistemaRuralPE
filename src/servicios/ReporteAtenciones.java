package servicios;

public class ReporteAtenciones extends Reporte {
	private final GestionPacientes gestion;

	public ReporteAtenciones() {
		this(null);
	}

	public ReporteAtenciones(GestionPacientes gestion) {
		this.gestion = gestion;
	}

	@Override
	public String generar() {
		if (gestion == null) {
			return "Reporte de Atenciones (" + getFechaGeneracion() + "): sin datos asociados.";
		}
		int totalPacientes = gestion.getListaPacientes().size();
		int totalAtenciones = gestion.getListaPacientes().stream()
				.mapToInt(p -> p.getHistorial().getAtenciones().size())
				.sum();
		// Solo se informan totales: el reporte no incluye datos personales de los pacientes.
		return "Reporte de Atenciones (" + getFechaGeneracion() + "): "
				+ totalPacientes + " pacientes registrados, "
				+ totalAtenciones + " atenciones documentadas.";
	}
}
