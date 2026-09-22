package modelo;

import java.time.LocalDate;
import java.time.Period;

public class Persona {
	
	private String dni;
	private String nombres;
	private String apellidos;
	private LocalDate fechaNacimiento;
	
	public Persona(String dni, String nombres, String apellidos, LocalDate fechaNacimiento) {
		this.dni=dni;
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.fechaNacimiento=fechaNacimiento;
	}
	
	public String getDni() { return dni; }
	public String getNombres() { return nombres; }
	public String getApellidos() { return apellidos; }
	public LocalDate getfechaNacimiento() { return fechaNacimiento; }
	
	public int calcularEdad() {
		if (fechaNacimiento == null) return 0;
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}
	
	public String getNombreCompleto() {
		return nombres + " " + apellidos;
	}
	
	
}