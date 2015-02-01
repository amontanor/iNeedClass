package dotidapp.newineedclass;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class Fragment_login extends Fragment {

	// Propiedades
	private static InputStream is;
	private ShapeDrawable miImagen;
	private static Context contexto;
	private View V;
	private String titulo;
	private static LoginButton loginFcBtn;
	private Button loginButton;
	private TextView linkNuevoUsuario;
	private static SQLiteHelper usdbh;
	private static SQLiteDatabase db;
	private static Button btnLogin;
	private static EditText txtUsuario;
	private static EditText txtPassword;

	// Permisos Facebook
	private static final List<String> PERMISSIONS = Arrays.asList("email",
			"user_location");
	private UiLifecycleHelper uiHelper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// El contexto sera usado para todos los metodos
		contexto = container.getContext();

		// Estilo de la fuente
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");
		V = inflater.inflate(R.layout.fragment_login, container, false);
		
		return V;
	}
	
	private Session.StatusCallback statusCallback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			if (state.isOpened()) {
				final Usuario usuario = new Usuario();
				Request.executeMeRequestAsync(session,
						new Request.GraphUserCallback() {
							@Override
							public void onCompleted(GraphUser user,
									Response response) {
								if (user != null) {
									String mail = (String) user
											.getProperty("email");
									usuario.setMail(mail);
									// Comprobar si existe el usuario
									/*boolean existe = existeUsuario(usuario
											.getMail());
									*/
									boolean existe = Herramientas.rescatarUsuarioInternet(contexto, mail, "");
									if (!existe) {
										crearUsuario(usuario);
										// Guardamos el usuario en Herramientas
										Herramientas.setUsuarioLogado(usuario);
									}
									
									
									// Deshabilitar boton Login
									loginButton.setClickable(false);
									Herramientas.setLoginConFacebook(true);
									Herramientas.getUsuarioLogado().setMail(
											mail);
									conectar();
									//Retornamos atras
									FragmentManager fragmentManager = ((FragmentActivity) contexto).getSupportFragmentManager();
									fragmentManager.popBackStack();
								}
							}

							private void crearUsuario(Usuario usuario) {
								// Registar
								String resultJson = Herramientas
										.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=10&mail="
												+ usuario.getMail());

							}
						});

			} else if (state.isClosed()) {
				// Habilitar boton Login
				loginButton.setClickable(true);
				Herramientas.setLoginConFacebook(true);
				desconectar();
			}
		}

		private boolean existeUsuario(String mail) {
			String resultJson = Herramientas
					.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=11&usuario="
							+ mail);
			Toast.makeText(getActivity().getApplicationContext(), mail,
					Toast.LENGTH_SHORT).show();
			if (resultJson.length() == 3) {
				return false;
			}

			return true;
		}
	};;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Session.OpenRequest request = new Session.OpenRequest(getActivity());
		request.setPermissions(Arrays.asList("email"));
		request.setCallback(statusCallback);

		uiHelper = new UiLifecycleHelper(getActivity(), statusCallback);
		uiHelper.onCreate(savedInstanceState);

		iniciaVariables();

		// Si no estas logado
		if (!Herramientas.getLoginPass() && !Herramientas.getLoginConFacebook()) {
			// Activar
			loginButton.setClickable(true);
			loginFcBtn.setClickable(true);
		}
		// Si esta logado
		else {
			// Activar/Desactivar botones
			loginButton.setClickable(Herramientas.getLoginPass());
			if (Herramientas.getLoginPass()) {
				btnLogin.setText(getResources().getString(R.string.desconectar));
			}
			loginFcBtn.setClickable(Herramientas.getLoginConFacebook());
		}

	}

	private void iniciaVariables() {
		loginFcBtn = (LoginButton) getActivity().findViewById(
				R.id.fb_login_button);
		loginFcBtn.setFragment(this);
		loginFcBtn.setReadPermissions(PERMISSIONS);

		loginButton = (Button) getActivity().findViewById(R.id.buttonLogin);

		linkNuevoUsuario = (TextView) getActivity().findViewById(
				R.id.linkNuevoUsuario);
		linkNuevoUsuario.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Fragment fragment = new Fragment_nuevo_usuario();
				FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();

				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).addToBackStack(getActivity().getString(R.string.desNuevoUsuario)).commit();
			}
		});

		// Usuario y Password
		txtUsuario = (EditText) getActivity().findViewById(R.id.txtUsuario);
		txtPassword = (EditText) getActivity().findViewById(R.id.txtPassword);
		btnLogin = (Button) getActivity().findViewById(R.id.buttonLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Login();
			}

			private void Login() {
				if (((MainActivity) getActivity()).getConectado()) {
					btnLogin.setText(getResources()
							.getString(R.string.conectar));
					desconectar();
					// Habilitar boton facebook
					loginFcBtn.setClickable(true);
					Herramientas.setLoginPass(false);
				} else {
					LoginThread loginThread = new LoginThread(contexto);
					loginThread.execute();
				}
			}
		});
	}

	public static void conectar() {
		((MainActivity) contexto).setConectado(true);
		((MainActivity) contexto).actualzarLista();
		usdbh = new SQLiteHelper(contexto, "baseDeDatos", null, 1);
		db = usdbh.getWritableDatabase();
		usdbh.usuarioConectado(db);
		//Herramientas.rescatarUsuarioInternet(contexto);
		Herramientas.insertarUsuarioBaseDeDatos(contexto);

	}

	private void desconectar() {
		((MainActivity) getActivity()).setConectado(false);
		((MainActivity) getActivity()).actualzarLista();
		usdbh = new SQLiteHelper(this.getActivity(), "baseDeDatos", null, 1);

		db = usdbh.getWritableDatabase();
		usdbh.usuarioDesconectado(db);
	}

	@Override
	public void onResume() {
		super.onResume();
		// Si no estas logado
		if (!Herramientas.getLoginPass() && !Herramientas.getLoginConFacebook()) {
			// Activar
			loginButton.setClickable(true);
			loginFcBtn.setClickable(true);
		}
		// Si esta logado
		else {
			// Activar/Desactivar botones
			loginButton.setClickable(Herramientas.getLoginPass());
			if (Herramientas.getLoginPass()) {
				btnLogin.setText(getResources().getString(R.string.desconectar));
			}
			loginFcBtn.setClickable(Herramientas.getLoginConFacebook());
		}
		uiHelper.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onPause();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);
		uiHelper.onSaveInstanceState(savedState);
	
	}

	private static class LoginThread extends AsyncTask<Void, Void, Void> {

		private AlertDialog alert = null, alertNuevaTarjeta = null;
		private Builder ringProgressDialogMapa, insertarNuevaTarjeta;
		private Context contexto;
		private boolean exito;

		public LoginThread(Context contexto) {
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
			ocultarTeclado();
			ringProgressDialogMapa = new AlertDialog.Builder(contexto);
			ringProgressDialogMapa.setMessage(contexto.getResources()
					.getString(R.string.cargandoTxt));
			ringProgressDialogMapa.setCancelable(false);

			alert = ringProgressDialogMapa.create();

			alert.show();

		}

		private void ocultarTeclado() {
			InputMethodManager inputManager = (InputMethodManager) contexto
					.getSystemService(Context.INPUT_METHOD_SERVICE);

			// check if no view has focus:
			View view = ((Activity) contexto).getCurrentFocus();
			if (view != null) {
				inputManager.hideSoftInputFromWindow(view.getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}

		@Override
		protected Void doInBackground(Void... accion) {
			//exito = comprobarUsuarioPassword()
			String mail = txtUsuario.getText().toString();
			String pass = txtPassword.getText().toString();
			exito = Herramientas.rescatarUsuarioInternet(contexto, mail, pass);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			if (exito) {
				// Cambiar titulo al boton
				btnLogin.setText(contexto.getResources().getString(
						R.string.desconectar));
				// Se inserta el mail en el usuario en session
				Herramientas.getUsuarioLogado().setMail(
						txtUsuario.getText().toString());
				// Guardar en base de datos que ha conectado
				conectar();
				// Deshabilitar boton facebook
				loginFcBtn.setClickable(false);

				Herramientas.setLoginPass(true);
				
				//Retornamos atras
				FragmentManager fragmentManager = ((FragmentActivity) contexto).getSupportFragmentManager();
				fragmentManager.popBackStack();
			} else {
				Toast.makeText(contexto.getApplicationContext(),
						contexto.getResources().getString(R.string.falloLogin),
						Toast.LENGTH_SHORT).show();
			}
			alert.cancel();

		}

		/*private boolean comprobarUsuarioPassword() {

			String mail = txtUsuario.getText().toString();
			String password = txtPassword.getText().toString();
			String resultJson = Herramientas
					.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=0&usuario="
							+ mail + "&password=" + password);

			Usuario usuario = Herramientas.parsearUsuario(resultJson, contexto);
			if (usuario == null) {
				return false;
			}

			Herramientas.setUsuarioLogado(usuario);
			return true;
		}*/

	}

}