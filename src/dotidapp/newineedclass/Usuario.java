package dotidapp.newineedclass;

import android.graphics.Bitmap;

public class Usuario {

	String id="", nombre="", apellido1="", apellido2="", fnac=""
			, tlf="", mail="", estudios="", poblacion="",provincia="",
			nacionalidad="", id_poblacion="", id_tipo="", password="", sexo="", notificarMail="";
	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getNotificarMail() {
		return notificarMail;
	}

	public void setNotificarMail(String notificarMail) {
		this.notificarMail = notificarMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProvincia() {
		return provincia;
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getPoblacion() {
		return poblacion;
	}

	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}

	public String getEstudios() {
		return estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	Bitmap imagen;

	public Bitmap getImagen() {
		return imagen;
	}

	public void setImagen(Bitmap imagen) {
		this.imagen = imagen;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getFnac() {
		return fnac;
	}

	public void setFnac(String fnac) {
		this.fnac = fnac;
	}

	public String getTlf() {
		return tlf;
	}

	public void setTlf(String tlf) {
		this.tlf = tlf;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getId_poblacion() {
		return id_poblacion;
	}

	public void setId_poblacion(String id_poblacion) {
		this.id_poblacion = id_poblacion;
	}

	public String getId_tipo() {
		return id_tipo;
	}

	public void setId_tipo(String id_tipo) {
		this.id_tipo = id_tipo;
	}
	
	
}
