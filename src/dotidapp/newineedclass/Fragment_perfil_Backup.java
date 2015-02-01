package dotidapp.newineedclass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class Fragment_perfil_Backup extends Fragment {

	private ShapeDrawable miImagen;
	private static Context contexto;
	private View V;
	private String titulo;
	private static EditText textoNombre;
	private static EditText textoApellido1;
	private static EditText textoApellido2;
	private static EditText textoFechaNacimiento;
	private static EditText textoTlf;
	private static EditText textoNacionalidad;
	private static EditText textoEstudios;
	private static Spinner textoPoblacion, textoProvincia;
	private ImageView imagen;

	private static SQLiteHelper usdbh;
	private static SQLiteDatabase db;
	Fragment fragment = this;
	private static Bitmap bitmap;
	private String urlBitmap;
	public static ArrayAdapter adapterPoblacion;
	private static boolean subirImagen;
	private static List<Poblacion> listaPoblacion;
	private static boolean actualizarPoblacion = false;
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

		actualizarPoblacion = false;
		
		Herramientas.rescatarPoblacionProvincia(contexto);

		// Capturar objetos de la pantalla
		textoNombre = (EditText) getActivity().findViewById(R.id.textoNombre);
		textoApellido1 = (EditText) getActivity().findViewById(
				R.id.textoApellido1);
		textoApellido2 = (EditText) getActivity().findViewById(
				R.id.textoApellido2);
		textoFechaNacimiento = (EditText) getActivity().findViewById(
				R.id.textoFechaNacimiento);
		textoTlf = (EditText) getActivity().findViewById(R.id.textoTlf);
		textoNacionalidad = (EditText) getActivity().findViewById(
				R.id.textoNacionalidad);
		textoEstudios = (EditText) getActivity().findViewById(
				R.id.textoEstudios);
		textoPoblacion = (Spinner) getActivity().findViewById(
				R.id.textoPoblacion);
		textoProvincia = (Spinner) getActivity().findViewById(
				R.id.textoProvincia);
		textoProvincia.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v, int pos,
					long id) {
				if (actualizarPoblacion) {
					// Poblacion
					int idProvincia = Herramientas.getListaProvincia().get(pos)
							.getIdProvincia();
					// Capturar listaPoblacionesy
					actualizarPoblacion(idProvincia);
					adapterPoblacion.notifyDataSetChanged();
				} else {
					actualizarPoblacion = true;
				}
			}

			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

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
		textoApellido2.setText(usuario.getApellido2());
		textoFechaNacimiento.setText(usuario.getFnac());
		textoTlf.setText(usuario.getTlf());
		textoNacionalidad.setText(usuario.getNacionalidad());
		textoEstudios.setText(usuario.getEstudios());

		cargarProvinciasYPoblacion();

	}

	private void cargarProvinciasYPoblacion() {

		Herramientas.descargarListaProvincia(getActivity());

		ArrayAdapter adapterProvincia = new ArrayAdapter(contexto,
				android.R.layout.simple_spinner_item,
				Herramientas.getListaProvinciaString());
		textoProvincia.setAdapter(adapterProvincia);

		// Mostrar la provincia del usuario
		int posSpinnerProvincia = adapterProvincia.getPosition(Herramientas
				.getPoblacionProvinciaUsuario()[1]);
		textoProvincia.setSelection(posSpinnerProvincia);

		// Buscar el id de la provincia
		int idProvincia = Herramientas.getListaProvincia()
				.get(posSpinnerProvincia).getIdProvincia();
		// Capturar listaPoblaciones
		actualizarPoblacion(idProvincia);

		// Comprobamos si hemos reiniciado, insertamos solo la poblacion actual
		if (Herramientas.getListaPoblacionString().size() == 0) {
			Herramientas.getListaPoblacionString().add(
					Herramientas.getPoblacionProvinciaUsuario()[0]);
		}
		// Poblacion
		adapterPoblacion = new ArrayAdapter(contexto,
				android.R.layout.simple_spinner_item,
				Herramientas.getListaPoblacionString());
		textoPoblacion.setAdapter(adapterPoblacion);

		// Mostrar la provincia del usuario
		int posSpinnerPoblacion = adapterPoblacion.getPosition(Herramientas
				.getPoblacionProvinciaUsuario()[0]);
		textoPoblacion.setSelection(posSpinnerPoblacion);

		if (Herramientas.getUsuarioLogado().getImagen() != null) {
			imagen.setImageBitmap(Herramientas.getUsuarioLogado().getImagen());
		}

	}

	public static void actualizarPoblacion(int idPoblacion) {
		listaPoblacion = new ArrayList<Poblacion>();
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=15&idprovincia="
						+ idPoblacion);
		try {
			adapterPoblacion.clear();
		} catch (Exception e) {

		} finally {
			listaPoblacion = Herramientas
					.parsearPoblacion(resultJson, contexto);
			textoPoblacion.setSelection(0);
		}
		// insertarPoblacionEnBBDD(listaPoblacion);
	}

	private static void insertarPoblacionEnBBDD(List<Poblacion> listaPoblacion) {
		Iterator iterator = listaPoblacion.iterator();
		while (iterator.hasNext()) {
			Poblacion poblacion = new Poblacion();
			poblacion = (Poblacion) iterator.next();
			usdbh.insertarPoblacion(db, poblacion);
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
				imageView.setImageBitmap(bitmap);
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

	public Spinner getTextoPoblacion() {
		return textoPoblacion;
	}

	public void setTextoPoblacion(Spinner textoPoblacion) {
		this.textoPoblacion = textoPoblacion;
	}

	public Spinner getTextoProvincia() {
		return textoProvincia;
	}

	public void setTextoProvincia(Spinner textoProvincia) {
		this.textoProvincia = textoProvincia;
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
			Herramientas.getUsuarioLogado().setApellido2(
					textoApellido2.getText().toString());
			Herramientas.getUsuarioLogado().setFnac(
					textoFechaNacimiento.getText().toString());
			Herramientas.getUsuarioLogado().setTlf(
					textoTlf.getText().toString());
			Herramientas.getUsuarioLogado().setNacionalidad(
					textoNacionalidad.getText().toString());
			Herramientas.getUsuarioLogado().setEstudios(
					textoEstudios.getText().toString());

			Herramientas.getUsuarioLogado().setPoblacion(
					textoPoblacion.getSelectedItem().toString());

			// Buscamos el id de la poblacion
			int idPoblacion = buscarIdPoblacion();
			// Guardamos el id de la provincia en sesion
			Herramientas.getUsuarioLogado().setId_poblacion(
					String.valueOf(idPoblacion));
			// Guardamos la ProvinciaPoblacion
			Herramientas.getPoblacionProvinciaUsuario()[0] = textoPoblacion
					.getSelectedItem().toString();
			Herramientas.getPoblacionProvinciaUsuario()[1] = textoProvincia
					.getSelectedItem().toString();

			// Guardamos el resto de datos
			Herramientas.getUsuarioLogado().setProvincia(
					textoProvincia.getSelectedItem().toString());

			Herramientas.getUsuarioLogado().setFnac(
					textoFechaNacimiento.getText().toString());
			if (subirImagen) {
				Herramientas.getUsuarioLogado().setImagen(bitmap);
			}
		}

		private int buscarIdPoblacion() {
			int posSpinnerPoblacion = adapterPoblacion
					.getPosition(textoPoblacion.getSelectedItem().toString());

			return listaPoblacion.get(posSpinnerPoblacion).getIdPoblacion();
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
							+ "&apellido2="
							+ Herramientas.getUsuarioLogado().getApellido2()
							+ "&fnac="
							+ Herramientas.getUsuarioLogado().getFnac()
							+ "&tlf="
							+ Herramientas.getUsuarioLogado().getTlf()
							+ "&nacionalidad="
							+ Herramientas.getUsuarioLogado().getNacionalidad()
							+ "&idpoblacion="
							+ Herramientas.getUsuarioLogado().getId_poblacion()
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

		private void capturarPoblacionProvincia() {
			String resultJson = Herramientas
					.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=13&usuario="
							+ Herramientas.getUsuarioLogado().getMail());

		}

	}

}