package modelo;

import java.time.LocalDate;

public class Atencion {
	
	private String idAtencion;
	private LocalDate fecha;
	private String diagnostico;
	private String tratamiento;
	
	public Atencion(String idAtencion, LocalDate fecha, String diagnostico, String tratamiento) {
		this.idAtencion=idAtencion;
		this.fecha=fecha;
		this.diagnostico=diagnostico;
		this.tratamiento=tratamiento;
	}
	
	public String getIdAtencion() { return idAtencion; }
	public LocalDate getFecha() { return fecha; }
	public String getDiagnostico() { return diagnostico; }
	public String getTratamiento() { return tratamiento; }

	
}