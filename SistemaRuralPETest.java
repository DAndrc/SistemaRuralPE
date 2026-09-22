package test;

import modelo.Paciente;
import modelo.Medicamento;
import excepciones.StockInsuficienteException;
import servicios.GestionPacientes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;


import java.time.LocalDate;
import java.util.List;

public class SistemaRuralPETest {
	
	private GestionPacientes gestion;
	
	@BeforeEach
	void setUp() {
		gestion = new GestionPacientes();
		gestion.agregarPaciente(new Paciente("74859612", "Juan", "Pérez", LocalDate.of(1995, 5, 10))); 
		gestion.agregarPaciente(new Paciente("45127896", "María", "Gómez", LocalDate.of(2015, 8, 22)));
	}
	
	@Test
	void testFiltrarPorEdadMinima() {
		List<Paciente> mayoresDeEdad = gestion.filtarPorEdadMinima(18);
		assertEquals(1, mayoresDeEdad.size()); 
		assertEquals("Juan Pérez", mayoresDeEdad.get(0).getNombreCompleto());
	}
	
	@Test 
	void testCalcularEdadPromedio() { 
		double promedio = gestion.calcularEdadPromedio(); 
		assertTrue(promedio > 0); 
	} 
	
	@Test 
	void testRegistrarSalidaStockInsuficiente() { 
		Medicamento med = new Medicamento("MED-01", "Paracetamol 500mg", 5, 10); 
		assertThrows(StockInsuficienteException.class, () -> { 
			med.registrarSalida(10);
			}); 
		} 
	}