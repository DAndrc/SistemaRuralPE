package servicios;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import excepciones.DatosInvalidosException;
import modelo.Cita;
import modelo.Paciente;

public class GestionCitas {
	private List<Cita> listaCitas;

	public GestionCitas() {
		listaCitas = new ArrayList<>();
	}

	public void agregarCita(Cita c) {
		if (c == null) {
			throw new DatosInvalidosException("No se puede agendar una cita nula");
		}
		if (listaCitas.stream().anyMatch(x -> x.getIdCita().equals(c.getIdCita()))) {
			throw new DatosInvalidosException("Ya existe una cita con el identificador " + c.getIdCita());
		}
		listaCitas.add(c);
	}

	public List<Cita> filtrarPorEstado(String estado) {
		return listaCitas.stream()
				.filter(c -> c.getEstado().equalsIgnoreCase(estado))
				.collect(Collectors.toList());
	}

	public List<Cita> filtrarPorPaciente(Paciente paciente) {
		return listaCitas.stream()
				.filter(c -> c.getPaciente() != null && c.getPaciente().getDni().equals(paciente.getDni()))
				.collect(Collectors.toList());
	}

	public void cambiarEstado(String idCita, String nuevoEstado) {
		Cita cita = listaCitas.stream()
				.filter(c -> c.getIdCita().equals(idCita))
				.findFirst()
				.orElseThrow(() -> new DatosInvalidosException("No existe la cita " + idCita));
		cita.cambiarEstado(nuevoEstado);
	}

	public List<Cita> getListaCitas() {
		return listaCitas;
	}
}
