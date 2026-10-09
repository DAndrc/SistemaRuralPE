package excepciones;

/** Se lanza cuando una entrada no cumple las reglas de validación del sistema. */
public class DatosInvalidosException extends SistemaRuralException {
	private static final long serialVersionUID = 1L;

	public DatosInvalidosException(String mensaje) {
		super(mensaje);
	}
}
