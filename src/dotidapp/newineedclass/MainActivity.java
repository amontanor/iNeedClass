package dotidapp.newineedclass;

import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.facebook.Session;

public class MainActivity extends ActionBarActivity {

	private DrawerLayout drawerLayout;
	private ListView drawerList;
	private ActionBarDrawerToggle drawerToggle;
	private AdaptadorRows AdaptadorRows;
	private static List<row> listarowsLogIn = new ArrayList<>();
	private static List<row> listarowsLogOut = new ArrayList<>();
	private List<String> listaDescripcionesLogin = new ArrayList<String>();
	private List<String> listaDescripcionesLogOut = new ArrayList<String>();
	private String titulo;
	private String usuario = "";
	private static Boolean conectado = true;
	private dotidapp.newineedclass.SQLiteHelper usdbh;
	private SQLiteDatabase db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Titulo inicial
		titulo = "Inicio";

		// Cambiar color a la barra
		android.support.v7.app.ActionBar bar = getSupportActionBar();
		bar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(
				R.color.cabecera)));

		// Cargamos el fragment inicial
		Fragment fragment = new Fragment_inicio();
		FragmentManager fragmentManager = getSupportFragmentManager();

		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, fragment).commit();

		// Comprobamos la BBDD

		creamosBBDD();
		conectado = comprobarLogin();

		// Inicializar listarows

		listaDescripcionesLogin
				.add(getResources().getString(R.string.desHogar));
		listaDescripcionesLogin.add(getResources().getString(
				R.string.desAsignatura));
		listaDescripcionesLogin.add(getResources().getString(
				R.string.desIntercambio));
		listaDescripcionesLogin.add(getResources()
				.getString(R.string.desPerfil));
		listaDescripcionesLogin
				.add(getResources().getString(R.string.desLogin));

		listaDescripcionesLogOut.add(getResources()
				.getString(R.string.desHogar));
		listaDescripcionesLogOut.add(getResources()
				.getString(R.string.desLogin));

		if (listarowsLogIn.size() == 0) {

			// Insertamos imagen grande
			listarowsLogIn.add(new row(getResources().getDrawable(
					R.drawable.dibujo_listview), "", 0));

			// Insertamos los registros
			for (int i = 1; i <= listaDescripcionesLogin.size(); i++) {
				listarowsLogIn.add(new row(getResources().getDrawable(
						R.drawable.ic_launcher), listaDescripcionesLogin
						.get(i - 1), i));

			}
		}

		if (listarowsLogOut.size() == 0) {

			// Insertamos imagen grande
			listarowsLogOut.add(new row(getResources().getDrawable(
					R.drawable.dibujo_listview), "", 0));

			// Insertamos los registros
			for (int i = 1; i <= listaDescripcionesLogOut.size(); i++) {
				listarowsLogOut.add(new row(getResources().getDrawable(
						R.drawable.ic_launcher), listaDescripcionesLogOut
						.get(i - 1), i));

			}
		}

		// Insertamos la lista correcta
		if (conectado) {
			AdaptadorRows = new AdaptadorRows(this, listarowsLogIn);
			AdaptadorRows.addAll(listarowsLogIn);

		} else {
			AdaptadorRows = new AdaptadorRows(this, listarowsLogOut);
			AdaptadorRows.addAll(listarowsLogOut);
		}

		drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawerList = (ListView) findViewById(R.id.left_drawer);

		drawerList.setAdapter(AdaptadorRows);

		drawerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView parent, View view,
					int position, long id) {

				Fragment fragment = null;
				FragmentManager fragmentManager = getSupportFragmentManager();

				switch (position) {
				case 1:
					fragment = new Fragment_inicio();
					titulo = getResources().getString(R.string.desHogar);
					break;
				case 2:
					if (conectado) {
						fragment = new Fragment_perfil();
						titulo = getResources().getString(
								R.string.desAsignatura);
					} else {
						fragment = new Fragment_login();
						titulo = getResources().getString(R.string.desLogin);
					}
					break;
				case 3:
					if (conectado) {
						fragment = new Fragment_inicio();
						titulo = getResources().getString(
								R.string.desIntercambio);
					}
					break;
				case 4:
					fragment = new Fragment_perfil();
					titulo = getResources().getString(R.string.desPerfil);

					break;
				case 5:
					fragment = new Fragment_login();
					titulo = getResources().getString(R.string.desLogin);
					break;
				default:
					break;
				}

				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).addToBackStack(titulo).commit();
				drawerLayout.closeDrawer(drawerList);

			}
		});

		drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
				R.drawable.ic_launcher, R.string.drawer_open,
				R.string.drawer_close) {

			public void onDrawerClosed(View view) {
				getSupportActionBar().setTitle(titulo);
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}

			public void onDrawerOpened(View drawerView) {
				getSupportActionBar().setTitle("iNeedClass");
				ActivityCompat.invalidateOptionsMenu(MainActivity.this);
			}
		};

		drawerLayout.setDrawerListener(drawerToggle);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

	}

	private Boolean comprobarLogin() {
		Boolean res = comprobarLoginUsuario() || comprobarLoginFacebook();
		if (res) {
			capturarDatosDelUsuario();
		}
		return res;
	}

	private void capturarDatosDelUsuario() {
		recuperarDatosASession();
		//recuperarProvincias();
		//Herramientas.rescatarPoblacionProvincia(this);
	}

	private void recuperarProvincias() {
		Usuario usuario = new Usuario();
		Cursor c = db.rawQuery("SELECT * from Provincia;", null);
		// Nos aseguramos de que existe al menos un registro
		Herramientas.setListaProvincia(new ArrayList<Provincia>());
		Herramientas.setListaProvinciaString(new ArrayList<String>());
		if (c.moveToFirst()) {
			while (c.moveToNext()) {
					Herramientas.getListaProvinciaString().add(c.getString(1));
					Herramientas.getListaProvincia().add(new Provincia(c.getString(0),c.getString(1)));
			}
		}

	}

	private void recuperarDatosASession() {
		Usuario usuario = new Usuario();
		Cursor c = db.rawQuery("SELECT * from Usuario;", null);
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			Herramientas.getUsuarioLogado().setNombre(c.getString(0));
			Herramientas.getUsuarioLogado().setApellido1(c.getString(1));
			Herramientas.getUsuarioLogado().setApellido2(c.getString(2));
			Herramientas.getUsuarioLogado().setMail(c.getString(3));
			Herramientas.getUsuarioLogado().setTlf(c.getString(4));
			Herramientas.getUsuarioLogado().setFnac(c.getString(5));
			Herramientas.getUsuarioLogado().setEstudios(c.getString(6));
			Herramientas.getUsuarioLogado().setNacionalidad(c.getString(7));
			Herramientas.getUsuarioLogado().setId_poblacion(c.getString(8));
			//Capturamos la imagen
			try{
			byte[] byteImage2 = c.getBlob(c.getColumnIndex("imagen"));
			Bitmap imagenBitMap = BitmapFactory.decodeByteArray(byteImage2, 0,
	                byteImage2.length);
			Herramientas.getUsuarioLogado().setImagen(imagenBitMap);
			}
			catch (Exception e){
				
			}
			finally
			{
				
			}
		}
	}

	private boolean comprobarLoginUsuario() {
		Cursor c = db.rawQuery(
				"SELECT valor from Configuracion where nombre='login';", null);
		// Nos aseguramos de que existe al menos un registro
		if (c.moveToFirst()) {
			String mail = c.getString(0);
			if (!mail.equals("n")) {
				Herramientas.setUsuarioLogado(new Usuario());
				Herramientas.getUsuarioLogado().setMail(mail);
				Herramientas.setLoginPass(true);
				return true;
			}
		}
		return false;
	}

	private boolean comprobarLoginFacebook() {

		boolean result = Session.openActiveSessionFromCache(this) != null
				&& Session.openActiveSessionFromCache(this).isOpened();
		if (result) {
			Herramientas.setLoginConFacebook(true);
		}
		return result;
	}

	private void creamosBBDD() {
		// Abrimos la base de datos en modo lectura
		usdbh = new SQLiteHelper(this, "baseDeDatos", null, 1);
		db = usdbh.getWritableDatabase();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		drawerToggle.syncState();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		}
		if (drawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return false;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Boolean getConectado() {
		return conectado;
	}

	public void setConectado(Boolean con) {
		conectado = con;
	}

	public void actualzarLista() {
		// Insertamos la lista correcta
		if (conectado) {
			AdaptadorRows = new AdaptadorRows(this, listarowsLogIn);
			runOnUiThread(new Runnable() {
				public void run() {
					AdaptadorRows.addAll(listarowsLogIn);
					drawerList.setAdapter(AdaptadorRows);
					AdaptadorRows.notifyDataSetChanged();
				}
			});
		} else {
			AdaptadorRows = new AdaptadorRows(this, listarowsLogOut);
			runOnUiThread(new Runnable() {
				public void run() {
					AdaptadorRows.addAll(listarowsLogOut);
					drawerList.setAdapter(AdaptadorRows);
					AdaptadorRows.notifyDataSetChanged();
				}
			});
		}

	}
	
	@Override
	public void onBackPressed(){
	    	super.onBackPressed();
	}
}
