package dotidapp.newineedclass;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class Fragment_listarProfesores extends ListFragment {



	private Context contexto;
	private Activity activity;
	private View V;
	private int codPoblacion;
	private int codCategoria;
	private List<Profesor> listaProfesores = new ArrayList<Profesor>();
	private AdaptadorProfesores adaptadorProfesores;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		contexto = container.getContext();
		activity = (Activity) container.getContext();
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");


		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    codPoblacion = getArguments().getInt("codPoblacion", 0);
		codCategoria = getArguments().getInt("codCategoria", 0);

		consultarProfesores();
		adaptadorProfesores = new AdaptadorProfesores(activity, listaProfesores);
		setListAdapter(adaptadorProfesores);
	  }


	private void consultarProfesores() {
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=18&idpoblacion="
						+ codPoblacion + "&idcategoria=" + codCategoria);
		listaProfesores = parsearProfesores(resultJson);

		incorporarAdaptados();

	}

	private void incorporarAdaptados() {
		adaptadorProfesores = new AdaptadorProfesores(activity, listaProfesores);
	}

	private List<Profesor> parsearProfesores(String resultJson) {
		List<Profesor> listaProfesores = new ArrayList<Profesor>();
		try {
			JSONArray jArray = new JSONArray(resultJson);
			for (int i = 0; i < jArray.length(); i++) {
				Profesor profesor = new Profesor();
				profesor.setPrecio(jArray.getJSONObject(0).getString("precio"));
				profesor.setNombre(jArray.getJSONObject(0).getString("nombre"));
				profesor.setTlf(jArray.getJSONObject(0).getString("telefono"));
				profesor.setMail(jArray.getJSONObject(0).getString("mail"));
				profesor.setSexo(jArray.getJSONObject(0).getString("mail"));
				profesor.setFnac(jArray.getJSONObject(0).getString("fnac"));
				//profesor.setNivel(jArray.getJSONObject(0).getString("nivel"));
				//profesor.setComentario(jArray.getJSONObject(0).getString(
					//	"comentario"));
				listaProfesores.add(profesor);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return listaProfesores;
	}
}