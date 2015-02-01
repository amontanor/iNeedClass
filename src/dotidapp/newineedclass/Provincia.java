package dotidapp.newineedclass;

public class Provincia {
	String provincia="";
	int idProvincia=0;
	public Provincia(String idProvincia, String provincia) {
		this.provincia = provincia;
		this.idProvincia = this.idProvincia;
	}
	
	public Provincia() {

	}
	
	public String getProvincia() {
		return provincia;
	}
	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}
	public int getIdProvincia() {
		return idProvincia;
	}
	public void setIdProvincia(int idProvincia) {
		this.idProvincia = idProvincia;
	}
}
