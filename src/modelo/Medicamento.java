package modelo;

import excepciones.DatosInvalidosException;
import excepciones.StockInsuficienteException;

public class Medicamento {
	private String idMedicamento;
	private String nombre;
	private int stockActual;
	private int stockMinimo;

	public Medicamento(String idMedicamento, String nombre, int stockActual, int stockMinimo) {
		if (idMedicamento == null || idMedicamento.isBlank() || nombre == null || nombre.isBlank()) {
			throw new DatosInvalidosException("El medicamento requiere identificador y nombre");
		}
		if (stockActual < 0 || stockMinimo < 0) {
			throw new DatosInvalidosException("El stock no puede ser negativo");
		}
		this.idMedicamento = idMedicamento;
		this.nombre = nombre;
		this.stockActual = stockActual;
		this.stockMinimo = stockMinimo;
	}

	public void registrarEntrada(int cantidad) {
		if (cantidad <= 0) {
			throw new DatosInvalidosException("La cantidad de entrada debe ser mayor a cero");
		}
		stockActual = stockActual + cantidad;
	}

	public void registrarSalida(int cantidad) throws StockInsuficienteException {
		if (cantidad <= 0) {
			throw new DatosInvalidosException("La cantidad de salida debe ser mayor a cero");
		}
		if (cantidad > stockActual) {
			throw new StockInsuficienteException("No hay suficiente stock de " + nombre);
		}
		stockActual = stockActual - cantidad;
	}

	public boolean estaEnAlertaStock() {
		return stockActual <= stockMinimo;
	}

	public String getIdMedicamento() { return idMedicamento; }
	public String getNombre() { return nombre; }
	public int getStockActual() { return stockActual; }
	public int getStockMinimo() { return stockMinimo; }
}
