package dotidapp.newineedclass;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.BitmapAjaxCallback;

public class AdaptadorProfesores extends ArrayAdapter {
	Activity context;
	List<Profesor> profesores;

	AdaptadorProfesores(Activity context, List<Profesor> profesores) {
		super(context, R.layout.item_profesor);
		this.context = context;
		this.profesores = profesores;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		Typeface font = Typeface.createFromAsset(context.getAssets(),
				"fonts/Montserrat-Bold.ttf");
		/*
		 * LayoutInflater inflater = context.getLayoutInflater(); View item =
		 * inflater.inflate(R.layout.item_profesor, null);
		 */

		if (convertView == null) {
			convertView = context.getLayoutInflater().inflate(
					R.layout.item_profesor, null);
		}

		ImageView foto = (ImageView) convertView.findViewById(R.id.foto);
		AQuery aq = new AQuery(convertView);
		String urlImagen = context.getResources().getString(R.string.urlImages)
				+ profesores.get(position).getMail() + "enana.jpg";

		// invoke it with an image view
		aq.id(R.id.foto).image(urlImagen, true, true, 0, R.drawable.contact,
				null, AQuery.FADE_IN_NETWORK);

		TextView lblTxtMateria = (TextView) convertView.findViewById(R.id.txtMateria);
		TextView lblTxtPrecio = (TextView) convertView.findViewById(R.id.txtPrecio);
		TextView lblMateria = (TextView) convertView.findViewById(R.id.Materia);
		TextView lblPrecio = (TextView) convertView.findViewById(R.id.precio);
		TextView lblBusco = (TextView) convertView.findViewById(R.id.textoBusco);

		if (Herramientas.getEsCurso()) {
			lblTxtMateria.setText(R.string.txtMateria);
			lblTxtPrecio.setText(R.string.txtPrecio);
			lblMateria.setText(profesores.get(position).getNombreAsignatura());
			lblPrecio.setText(profesores.get(position).getPrecio());
		} else {
			lblTxtMateria.setText(R.string.txtOfrezco);
			lblTxtPrecio.setText(R.string.txtBusco);
			lblMateria.setText(profesores.get(position).getOfrezco());
			lblPrecio.setText(profesores.get(position).getBusco());
		}

		lblTxtMateria.setTypeface(font);
		lblTxtPrecio.setTypeface(font);

		return (convertView);
	}

	@Override
	public int getCount() {
		return profesores.size();
	}

	public static boolean exists(String URLName) {
		try {
			HttpURLConnection.setFollowRedirects(false);
			// note : you may also need
			// HttpURLConnection.setInstanceFollowRedirects(false)
			HttpURLConnection con = (HttpURLConnection) new URL(URLName)
					.openConnection();
			con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
