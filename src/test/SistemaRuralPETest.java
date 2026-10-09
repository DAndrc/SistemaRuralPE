package test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import excepciones.AccesoDenegadoException;
import excepciones.DatosInvalidosException;
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

/** Pruebas con datos ficticios. */
public class SistemaRuralPETest {

	private GestionPacientes gestion;
	private PersonalSalud enfermera;
	private PersonalSalud tecnico;

	@BeforeEach
	void setUp() {
		gestion = new GestionPacientes();
		gestion.agregarPaciente(new Paciente("74859612", "Juan", "Pérez", LocalDate.of(1995, 5, 10)));
		gestion.agregarPaciente(new Paciente("45127896", "María", "Gómez", LocalDate.of(2015, 8, 22)));
		enfermera = new PersonalSalud("40123456", "Rosa", "Díaz", LocalDate.of(1988, 3, 14),
				"Enfermera", "rdiaz", "clave-demo-ficticia");
		tecnico = new PersonalSalud("41234567", "Luis", "Rojas", LocalDate.of(1992, 7, 2),
				"Tecnico", "lrojas", "clave-demo-ficticia");
		InventarioMedicamentos.getInstancia().limpiar();
	}

	// ---------- Transformación de colecciones con funciones de orden superior ----------

	@Test
	void testFiltrarPorEdadMinima() {
		List<Paciente> mayoresDeEdad = gestion.filtrarPorEdadMinima(18);
		assertEquals(1, mayoresDeEdad.size());
		assertEquals("Juan Pérez", mayoresDeEdad.get(0).getNombreCompleto());
	}

	@Test
	void testCalcularEdadPromedio() {
		double promedio = gestion.calcularEdadPromedio();
		assertTrue(promedio > 0);
	}

	@Test
	void testObtenerNombresCompletosConMap() {
		assertEquals(List.of("Juan Pérez", "María Gómez"), gestion.obtenerNombresCompletos());
	}

	// ---------- Manejo de excepciones ----------

	@Test
	void testRegistrarSalidaStockInsuficiente() {
		Medicamento med = new Medicamento("MED-01", "Paracetamol 500mg", 5, 10);
		assertThrows(StockInsuficienteException.class, () -> {
			med.registrarSalida(10);
		});
	}

	// ---------- Registro válido de una entidad principal ----------

	@Test
	void testRegistroValidoDePaciente() {
		gestion.agregarPaciente(new Paciente("70123456", "Ana", "Quispe", LocalDate.of(1990, 1, 1)));
		assertEquals(3, gestion.getListaPacientes().size());
		assertTrue(gestion.buscarPorDni("70123456").isPresent());
	}

	// ---------- Entrada inválida o incompleta ----------

	@Test
	void testDniInvalidoLanzaExcepcion() {
		assertThrows(DatosInvalidosException.class,
				() -> new Paciente("123", "Ana", "Quispe", LocalDate.of(1990, 1, 1)));
	}

	@Test
	void testNombreVacioLanzaExcepcion() {
		assertThrows(DatosInvalidosException.class,
				() -> new Paciente("70123456", " ", "Quispe", LocalDate.of(1990, 1, 1)));
	}

	@Test
	void testPacienteDuplicadoLanzaExcepcion() {
		assertThrows(DatosInvalidosException.class,
				() -> gestion.agregarPaciente(new Paciente("74859612", "Otro", "Nombre", LocalDate.of(1990, 1, 1))));
	}

	@Test
	void testSalidaConCantidadNegativaLanzaExcepcion() {
		Medicamento med = new Medicamento("MED-01", "Paracetamol 500mg", 5, 10);
		assertThrows(DatosInvalidosException.class, () -> med.registrarSalida(-1));
	}

	// ---------- Búsquedas con y sin coincidencias ----------

	@Test
	void testBusquedaPorDniConResultado() {
		assertEquals("Juan Pérez", gestion.buscarPorDni("74859612").get().getNombreCompleto());
	}

	@Test
	void testBusquedaPorNombreParcialConResultado() {
		assertEquals(1, gestion.buscarPorNombre("mar").size());
	}

	@Test
	void testBusquedaSinCoincidencias() {
		assertTrue(gestion.buscarPorDni("00000000").isEmpty());
		assertTrue(gestion.buscarPorNombre("zzz").isEmpty());
		assertTrue(gestion.filtrarPorEdadMinima(200).isEmpty());
	}

	// ---------- Protección de datos sensibles ----------

	@Test
	void testDniSeMuestraEnmascarado() {
		Paciente p = gestion.buscarPorDni("74859612").get();
		assertEquals("****9612", p.getDniEnmascarado());
		assertFalse(p.getDniEnmascarado().contains("7485"));
	}

	@Test
	void testPasswordSeGuardaComoHashYNoEnTextoPlano() throws Exception {
		Field campo = PersonalSalud.class.getDeclaredField("passwordHash");
		campo.setAccessible(true);
		String almacenado = (String) campo.get(enfermera);
		assertNotEquals("clave-demo-ficticia", almacenado);
		assertEquals(64, almacenado.length()); // SHA-256 en hexadecimal
		assertTrue(enfermera.autenticar("clave-demo-ficticia"));
		assertFalse(enfermera.autenticar("clave-incorrecta"));
	}

	@Test
	void testReportesYAuditoriaNoExponenDniCompleto() {
		gestion.buscarPorDni("74859612").get().getHistorial()
				.agregarAtencion(new Atencion("A-1", LocalDate.now(), "Dx ficticio", "Tx ficticio"));
		gestion.consultarHistorial(enfermera, "74859612");
		String reporte = ReporteFactory.crearReporte("atenciones", gestion).generar();
		assertFalse(reporte.contains("74859612"));
		assertTrue(gestion.getRegistroAuditoria().stream().noneMatch(linea -> linea.contains("74859612")));
	}

	// ---------- Control de acceso y auditoría ----------

	@Test
	void testAccesoDenegadoAlTecnicoQuedaAuditado() {
		assertThrows(AccesoDenegadoException.class, () -> gestion.consultarHistorial(tecnico, "74859612"));
		assertTrue(gestion.getRegistroAuditoria().get(0).contains("DENEGADO"));
	}

	// ---------- Citas ----------

	@Test
	void testCambioDeEstadoDeCita() {
		GestionCitas citas = new GestionCitas();
		Paciente juan = gestion.buscarPorDni("74859612").get();
		citas.agregarCita(new Cita("C-1", LocalDate.now(), "pendiente", "Control", juan, enfermera));
		citas.cambiarEstado("C-1", "atendida");
		assertEquals(1, citas.filtrarPorEstado("atendida").size());
		assertTrue(citas.filtrarPorEstado("pendiente").isEmpty());
		assertThrows(DatosInvalidosException.class, () -> citas.cambiarEstado("C-1", "inventado"));
	}

	// ---------- Patrones de diseño ----------

	@Test
	void testSingletonDevuelveSiempreLaMismaInstancia() {
		assertSame(InventarioMedicamentos.getInstancia(), InventarioMedicamentos.getInstancia());
	}

	@Test
	void testAlertaDeStockBajoEnInventario() {
		InventarioMedicamentos inv = InventarioMedicamentos.getInstancia();
		inv.agregarMedicamento(new Medicamento("MED-01", "Paracetamol 500mg", 5, 10));
		inv.agregarMedicamento(new Medicamento("MED-02", "Ibuprofeno 400mg", 30, 15));
		assertEquals(1, inv.obtenerBajoStock().size());
	}

	@Test
	void testFactoryCreaReportesYRechazaTiposDesconocidos() {
		Reporte atenciones = ReporteFactory.crearReporte("Atenciones", gestion);
		Reporte inventario = ReporteFactory.crearReporte("Inventario");
		assertTrue(atenciones.generar().contains("2 pacientes registrados"));
		assertTrue(inventario.generar().contains("Inventario"));
		assertThrows(IllegalArgumentException.class, () -> ReporteFactory.crearReporte("otro"));
	}

	// ---------- Flujo de extremo a extremo ----------

	@Test
	void testFlujoCompletoPacienteCitaAtencionInventarioReporte() {
		Paciente juan = gestion.buscarPorDni("74859612").get();
		GestionCitas citas = new GestionCitas();
		citas.agregarCita(new Cita("C-9", LocalDate.now(), "pendiente", "Control", juan, enfermera));
		juan.getHistorial().agregarAtencion(new Atencion("A-9", LocalDate.now(), "Dx ficticio", "Tx ficticio"));
		citas.cambiarEstado("C-9", "atendida");

		InventarioMedicamentos inv = InventarioMedicamentos.getInstancia();
		Medicamento med = new Medicamento("MED-09", "Paracetamol 500mg", 12, 10);
		inv.agregarMedicamento(med);
		med.registrarSalida(3);

		assertEquals(1, gestion.consultarHistorial(enfermera, "74859612").size());
		assertTrue(med.estaEnAlertaStock()); // 12 - 3 = 9 u., por debajo del mínimo (10)
		assertTrue(ReporteFactory.crearReporte("atenciones", gestion).generar().contains("1 atenciones"));
		assertTrue(ReporteFactory.crearReporte("inventario").generar().contains("Paracetamol"));
	}
}
