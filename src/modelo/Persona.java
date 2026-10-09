package modelo;

import java.time.LocalDate;
import java.time.Period;

import excepciones.DatosInvalidosException;

public class Persona {

	private String dni;
	private String nombres;
	private String apellidos;
	private LocalDate fechaNacimiento;
	private String direccion;

	public Persona(String dni, String nombres, String apellidos, LocalDate fechaNacimiento) {
		// Validación de entradas: no se aceptan datos vacíos ni con formato incorrecto.
		if (dni == null || !dni.matches("\\d{8}")) {
			throw new DatosInvalidosException("El DNI debe tener exactamente 8 dígitos numéricos");
		}
		if (nombres == null || nombres.isBlank() || apellidos == null || apellidos.isBlank()) {
			throw new DatosInvalidosException("Los nombres y apellidos son obligatorios");
		}
		if (fechaNacimiento == null || fechaNacimiento.isAfter(LocalDate.now())) {
			throw new DatosInvalidosException("La fecha de nacimiento es obligatoria y no puede ser futura");
		}
		this.dni = dni;
		this.nombres = nombres.trim();
		this.apellidos = apellidos.trim();
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDni() { return dni; }
	public String getNombres() { return nombres; }
	public String getApellidos() { return apellidos; }
	public LocalDate getFechaNacimiento() { return fechaNacimiento; }
	public String getDireccion() { return direccion; }
	public void setDireccion(String direccion) { this.direccion = direccion; }

	/** Devuelve el DNI con los primeros dígitos ocultos, para mostrarlo sin exponer el dato completo (Ley N.° 29733). */
	public String getDniEnmascarado() {
		return "*".repeat(dni.length() - 4) + dni.substring(dni.length() - 4);
	}

	public int calcularEdad() {
		return Period.between(fechaNacimiento, LocalDate.now()).getYears();
	}

	public String getNombreCompleto() {
		return nombres + " " + apellidos;
	}
}
