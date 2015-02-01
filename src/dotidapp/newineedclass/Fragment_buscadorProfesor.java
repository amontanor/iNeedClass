package dotidapp.newineedclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class Fragment_buscadorProfesor extends Fragment {

	private Context contexto;
	private View V;
	private EditText textoBuscar;
	private AdaptadorProfesores adaptadorProfesores;
	private ListView listProfesores;
	private Fragment_datosProfesor fragmentDatosrProfesor;
	private int modo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// El contexto sera usado para todos los metodos
		contexto = container.getContext();

		// Cambiar el menu del action bar
		setHasOptionsMenu(true);

		// Estilo de la fuente
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");
		V = inflater.inflate(R.layout.fragment_buscador_profesor, container,
				false);
		
		Bundle bundle = this.getArguments();
		modo = bundle.getInt("modo");

		return V;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		final FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();

		textoBuscar = (EditText) getActivity().findViewById(R.id.textoBuscar);
		listProfesores = (ListView) getActivity().findViewById(
				R.id.listaProfesores);
		listProfesores.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				fragmentDatosrProfesor = new Fragment_datosProfesor();
				Herramientas.setProfesor(Herramientas.getListaProfesores().get(
						position));
				final FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right,
								R.anim.slide_out_left, 0, 0)
						.replace(R.id.content_frame, fragmentDatosrProfesor)
						.addToBackStack(
								getActivity().getString(R.string.desHogar))
						.commit();
			}

		});

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.buscar, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.optionSearch:
			buscarProfesores();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void buscarProfesores() {
		final String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=19&texto="
						+ textoBuscar.getText());

		adaptadorProfesores = new AdaptadorProfesores(getActivity(),
				Herramientas.getListaProfesores());
		
		new Thread(new Runnable() {
		    public void run() {	 
		    getActivity().runOnUiThread(new Runnable() {
		        public void run() {
		        	Herramientas.parsearProfesores(resultJson, getActivity(), contexto, adaptadorProfesores, true, getActivity(), listProfesores, modo);
		        }
		    });
		    }
		}).start();		

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onSaveInstanceState(Bundle savedState) {
		super.onSaveInstanceState(savedState);

	}

}