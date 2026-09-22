package modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona{
	
	private List<String>antecedentes;
	private HistorialClinico historialClinico;
	
	public Paciente(String dni, String nombres, String apellidos, LocalDate fechaNacimiento) {
		super(dni, nombres, apellidos, fechaNacimiento);
		this.antecedentes = new ArrayList();
		this.historialClinico = new HistorialClinico();
	}
	
	public void registrarAntecedente(String texto) {
		if (texto !=null && !texto.isEmpty()) {
			antecedentes.add(texto);
		}
	}
	
	public List<String> getAntecedentes() { return antecedentes; }
	public HistorialClinico getHistorial() { return historialClinico; }
	
	
	
	
}