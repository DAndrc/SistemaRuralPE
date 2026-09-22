package modelo;

import java.time.LocalDate;

public class PersonalSalud extends Persona{
	
	private String rol;
	private String usuario;
	private String passwordHash;
	
	public PersonalSalud(String dni, String nombres, String apellidos, LocalDate fechaNacimiento, String rol, String usuario, String passwordHash) {
		super (dni, nombres, apellidos, fechaNacimiento);
		this.rol=rol;
		this.usuario=usuario;
		this.passwordHash=passwordHash;
	}
	
	public boolean autenticar(String pwd) {
		return this.passwordHash.equals(pwd);
	}
	
	public String getRol() { return rol; }
	public String getUsuario() {return usuario; }

	
	
}