package dotidapp.newineedclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Radio;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.R.id;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;

public class Fragment_perfil extends Fragment {

	private ShapeDrawable miImagen;
	private static Context contexto;
	private View V;
	private String titulo;
	private static EditText textoNombre;
	private static EditText textoApellido1;
	private static RadioButton radioHombre;
	private static RadioButton radioMujer;
	private static EditText textoFechaNacimiento;
	private static EditText textoPassword;
	private static Switch switchMail;
	private ImageView imagen;

	private static SQLiteHelper usdbh;
	private static SQLiteDatabase db;
	Fragment fragment = this;
	private static Bitmap bitmap;
	private String urlBitmap;
	private static boolean subirImagen;
	private static OutputStream fOut;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Cambiar el menu del action bar
		setHasOptionsMenu(true);

		contexto = container.getContext();
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");

		V = inflater.inflate(R.layout.fragment_perfil, container, false);

		return V;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Capturar objetos de la pantalla
		textoNombre = (EditText) getActivity().findViewById(R.id.textoNombre);
		textoApellido1 = (EditText) getActivity().findViewById(
				R.id.textoApellido1);
		radioHombre = (RadioButton) getActivity().findViewById(R.id.radioHombre);
		radioMujer = (RadioButton) getActivity().findViewById(R.id.radioMujer);
		switchMail = (Switch) getActivity().findViewById(R.id.switchMail);
		textoFechaNacimiento = (EditText) getActivity().findViewById(
				R.id.textoFechaNacimiento);
		textoPassword = (EditText) getActivity().findViewById(
				R.id.textoContrasena);
		imagen = (ImageView) getActivity().findViewById(R.id.imageFoto);

		imagen.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
				intent.setType("image/*");
				fragment.startActivityForResult(intent, 1234);
			}
		});

		// Buscar datos
		Usuario usuario = Herramientas.getUsuarioLogado();
		rescatarDatos(usuario);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.perfil, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void rescatarDatos(Usuario usuario) {

		// Se usan los datos del usuario en session
		textoNombre.setText(usuario.getNombre());
		textoApellido1.setText(usuario.getApellido1());
		if (usuario.getSexo().equals("H"))
		{
			radioHombre.setChecked(true);
			radioMujer.setChecked(false);
		}
		else
		{
			radioHombre.setChecked(false);
			radioMujer.setChecked(true);
		}
		switchMail.setChecked(usuario.getNotificarMail().equals("S"));
		textoFechaNacimiento.setText(usuario.getFnac());
		textoPassword.setTag(usuario.getPassword());
		if (Herramientas.getUsuarioLogado().getImagen() != null) {
			imagen.setImageBitmap(Herramientas.getImagenRedonda(Herramientas.getUsuarioLogado().getImagen(), 130, 130,contexto));
			//imagen.setImageBitmap(Herramientas.getUsuarioLogado().getImagen());
		}
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.save:
			SaveThread saveThread = new SaveThread(contexto);
			saveThread.execute();

			break;
		}
		return true;
	}

	private static void duplicarImagenMasPequena() {

		String path = Environment.getExternalStorageDirectory().toString();
		fOut = null;
		File file = new File(contexto.getFilesDir(), "image.tmp");
		try {
			fOut = new FileOutputStream(file);

			Herramientas.getUsuarioLogado().getImagen()
					.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			fOut.flush();
			fOut.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			Uri targetUri = data.getData();
			bitmap = BitmapFactory.decodeStream(getActivity()
					.getContentResolver().openInputStream(targetUri));
			ImageView imageView = (ImageView) getActivity().findViewById(
					R.id.imageFoto);
			if (bitmap != null) {
				bitmap = cambiarTamano(bitmap);
				imageView.setImageBitmap(Herramientas.getImagenRedonda(bitmap, 130, 130, contexto));
				Herramientas.getUsuarioLogado().setImagen(bitmap);
				subirImagen = true;
				urlBitmap = obtenerFileName(targetUri);
			} else {
				subirImagen = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private Bitmap cambiarTamano(Bitmap image) {
		int anchoOriginal, altoOriginal;
		int anchoModificado, altoModificado;
		float escala;

		anchoOriginal = bitmap.getWidth();
		altoOriginal = bitmap.getHeight();

		anchoModificado = getView().getWidth();
		escala = Float.valueOf(anchoOriginal) / Float.valueOf(anchoModificado);
		altoModificado = (int) (altoOriginal / escala);
		;

		Bitmap resizedBitmap = Bitmap.createScaledBitmap(image,
				anchoModificado, altoModificado, true);
		return resizedBitmap;
	}

	private String obtenerFileName(Uri uri) {
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = getActivity().getContentResolver().query(uri,
				projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		String fpath = cursor.getString(column_index);
		return fpath;
	}


	private static class SaveThread extends AsyncTask<Void, Void, Void> {

		private AlertDialog alert = null, alertNuevaTarjeta = null;
		private Builder ringProgressDialogMapa, insertarNuevaTarjeta;
		private Context contexto;
		private boolean exito;

		public SaveThread(Context contexto) {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
				this.contexto = contexto;
			}
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ringProgressDialogMapa = new AlertDialog.Builder(contexto);
			ringProgressDialogMapa.setMessage(contexto.getResources()
					.getString(R.string.guardandoTxt));
			ringProgressDialogMapa.setCancelable(false);

			alert = ringProgressDialogMapa.create();

			alert.show();

		}

		@Override
		protected Void doInBackground(Void... accion) {
			guardarDatosSession();
			guardarDatosBaseDeDatos();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			alert.cancel();

		}

		private void guardarDatosSession() {
			Herramientas.getUsuarioLogado().setNombre(
					textoNombre.getText().toString());
			Herramientas.getUsuarioLogado().setApellido1(
					textoApellido1.getText().toString());
			if (radioHombre.isChecked())
			{
				Herramientas.getUsuarioLogado().setSexo("H");
			}
			else
			{
				Herramientas.getUsuarioLogado().setSexo("M");
			}
			if (switchMail.isChecked())
			{
				Herramientas.getUsuarioLogado().setNotificarMail("S");
			}
			else
			{
				Herramientas.getUsuarioLogado().setNotificarMail("N");
			}
				
			Herramientas.getUsuarioLogado().setFnac(
					textoFechaNacimiento.getText().toString());
			Herramientas.getUsuarioLogado().setPassword(textoPassword.getText().toString());
			Herramientas.getUsuarioLogado().setFnac(
					textoFechaNacimiento.getText().toString());
			if (subirImagen) {
				Herramientas.getUsuarioLogado().setImagen(bitmap);
			}
		}

		private void guardarDatosBaseDeDatos() {
			String imagen;
			String resultJson = Herramientas
					.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=12&usuario="
							+ Herramientas.getUsuarioLogado().getMail()
							+ "&nombre="
							+ Herramientas.getUsuarioLogado().getNombre()
							+ "&apellido1="
							+ Herramientas.getUsuarioLogado().getApellido1()
							+ "&password="
							+ Herramientas.getUsuarioLogado().getPassword()
							+ "&fnac="
							+ Herramientas.getUsuarioLogado().getFnac()
							+ "&tlf="
							+ Herramientas.getUsuarioLogado().getTlf()
							+ "&sexo="
							+ Herramientas.getUsuarioLogado().getSexo()
							+ "&notificarMail="
							+ Herramientas.getUsuarioLogado().getNotificarMail()
							+ "&estudios="
							+ Herramientas.getUsuarioLogado().getEstudios()
							+ "");
			if (subirImagen) {
				duplicarImagenMasPequena();
				int respuesta = Herramientas.uploadFile(contexto.getFilesDir()
						+ "/image.tmp", (Activity) contexto, false);
				duplicarImagenEnana();
				respuesta = Herramientas.uploadFile(contexto.getFilesDir()
						+ "/image.tmp", (Activity) contexto, true);
			}
		}

		private void duplicarImagenEnana() {
			String path = Environment.getExternalStorageDirectory().toString();
			fOut = null;
			File file = new File(contexto.getFilesDir(), "image.tmp");
			try {
				fOut = new FileOutputStream(file);

				Bitmap.createScaledBitmap(Herramientas.getUsuarioLogado().getImagen(), 
						Herramientas.getUsuarioLogado().getImagen().getWidth()/10, 
						Herramientas.getUsuarioLogado().getImagen().getHeight()/10,
				true).compress(Bitmap.CompressFormat.JPEG, 100, fOut);
				fOut.flush();
				fOut.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}