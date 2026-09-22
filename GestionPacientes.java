package servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import modelo.Paciente;

public class GestionPacientes {
	private List<Paciente> listaPacientes;	
	
	public GestionPacientes() {
		listaPacientes = new ArrayList<>();
	}
	
	public void agregarPaciente(Paciente p) {
		if (p!= null) {
		listaPacientes.add(p);
		}
	}
	
	public List<Paciente> filtarPorEdadMinima(int edadMinima) {
		return listaPacientes.stream()
				.filter(p -> p.calcularEdad() >= edadMinima)
				.collect(Collectors.toList());	
	}
	
	public double calcularEdadPromedio() { 
		return listaPacientes.stream() 
				.mapToInt(Paciente::calcularEdad) 
				.average() 
				.orElse(0.0);
		}
	
	public List<Paciente> getListaPacientes() {
		return listaPacientes;
	}
	
}