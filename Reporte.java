package servicios;

import java.time.LocalDate;

public abstract class Reporte {
	private LocalDate fechaGeneracion;
	
	public Reporte() {
		this.fechaGeneracion = LocalDate.now();
	}
	
	public abstract String generar();
	
	public LocalDate getFechaGeneracion() { return fechaGeneracion; }
	
	
	
	
	
}