package modelo;

import java.util.ArrayList;
import java.util.List;

public class HistorialClinico {
	private List<Atencion> atenciones;
	
	public HistorialClinico() {
		this.atenciones = new ArrayList<>();
	}
	
	public void agregarAtencion(Atencion atencion) {
		if (atencion != null) {
			this.atenciones.add(atencion);
		}
	}
	
	public List<Atencion> getAtenciones() { return atenciones; }
	
	

	
}