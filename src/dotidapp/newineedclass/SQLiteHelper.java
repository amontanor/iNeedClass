package dotidapp.newineedclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

public class SQLiteHelper extends SQLiteOpenHelper {

	// Sentencia SQL para crear la tabla de Configuracion
	String sqlCreateConfiguracion = "create table if not exists Configuracion (id TEXT, nombre TEXT, valor TEXT)";
	// Sentencia SQL para crear la tabla de Usuario
	String sqlCreateUsuario = "create table if not exists Usuario (nombre TEXT, apellido1 TEXT,password TEXT,mail TEXT,tlf TEXT,fnac TEXT,estudios TEXT,sexo TEXT,notificarMail TEXT,id_tipo TEXT, imagen blob)";
	// Sentencia SQL para crear la tabla Poblacion
	String sqlCreatePoblacion = "create table if not exists Poblacion (idPoblacion TEXT, poblacion TEXT)";
	// Sentencia SQL para crear la tabla Provincia
	String sqlCreateProvincia = "create table if not exists Provincia (idProvincia TEXT, provincia TEXT, idpoblacion TEXT)";

	Bitmap imagen;
	// Sentencia SQL para insertar el registro de login
	String sqlInsertLoginConfiguracion = "insert into Configuracion values ('1','login','n');";

	// Sentencia SQL para borrar base de datos
	String sqlBorrarBBDD = "drop database 'baseDeDatos'";

	// Sentencia SQL para conocer que el usuario esta conectado
	String mail = "n";

	// Sentencia SQL para conocer que el usuario esta desconectado
	String sqlUsuarioDesconectado = "update Configuracion set valor = 'n' where nombre = 'login'";
	
	// Sentencia SQL para vacias tablas Poblacion
	String sqlVaciasPoblacion = "delete from Poblacion where 1";
	
	// Sentencia SQL para vacias tablas Provincia
	String sqlVaciasProvincia = "delete from Provincia where 1";

	public SQLiteHelper(Context contexto, String nombre, CursorFactory factory,
			int version) {
		super(contexto, nombre, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// Se ejecuta la sentencia SQL de creación de la tabla
		db.execSQL(sqlCreateConfiguracion);
		db.execSQL(sqlInsertLoginConfiguracion);
		db.execSQL(sqlCreateUsuario);
		db.execSQL(sqlCreatePoblacion);
		db.execSQL(sqlCreateProvincia);
	}
	
	public void insertarPoblacion(SQLiteDatabase db,Poblacion poblacion) {
		db.execSQL("insert into Poblacion values ('"+poblacion.getIdPoblacion()+"','"+poblacion.getPoblacion()+"');");
	}
	
	public void insertarProvincia(SQLiteDatabase db, Provincia provincia) {
		db.execSQL("insert into Provincia values ('"+provincia.getIdProvincia()+"','"+provincia.getProvincia()+"','"+provincia.getIdProvincia()+"');");	
	}

	public void borrarBBDD(SQLiteDatabase db) {
		// Se ejecuta la sentencia para borrar bbdd
		db.execSQL(sqlBorrarBBDD);
	}
	
	public void vaciasPoblacionProvincia(SQLiteDatabase db) {
		db.execSQL(sqlVaciasPoblacion);
		db.execSQL(sqlVaciasProvincia);
	}

	public void usuarioConectado(SQLiteDatabase db) {
		// Se ejecuta la sentencia para c
		mail = Herramientas.getUsuarioLogado().getMail();
		db.execSQL("update Configuracion set valor =' "+mail+"' where nombre = 'login'");
	}

	public void usuarioDesconectado(SQLiteDatabase db) {
		mail = "n";
		db.execSQL(sqlUsuarioDesconectado);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int versionAnterior,
			int versionNueva) {

		// Se elimina la versión anterior de la tabla
		db.execSQL("DROP TABLE IF EXISTS Configuracion");

		// Se crea la nueva versión de la tabla
		// db.execSQL(sqlCreate);
	}
}