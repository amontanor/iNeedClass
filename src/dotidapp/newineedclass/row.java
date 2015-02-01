package dotidapp.newineedclass;

import android.graphics.drawable.Drawable;

public class row {
	
	private Drawable icono;
	private String titulo;
	private int id;
	public Drawable getIcono() {
		return icono;
	}
	public void setIcono(Drawable icono) {
		this.icono = icono;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public row(Drawable icono, String titulo, int id) {
		super();
		this.icono = icono;
		this.titulo = titulo;
		this.id = id;
	}
	

}
