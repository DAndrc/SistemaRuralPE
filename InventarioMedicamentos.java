package modelo;

import java.util.ArrayList;
import java.util.List;

public class InventarioMedicamentos {
	private static InventarioMedicamentos instanciaUnica;
	private List<Medicamento> medicamentos;
	
	private InventarioMedicamentos() {
		this.medicamentos = new ArrayList<>();
	}
	
	public static InventarioMedicamentos getInstancia() {
		if (instanciaUnica == null) {
			instanciaUnica = new InventarioMedicamentos();
		}
		return instanciaUnica;
	}
	
	public void agregarMedicamento(Medicamento med) {
		if (med != null) {
			medicamentos.add(med);
		}
	}
	
	public List<Medicamento> obtenerBajoStock(){
		List<Medicamento> alertaList = new ArrayList<>();
		for (int i = 0; i < medicamentos.size(); i++) {
			Medicamento m = medicamentos.get(i);
			if (m.estaEnAlertaStock()) {
				alertaList.add(m);
			}
		}
		return alertaList;
	}
	
	public List<Medicamento> getMedicamentos() { return medicamentos; }
	

	
	
	
	
}