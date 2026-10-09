package excepciones;

/** Se lanza cuando un usuario intenta acceder a datos sensibles sin tener el rol adecuado. */
public class AccesoDenegadoException extends SistemaRuralException {
	private static final long serialVersionUID = 1L;

	public AccesoDenegadoException(String mensaje) {
		super(mensaje);
	}
}
