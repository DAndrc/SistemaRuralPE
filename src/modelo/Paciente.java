package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import excepciones.DatosInvalidosException;

public class Paciente extends Persona {

	private List<String> antecedentes;
	private HistorialClinico historialClinico;

	public Paciente(String dni, String nombres, String apellidos, LocalDate fechaNacimiento) {
		super(dni, nombres, apellidos, fechaNacimiento);
		this.antecedentes = new ArrayList<>();
		this.historialClinico = new HistorialClinico();
	}

	public void registrarAntecedente(String texto) {
		if (texto == null || texto.isBlank()) {
			throw new DatosInvalidosException("El antecedente no puede estar vacío");
		}
		antecedentes.add(texto.trim());
	}

	public List<String> getAntecedentes() { return antecedentes; }
	public HistorialClinico getHistorial() { return historialClinico; }
}
