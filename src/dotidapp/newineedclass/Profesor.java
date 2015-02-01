package dotidapp.newineedclass;

public class Profesor extends Usuario{
	
	private String precio = "", nombreAsignatura = "", comentario="", ofrezco="", busco="";

	public String getPrecio() {
		return precio;
	}

	public void setPrecio(String precio) {
		this.precio = precio;
	}

	public String getNombreAsignatura() {
		return nombreAsignatura;
	}

	public void setNombreAsignatura(String nombreAsignatura) {
		this.nombreAsignatura = nombreAsignatura;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public String getOfrezco() {
		return ofrezco;
	}

	public void setOfrezco(String ofrezco) {
		this.ofrezco = ofrezco;
	}

	public String getBusco() {
		return busco;
	}

	public void setBusco(String busco) {
		this.busco = busco;
	}

}
