package dotidapp.newineedclass;

public class Categoria {
	String id, nombre;

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

	public Categoria(String id, String nombre) {
		super();
		this.id = id;
		this.nombre = nombre;
	}
	
}
