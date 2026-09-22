package modelo;

import java.time.LocalDate;

public class Cita {
	private String idCita;
	private LocalDate fecha;
	private String estado;
	private String motivo;
	
	public Cita (String idCita, LocalDate fecha, String estado, String motivo) {
		this.idCita=idCita;
		this.fecha=fecha;
		this.estado=estado;
		this.motivo=motivo;
	}
	
	public String getIdCita() { return idCita; }
	public LocalDate getFecha() { return fecha; }
	public String getEstado() { return estado; }
	public String getMotivo() { return motivo; }
	
	
	
}