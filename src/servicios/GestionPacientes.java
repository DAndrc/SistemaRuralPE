package servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import excepciones.AccesoDenegadoException;
import excepciones.DatosInvalidosException;
import modelo.Atencion;
import modelo.Paciente;
import modelo.PersonalSalud;

public class GestionPacientes {
	private List<Paciente> listaPacientes;
	private List<String> registroAuditoria;

	public GestionPacientes() {
		listaPacientes = new ArrayList<>();
		registroAuditoria = new ArrayList<>();
	}

	public void agregarPaciente(Paciente p) {
		if (p == null) {
			throw new DatosInvalidosException("No se puede registrar un paciente nulo");
		}
		boolean duplicado = listaPacientes.stream().anyMatch(x -> x.getDni().equals(p.getDni()));
		if (duplicado) {
			throw new DatosInvalidosException("Ya existe un paciente registrado con ese DNI");
		}
		listaPacientes.add(p);
	}

	public Optional<Paciente> buscarPorDni(String dni) {
		return listaPacientes.stream()
				.filter(p -> p.getDni().equals(dni))
				.findFirst();
	}

	public List<Paciente> buscarPorNombre(String fragmento) {
		if (fragmento == null || fragmento.isBlank()) {
			throw new DatosInvalidosException("Ingrese al menos un carácter para buscar por nombre");
		}
		String criterio = fragmento.trim().toLowerCase();
		return listaPacientes.stream()
				.filter(p -> p.getNombreCompleto().toLowerCase().contains(criterio))
				.collect(Collectors.toList());
	}

	public List<Paciente> filtrarPorEdadMinima(int edadMinima) {
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

	public List<String> obtenerNombresCompletos() {
		return listaPacientes.stream()
				.map(Paciente::getNombreCompleto)
				.collect(Collectors.toList());
	}

	/**
	 * Control de acceso por rol: solo el personal autorizado puede ver el historial.
	 * Toda consulta (permitida o denegada) queda registrada en la auditoría, con el DNI enmascarado.
	 */
	public List<Atencion> consultarHistorial(PersonalSalud usuario, String dni) {
		String quien = (usuario == null) ? "desconocido" : usuario.getUsuario();
		if (usuario == null || !usuario.puedeConsultarHistorial()) {
			registrarAuditoria(quien, "consultar historial", "DENEGADO");
			throw new AccesoDenegadoException("El rol del usuario no permite consultar historiales clínicos");
		}
		Paciente paciente = buscarPorDni(dni)
				.orElseThrow(() -> new DatosInvalidosException("No existe un paciente con el DNI indicado"));
		registrarAuditoria(quien, "consultar historial de DNI " + paciente.getDniEnmascarado(), "PERMITIDO");
		return Collections.unmodifiableList(paciente.getHistorial().getAtenciones());
	}

	private void registrarAuditoria(String usuario, String accion, String resultado) {
		registroAuditoria.add(LocalDateTime.now() + " | " + usuario + " | " + accion + " | " + resultado);
	}

	public List<String> getRegistroAuditoria() {
		return Collections.unmodifiableList(registroAuditoria);
	}

	public List<Paciente> getListaPacientes() {
		return listaPacientes;
	}
}
