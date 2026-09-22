package modelo;

import excepciones.StockInsuficienteException;

public class Medicamento {
	private String idMedicamento;
	private String nombre;
	private int stockActual;
	private int stockMinimo;
	
	public Medicamento(String idMedicamento, String nombre, int stockActual, int stockMinimo) {
		this.idMedicamento=idMedicamento;
		this.nombre=nombre;
		this.stockActual=stockActual;
		this.stockMinimo=stockMinimo;
	}
	
	public void registrarEntrada(int cantidad) {
		if (cantidad > 0) {
			stockActual = stockActual + cantidad;
		}
	}
	
	public void registrarSalida(int cantidad) throws StockInsuficienteException {
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