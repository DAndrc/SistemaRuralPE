package modelo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.Set;

import excepciones.DatosInvalidosException;

public class PersonalSalud extends Persona {

	// Roles autorizados para consultar historiales clínicos (control de acceso por rol).
	private static final Set<String> ROLES_CON_ACCESO_CLINICO = Set.of("enfermera", "medico", "médico");

	private String rol;
	private String usuario;
	private String passwordHash;

	public PersonalSalud(String dni, String nombres, String apellidos, LocalDate fechaNacimiento,
			String rol, String usuario, String passwordPlano) {
		super(dni, nombres, apellidos, fechaNacimiento);
		if (rol == null || rol.isBlank() || usuario == null || usuario.isBlank()
				|| passwordPlano == null || passwordPlano.isBlank()) {
			throw new DatosInvalidosException("Rol, usuario y contraseña son obligatorios");
		}
		this.rol = rol;
		this.usuario = usuario;
		this.passwordHash = hashPassword(passwordPlano);
	}

	public boolean autenticar(String pwd) {
		return pwd != null && this.passwordHash.equals(hashPassword(pwd));
	}

	public boolean puedeConsultarHistorial() {
		return ROLES_CON_ACCESO_CLINICO.contains(rol.toLowerCase());
	}

	// La contraseña nunca se guarda ni se compara en texto plano: solo su hash SHA-256 (Ley N.° 29733).
	private static String hashPassword(String textoPlano) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hashBytes = digest.digest(textoPlano.getBytes(StandardCharsets.UTF_8));
			StringBuilder hex = new StringBuilder();
			for (byte b : hashBytes) {
				hex.append(String.format("%02x", b));
			}
			return hex.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("No se pudo generar el hash de la contraseña", e);
		}
	}

	public String getRol() { return rol; }
	public String getUsuario() { return usuario; }
}
