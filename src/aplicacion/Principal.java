package aplicacion;

import java.time.LocalDate;

import excepciones.AccesoDenegadoException;
import excepciones.SistemaRuralException;
import excepciones.StockInsuficienteException;
import modelo.Atencion;
import modelo.Cita;
import modelo.InventarioMedicamentos;
import modelo.Medicamento;
import modelo.Paciente;
import modelo.PersonalSalud;
import servicios.GestionCitas;
import servicios.GestionPacientes;
import servicios.Reporte;
import servicios.ReporteFactory;

/**
 * Flujo de extremo a extremo con datos ficticios:
 * personal de salud -> pacientes -> cita -> atención -> medicamento -> reportes -> auditoría.
 */
public class Principal {
	public static void main(String[] args) {
		System.out.println("--- INICIANDO SISTEMA RURAL-PE (datos ficticios) ---");
		try {
			GestionPacientes gestion = new GestionPacientes();
			GestionCitas citas = new GestionCitas();
			InventarioMedicamentos inventario = InventarioMedicamentos.getInstancia();

			// 1. Personal de salud y autenticación (la contraseña se guarda solo como hash)
			PersonalSalud enfermera = new PersonalSalud("40123456", "Rosa", "Díaz",
					LocalDate.of(1988, 3, 14), "Enfermera", "rdiaz", "clave-demo-ficticia");
			PersonalSalud tecnico = new PersonalSalud("41234567", "Luis", "Rojas",
					LocalDate.of(1992, 7, 2), "Tecnico", "lrojas", "clave-demo-ficticia");
			System.out.println("\n--- 1. AUTENTICACIÓN ---");
			System.out.println("Ingreso correcto de " + enfermera.getUsuario() + ": "
					+ enfermera.autenticar("clave-demo-ficticia"));
			System.out.println("Ingreso con clave errada: " + enfermera.autenticar("otra-clave"));

			// 2. Registro y búsqueda de pacientes
			System.out.println("\n--- 2. REGISTRO Y BÚSQUEDA DE PACIENTES ---");
			Paciente juan = new Paciente("74859612", "Juan", "Pérez", LocalDate.of(1995, 5, 10));
			Paciente maria = new Paciente("45127896", "María", "Gómez", LocalDate.of(2015, 8, 22));
			juan.setDireccion("Caserío Marcabal Grande");
			juan.registrarAntecedente("Alergia a la penicilina");
			gestion.agregarPaciente(juan);
			gestion.agregarPaciente(maria);
			System.out.println("Pacientes: " + gestion.obtenerNombresCompletos());
			System.out.println("Búsqueda por DNI " + juan.getDniEnmascarado() + ": "
					+ gestion.buscarPorDni("74859612").map(Paciente::getNombreCompleto).orElse("sin resultados"));
			System.out.println("Búsqueda por DNI inexistente: "
					+ gestion.buscarPorDni("00000000").map(Paciente::getNombreCompleto).orElse("sin resultados"));
			System.out.println("Mayores de 18 años: " + gestion.filtrarPorEdadMinima(18).size());
			System.out.println("Edad promedio: " + gestion.calcularEdadPromedio());

			// 3. Agendamiento de citas
			System.out.println("\n--- 3. CITAS ---");
			citas.agregarCita(new Cita("C-001", LocalDate.now().plusDays(1), "pendiente",
					"Control general", juan, enfermera));
			System.out.println("Citas pendientes: " + citas.filtrarPorEstado("pendiente").size());
			citas.cambiarEstado("C-001", "atendida");
			System.out.println("Citas atendidas: " + citas.filtrarPorEstado("atendida").size());

			// 4. Registro de atención y control de acceso al historial
			System.out.println("\n--- 4. ATENCIÓN Y CONTROL DE ACCESO ---");
			juan.getHistorial().agregarAtencion(new Atencion("A-001", LocalDate.now(),
					"Faringitis aguda", "Paracetamol 500mg cada 8 horas"));
			System.out.println("Atenciones consultadas por la enfermera: "
					+ gestion.consultarHistorial(enfermera, "74859612").size());
			try {
				gestion.consultarHistorial(tecnico, "74859612");
			} catch (AccesoDenegadoException e) {
				System.out.println("Acceso denegado al técnico: " + e.getMessage());
			}

			// 5. Inventario: descuento por tratamiento y alertas de stock
			System.out.println("\n--- 5. INVENTARIO ---");
			Medicamento paracetamol = new Medicamento("MED-01", "Paracetamol 500mg", 12, 10);
			Medicamento ibuprofeno = new Medicamento("MED-02", "Ibuprofeno 400mg", 30, 15);
			inventario.agregarMedicamento(paracetamol);
			inventario.agregarMedicamento(ibuprofeno);
			paracetamol.registrarSalida(3);
			try {
				paracetamol.registrarSalida(50);
			} catch (StockInsuficienteException e) {
				System.out.println("Error controlado: " + e.getMessage());
			}
			inventario.obtenerBajoStock().forEach(m ->
					System.out.println("¡Alerta! Stock bajo de " + m.getNombre() + " (" + m.getStockActual() + " u.)"));

			// 6. Reportes (Factory Method)
			System.out.println("\n--- 6. REPORTES ---");
			Reporte rAtenciones = ReporteFactory.crearReporte("atenciones", gestion);
			Reporte rInventario = ReporteFactory.crearReporte("inventario");
			System.out.println(rAtenciones.generar());
			System.out.println(rInventario.generar());

			// 7. Auditoría
			System.out.println("\n--- 7. AUDITORÍA ---");
			gestion.getRegistroAuditoria().forEach(System.out::println);

			System.out.println("\n=== EJECUCIÓN FINALIZADA EXITOSAMENTE ===");
		} catch (SistemaRuralException e) {
			System.out.println("Error del sistema: " + e.getMessage());
		}
	}
}
