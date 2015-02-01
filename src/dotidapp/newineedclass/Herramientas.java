package dotidapp.newineedclass;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ListView;

public class Herramientas {

	public static List<Profesor> getListaProfesores() {
		return listaProfesores;
	}

	public static void setListaProfesores(List<Profesor> listaProfesores) {
		Herramientas.listaProfesores = listaProfesores;
	}

	private static InputStream is;
	private static JSONArray jArray;
	private static Usuario usuarioLogado;
	private static Profesor profesor;

	private static Boolean loginConFacebook = false;
	private static Boolean loginPass = false;
	private static Boolean semaforoOcupadoImagenes = false;
	private static SQLiteHelper usdbh;
	private static SQLiteDatabase db;
	private static List<Poblacion> listaPoblacion = new ArrayList<Poblacion>();
	private static List<String> listaPoblacionString = new ArrayList<String>();
	private static List<Profesor> listaProfesores = new ArrayList<Profesor>();

	private static List<Categoria> listaCategoria = new ArrayList<Categoria>();
	private static List<String> listaCategoriaString = new ArrayList<String>();

	private static DescargaFotos descargaFotos;
	private static Boolean esCurso;

	public static List<Categoria> getListaCategoria() {
		return listaCategoria;
	}

	public static void setListaCategoria(List<Categoria> listaCategoria) {
		Herramientas.listaCategoria = listaCategoria;
	}

	public static List<String> getListaCategoriaString() {
		return listaCategoriaString;
	}

	public static void setListaCategoriaString(List<String> listaCategoriaString) {
		Herramientas.listaCategoriaString = listaCategoriaString;
	}

	private static List<Provincia> listaProvincia = new ArrayList<Provincia>();
	private static List<String> listaProvinciaString = new ArrayList<String>();

	private static List<String> listaMateriaString = new ArrayList<String>();

	public static List<String> getListaMateriaString() {
		return listaMateriaString;
	}

	public static void setListaMateriaString(List<String> listaMateriaString) {
		Herramientas.listaMateriaString = listaMateriaString;
	}

	private static String[] poblacionProvinciaUsuario;

	public static String jsonLoad(String url) {

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		try {
			// defaultHttpClient

			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String json = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			sb.append(reader.readLine() + "\n");
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return json;
	}

	public static Bitmap getImagenRedonda(Bitmap scaleBitmapImage,
			int targetWidth, int targetHeight, Context context) {

		float ancho = (int) convertDpToPixel(targetWidth, context);
		float alto = (int) convertDpToPixel(targetHeight, context);
		Bitmap targetBitmap = Bitmap.createBitmap((int) ancho, (int) alto,
				Bitmap.Config.ARGB_8888);

		Canvas canvas = new Canvas(targetBitmap);
		Path path = new Path();
		path.addCircle(((float) ancho) / 2, ((float) alto) / 2,
				(Math.min(((float) ancho), ((float) alto)) / 2),
				Path.Direction.CCW);

		canvas.clipPath(path);
		Bitmap sourceBitmap = scaleBitmapImage;
		canvas.drawBitmap(sourceBitmap, new Rect(0, 0, sourceBitmap.getWidth(),
				sourceBitmap.getHeight()), new Rect(0, 0, (int) ancho,
				(int) alto), null);
		return targetBitmap;
	}

	public static float convertDpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160);
		return px;
	}

	public static Usuario parsearUsuario(String resultJson, Context contexto) {
		Usuario usuario = new Usuario();
		try {
			jArray = new JSONArray(resultJson);
			if (jArray.length() > 0) {
				usuario.id = jArray.getJSONObject(0).getString("id");
				usuario.mail = jArray.getJSONObject(0).getString("mail");
				usuario.nombre = jArray.getJSONObject(0).getString("nombre");
				usuario.apellido1 = jArray.getJSONObject(0).getString(
						"apellido1");
				usuario.password = jArray.getJSONObject(0)
						.getString("password");
				usuario.fnac = jArray.getJSONObject(0).getString("fnac");
				usuario.estudios = jArray.getJSONObject(0)
						.getString("cursando");
				usuario.setSexo(jArray.getJSONObject(0).getString("sexo"));
				usuario.setNotificarMail(jArray.getJSONObject(0).getString(
						"notificarMail"));
				usuario.setId_poblacion(jArray.getJSONObject(0).getString(
						"idpoblacion"));
				usuario.tlf = jArray.getJSONObject(0).getString("telefono");

				descargaFotos = new DescargaFotos(usuario.mail, contexto,
						usuario, null, false, null);
				new Thread(descargaFotos).start();

			} else {
				return null;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return usuario;
	}

	public static List<Provincia> parsearProvincia(String resultJson,
			Context contexto) {
		listaProvincia = new ArrayList<Provincia>();
		try {
			jArray = new JSONArray(resultJson);
			if (listaProvinciaString.size() != 0) {
				listaProvincia.clear();
				listaProvinciaString.clear();
			}
			for (int i = 0; jArray.length() > 0; i++) {
				Provincia provincia = new Provincia();
				provincia.setIdProvincia(jArray.getJSONObject(i).getInt(
						"idprovincia"));
				provincia.setProvincia(jArray.getJSONObject(i).getString(
						"provincia"));
				listaProvinciaString.add(jArray.getJSONObject(i).getString(
						"provincia"));
				listaProvincia.add(provincia);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaProvincia;
	}

	public static void actualizarProvincia(Context contexto) {
		borrarTablasProvinciaYPoblacion();
		descargarListaProvincia(contexto);
		insertarProvinciaEnBBDD(listaProvincia);
	}

	public static void descargarListaProvincia(Context contexto) {
		List<Provincia> listaProvincia = new ArrayList<Provincia>();
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=14");
		listaProvincia = Herramientas.parsearProvincia(resultJson, contexto);
	}

	private static void insertarProvinciaEnBBDD(List<Provincia> listaProvincia) {
		Iterator iterator = listaProvincia.iterator();
		while (iterator.hasNext()) {
			Provincia provincia = new Provincia();
			provincia = (Provincia) iterator.next();
			usdbh.insertarProvincia(db, provincia);
		}

	}

	private static void borrarTablasProvinciaYPoblacion() {
		usdbh.vaciasPoblacionProvincia(db);

	}

	public static List<Poblacion> parsearPoblacion(String resultJson,
			Context contexto) {
		listaPoblacion = new ArrayList<Poblacion>();
		try {
			jArray = new JSONArray(resultJson);
			for (int i = 0; jArray.length() > 0; i++) {
				Poblacion poblacion = new Poblacion();
				poblacion.setIdPoblacion(jArray.getJSONObject(i).getInt(
						"idpoblacion"));
				poblacion.setIdProvincia(jArray.getJSONObject(i).getInt(
						"idprovincia"));
				poblacion.setPoblacion(jArray.getJSONObject(i).getString(
						"poblacion"));
				listaPoblacion.add(poblacion);
				listaPoblacionString.add(jArray.getJSONObject(i).getString(
						"poblacion"));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listaPoblacion;
	}

	public static String[] parsearPoblacionProvincia(String resultJson,
			Context contexto) {
		String[] resultado = new String[2];
		try {
			jArray = new JSONArray(resultJson);
			if (jArray.length() > 0) {
				resultado[0] = jArray.getJSONObject(0).getString("poblacion");
				resultado[1] = jArray.getJSONObject(0).getString("provincia");
			} else {
				return null;
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return resultado;
	}

	public static Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public static void setUsuarioLogado(Usuario usuarioLogado) {
		Herramientas.usuarioLogado = usuarioLogado;
	}

	public static boolean rescatarUsuarioInternet(Context contexto,
			String mail, String pass) {
		// Capturamos datos del usuario
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=11&usuario="
						+ mail + "&password=" + pass);

		Usuario usuario = Herramientas.parsearUsuario(resultJson, contexto);
		if (usuario == null) {
			return false;
		}
		Herramientas.setUsuarioLogado(usuario);

		return true;

	}

	public static void rescatarPoblacionProvincia(Context contexto) {
		// Capturamos poblacion y provincia del usuario
		setPoblacionProvinciaUsuario(new String[2]);
		if (Herramientas.getUsuarioLogado() != null) {
			String resultJson = Herramientas
					.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=13&usuario="
							+ Herramientas.getUsuarioLogado().getMail());
			setPoblacionProvinciaUsuario(new String[2]);
			// setPoblacionProvinciaUsuario(parsearPoblacionProvincia(resultJson,
			// contexto));
		} else {
			Herramientas.poblacionProvinciaUsuario[1] = "Alava";
			Herramientas.poblacionProvinciaUsuario[0] = "Alegria-Dulantzi";
		}

	}

	public static void rescatarUsuarioBaseDeDatos(Context contexto) {
		Cursor c = db.rawQuery(
				"SELECT valor from Configuracion where nombre='login';", null);
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			String mail = c.getString(0);
			if (!mail.equals('n')) {
				Herramientas.setUsuarioLogado(new Usuario());
				Herramientas.getUsuarioLogado().setMail(mail);
			}
		}
	}

	public static void insertarUsuarioBaseDeDatos(Context contexto) {
		ContentValues cv = new ContentValues();
		usdbh = new SQLiteHelper(contexto, "baseDeDatos", null, 1);

		db = usdbh.getWritableDatabase();
		usdbh.usuarioConectado(db);

		if (Herramientas.getUsuarioLogado().getImagen() != null) {
			cv.put("imagen", getBytes(Herramientas.getUsuarioLogado()
					.getImagen()));
		}
		byte[] byteImage1 = null;
		cv.put("nombre", Herramientas.getUsuarioLogado().getNombre());
		cv.put("apellido1", Herramientas.getUsuarioLogado().getApellido1());
		cv.put("notificarMail", Herramientas.getUsuarioLogado()
				.getNotificarMail());
		cv.put("password", Herramientas.getUsuarioLogado().getPassword());
		cv.put("mail", Herramientas.getUsuarioLogado().getMail());
		cv.put("tlf", Herramientas.getUsuarioLogado().getTlf());
		cv.put("fnac", Herramientas.getUsuarioLogado().getFnac());
		cv.put("sexo", Herramientas.getUsuarioLogado().getSexo());
		cv.put("idpoblacion", Herramientas.getUsuarioLogado().getId_poblacion());
		db.insert("usuario", null, cv);
	}

	public static Boolean getLoginPass() {
		return loginPass;
	}

	public static void setLoginPass(Boolean loginPass) {
		Herramientas.loginPass = loginPass;
	}

	public static Boolean getLoginConFacebook() {
		return loginConFacebook;
	}

	public static void setLoginConFacebook(Boolean loginConFacebook) {
		Herramientas.loginConFacebook = loginConFacebook;
	}

	public static int uploadFile(String sourceFileUri, Activity actividad,
			Boolean enana) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		int serverResponseCode = 0;
		try {

			// open a URL connection to the Servlet
			FileInputStream fileInputStream = new FileInputStream(sourceFile);
			URL url = new URL(actividad.getResources().getString(
					R.string.urlServer));

			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", fileName);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			if (enana) {
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ getUsuarioLogado().getMail()
						+ "enana.jpg"
						+ "\""
						+ lineEnd);
			} else {
				dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
						+ getUsuarioLogado().getMail()
						+ ".jpg"
						+ "\""
						+ lineEnd);
			}
			dos.writeBytes(lineEnd);

			// create a buffer of maximum size
			bytesAvailable = fileInputStream.available();

			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];

			// read file and write it into form...
			bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			while (bytesRead > 0) {

				dos.write(buffer, 0, bufferSize);
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

			}

			// send multipart form data necesssary after file data...
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();

			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);

			if (serverResponseCode == 200) {

			}

			// close the streams //
			fileInputStream.close();
			dos.flush();
			dos.close();

		} catch (Exception e) {
			int a = 0;
		}
		return serverResponseCode;

	}

	public static byte[] getBytes(Bitmap bitmap) {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 0, stream);
		return stream.toByteArray();
	}

	public static List<Poblacion> getListaPoblacion() {
		return listaPoblacion;
	}

	public static void setListaPoblacion(List<Poblacion> listaPoblacion) {
		Herramientas.listaPoblacion = listaPoblacion;
	}

	public static List<String> getListaPoblacionString() {
		return listaPoblacionString;
	}

	public static void setListaPoblacionString(List<String> listaPoblacionString) {
		Herramientas.listaPoblacionString = listaPoblacionString;
	}

	public static List<String> getListaProvinciaString() {
		return listaProvinciaString;
	}

	public static void setListaProvinciaString(List<String> listaProvinciaString) {
		Herramientas.listaProvinciaString = listaProvinciaString;
	}

	public static List<Provincia> getListaProvincia() {
		return listaProvincia;
	}

	public static void setListaProvincia(List<Provincia> listaProvincia) {
		Herramientas.listaProvincia = listaProvincia;
	}

	public static String[] getPoblacionProvinciaUsuario() {
		return poblacionProvinciaUsuario;
	}

	public static void setPoblacionProvinciaUsuario(
			String[] poblacionProvinciaUsuario) {
		Herramientas.poblacionProvinciaUsuario = poblacionProvinciaUsuario;
	}

	public static void parsearMateria(String resultJson,
			FragmentActivity activity) {
		listaMateriaString.clear();
		try {
			jArray = new JSONArray(resultJson);
			for (int i = 0; i < jArray.length(); i++) {
				listaMateriaString.add(jArray.getJSONObject(i).getString(
						"nombre"));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void parsearCategoria(String resultJson,
			FragmentActivity activity) {

		listaCategoriaString.clear();
		listaCategoria.clear();
		try {
			jArray = new JSONArray(resultJson);
			for (int i = 0; i < jArray.length(); i++) {
				listaCategoriaString.add(jArray.getJSONObject(i).getString(
						"nombre"));
				listaCategoria.add(new Categoria(jArray.getJSONObject(i)
						.getString("id"), jArray.getJSONObject(i).getString(
						"nombre")));
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void parsearProfesores(String resultJson,
			FragmentActivity activity, Context contexto,
			AdaptadorProfesores adaptadorProfesores,
			boolean refrescarListaProfesores, final Activity actividad,
			ListView listProfesores, int modo) {

		listaProfesores.clear();

		try {
			jArray = new JSONArray(resultJson);
			for (int i = 0; i < jArray.length(); i++) {
				Profesor profe = new Profesor();
				profe.setId(jArray.getJSONObject(i).getString("id"));
				profe.setNombre(jArray.getJSONObject(i).getString("nombre"));
				profe.setApellido1(jArray.getJSONObject(i).getString(
						"apellido1"));
				profe.setFnac(jArray.getJSONObject(i).getString("fnac"));
				profe.setComentario(jArray.getJSONObject(i).getString(
						"comentario"));
				profe.setPrecio(jArray.getJSONObject(i).getString("precio"));
				profe.setSexo(jArray.getJSONObject(i).getString("fnac"));
				profe.setTlf(jArray.getJSONObject(i).getString("telefono"));
				profe.setMail(jArray.getJSONObject(i).getString("mail"));
				profe.setPrecio(jArray.getJSONObject(i).getString("precio"));
				profe.setNombreAsignatura(jArray.getJSONObject(i)
						.getString("nombreAsignatura").replace("%20", " ").replace("%30", "ñ"));
				profe.setBusco(jArray.getJSONObject(i)
						.getString("nombreBusco").replace("%20", " ").replace("%30", "ñ"));
				profe.setOfrezco(jArray.getJSONObject(i)
						.getString("nombreOfrezco").replace("%20", " ").replace("%30", "ñ"));

				boolean estaAsignaturaEsCurso = jArray.getJSONObject(i)
						.getString("nombreBusco").replace("%20", " ").replace("%30", "ñ").equals("null");
				if (estaAsignaturaEsCurso && modo == 1) {
					listaProfesores.add(profe);
				}
				if (!estaAsignaturaEsCurso && modo == 2) {
					listaProfesores.add(profe);
				}
				adaptadorProfesores.setNotifyOnChange(true);
				listProfesores.setAdapter(adaptadorProfesores);

			}
			Herramientas.setListaProfesores(listaProfesores);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Profesor getProfesor() {
		return profesor;
	}

	public static void setProfesor(Profesor profesor) {
		Herramientas.profesor = profesor;
	}

	public static Boolean getSemaforoOcupadoImagenes() {
		return semaforoOcupadoImagenes;
	}

	public static void setSemaforoOcupadoImagenes(
			Boolean semaforoOcupadoImagenes) {
		Herramientas.semaforoOcupadoImagenes = semaforoOcupadoImagenes;
	}

	public static Boolean getEsCurso() {
		return esCurso;
	}

	public static void setEsCurso(Boolean esCurso) {
		Herramientas.esCurso = esCurso;
	}

}
