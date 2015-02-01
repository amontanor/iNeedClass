package dotidapp.newineedclass;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

public class DescargaFotos implements Runnable {

	private String mail;
	private Context contexto;
	private Usuario usuario;
	private AdaptadorProfesores adaptadorProfesores;
	private boolean refrescarListaProfesores;
	private Activity actividad;

	public DescargaFotos(String mail, Context contexto, Usuario usuario,
			AdaptadorProfesores adaptadorProfesores,
			boolean refrescarListaProfesores, Activity actividad) {
		super();
		this.mail = mail;
		this.contexto = contexto;
		this.usuario = usuario;
		this.adaptadorProfesores = adaptadorProfesores;
		this.refrescarListaProfesores = refrescarListaProfesores;
		this.actividad = actividad;
	}

	@Override
	public void run() {
		Bitmap imagen = rescatarImagen(mail, contexto);
		if (imagen != null) {
			usuario.setImagen(imagen);
		}
	}

	public Bitmap rescatarImagen(String mail, Context contexto) {
		try {
			while (Herramientas.getSemaforoOcupadoImagenes()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Herramientas.setSemaforoOcupadoImagenes(true);
			java.net.URL url = new java.net.URL(contexto.getResources()
					.getString(R.string.urlImages) + mail + ".jpg");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			Bitmap myBitmap = null;
			if ((connection.getResponseCode() == HttpURLConnection.HTTP_OK)) {
				connection.connect();
				InputStream input = connection.getInputStream();
				myBitmap = BitmapFactory.decodeStream(input);

				if (refrescarListaProfesores) {
					adaptadorProfesores.notifyDataSetChanged();
				}
			}
			Herramientas.setSemaforoOcupadoImagenes(false);
			return myBitmap;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
