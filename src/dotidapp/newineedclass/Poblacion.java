package dotidapp.newineedclass;

public class Poblacion {
	public Poblacion(int idProvincia, String poblacion, int idPoblacion) {
		super();
		this.poblacion = poblacion;
		this.idPoblacion = idPoblacion;
		this.idProvincia = idProvincia;
	}
	public Poblacion() {
		super();
	}
	String poblacion="";
	int idPoblacion=0,idProvincia=0;
	public int getIdProvincia() {
		return idProvincia;
	}
	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
	}
	public String getPoblacion() {
		return poblacion;
	}
	public void setPoblacion(String poblacion) {
		this.poblacion = poblacion;
	}
	public int getIdPoblacion() {
		return idPoblacion;
	}
	public void setIdPoblacion(int idPoblacion) {
		this.idPoblacion = idPoblacion;
	}
}
