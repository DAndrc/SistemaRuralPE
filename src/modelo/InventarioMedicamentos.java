package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import excepciones.DatosInvalidosException;

/** Patrón Singleton: el puesto de salud tiene una única fuente de verdad del stock. */
public class InventarioMedicamentos {
	private static InventarioMedicamentos instanciaUnica;
	private List<Medicamento> medicamentos;

	private InventarioMedicamentos() {
		this.medicamentos = new ArrayList<>();
	}

	public static synchronized InventarioMedicamentos getInstancia() {
		if (instanciaUnica == null) {
			instanciaUnica = new InventarioMedicamentos();
		}
		return instanciaUnica;
	}

	public void agregarMedicamento(Medicamento med) {
		if (med == null) {
			throw new DatosInvalidosException("No se puede agregar un medicamento nulo");
		}
		medicamentos.add(med);
	}

	// Función de orden superior (filter): medicamentos cuyo stock está en el mínimo crítico o por debajo.
	public List<Medicamento> obtenerBajoStock() {
		return medicamentos.stream()
				.filter(Medicamento::estaEnAlertaStock)
				.collect(Collectors.toList());
	}

	public void limpiar() {
		medicamentos.clear();
	}

	public List<Medicamento> getMedicamentos() { return medicamentos; }
}
