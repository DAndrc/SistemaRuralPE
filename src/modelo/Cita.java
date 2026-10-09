package modelo;

import java.time.LocalDate;
import java.util.Set;

import excepciones.DatosInvalidosException;

public class Cita {

	public static final String PENDIENTE = "pendiente";
	public static final String ATENDIDA = "atendida";
	public static final String CANCELADA = "cancelada";
	private static final Set<String> ESTADOS_VALIDOS = Set.of(PENDIENTE, ATENDIDA, CANCELADA);

	private String idCita;
	private LocalDate fecha;
	private String estado;
	private String motivo;
	private Paciente paciente;
	private PersonalSalud profesional;

	public Cita(String idCita, LocalDate fecha, String estado, String motivo) {
		this(idCita, fecha, estado, motivo, null, null);
	}

	public Cita(String idCita, LocalDate fecha, String estado, String motivo,
			Paciente paciente, PersonalSalud profesional) {
		if (idCita == null || idCita.isBlank() || fecha == null) {
			throw new DatosInvalidosException("La cita requiere identificador y fecha");
		}
		this.idCita = idCita;
		this.fecha = fecha;
		this.estado = normalizarEstado(estado);
		this.motivo = motivo;
		this.paciente = paciente;
		this.profesional = profesional;
	}

	private static String normalizarEstado(String estado) {
		String e = (estado == null) ? "" : estado.trim().toLowerCase();
		if (!ESTADOS_VALIDOS.contains(e)) {
			throw new DatosInvalidosException("Estado de cita no válido: " + estado
					+ " (use pendiente, atendida o cancelada)");
		}
		return e;
	}

	public void cambiarEstado(String nuevoEstado) {
		this.estado = normalizarEstado(nuevoEstado);
	}

	public String getIdCita() { return idCita; }
	public LocalDate getFecha() { return fecha; }
	public String getEstado() { return estado; }
	public String getMotivo() { return motivo; }
	public Paciente getPaciente() { return paciente; }
	public PersonalSalud getProfesional() { return profesional; }
}
