package dotidapp.newineedclass;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class Fragment_buscadorClase extends Fragment {

	private Context contexto;
	private View V;
	private ArrayAdapter<String> adaptadorProvincia;
	private ArrayAdapter<String> adaptadorPoblacion;
	private ArrayAdapter<String> adaptadorMateria;
	private ArrayAdapter<String> adaptadorCategoria;
	private Spinner spinnerProvincia;
	private Spinner spinnerPoblacion;
	private Spinner spinnerMateria;
	private Spinner spinnerCategoria;
	private static ArrayList<Poblacion> listaPoblacion;
	private Button botonSiguiente;
	private int codPoblacion = -1, codCategoria = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// El contexto sera usado para todos los metodos
		contexto = container.getContext();

		// Estilo de la fuente
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");
		V = inflater
				.inflate(R.layout.fragment_buscador_clase, container, false);

		return V;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		// Capturar desplegables
		spinnerProvincia = (Spinner) getActivity().findViewById(
				R.id.spinnerProvincia);
		spinnerPoblacion = (Spinner) getActivity().findViewById(
				R.id.spinnerPoblacion);
		spinnerMateria = (Spinner) getActivity().findViewById(
				R.id.spinnerMateria);
		spinnerCategoria = (Spinner) getActivity().findViewById(
				R.id.spinnerCategoria);
		
		spinnerProvincia.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v, int pos,
					long id) {
					// Poblacion
					int idProvincia = Herramientas.getListaProvincia().get(pos)
							.getIdProvincia();
					// Capturar listaPoblacionesy
					actualizarPoblacion(idProvincia);
					adaptadorPoblacion.notifyDataSetChanged();
				}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}

		});
		
		spinnerMateria.setOnItemSelectedListener(new OnItemSelectedListener() {

			public void onItemSelected(AdapterView<?> parent, View v, int pos,
					long id) {

					// Capturar listaCategoria. El indice de la materia corresponde con el indice en el spinner
				rellenarSpinnerCategoria(String.valueOf(pos+1));
					adaptadorPoblacion.notifyDataSetChanged();
				}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}

		});
		
		final FragmentManager fragmentManager = getActivity()
				.getSupportFragmentManager();
		
		botonSiguiente = (Button) getActivity().findViewById(R.id.buttonSiguiente);
		botonSiguiente.setOnClickListener(new View.OnClickListener() {
			// @Override
			public void onClick(View v) {
				Fragment_listarProfesores fragment = new Fragment_listarProfesores();
				Bundle bundle = new Bundle();
				codPoblacion = buscarCodPoblacion();
				codCategoria = buscarCodCategoria();
				bundle.putInt("codPoblacion",codPoblacion );
				bundle.putInt("codCategoria", codCategoria);
				fragment.setArguments(bundle);
				 fragmentManager
					.beginTransaction()
					.setCustomAnimations(R.anim.slide_in_right,
							R.anim.slide_out_left, 0, 0)
					.replace(R.id.content_frame, fragment).addToBackStack(getActivity().getString(R.string.desListarProfesores)).commit();
			}

			private int buscarCodCategoria() {
				int posSpinnerCategoria = adaptadorCategoria
						.getPosition(spinnerCategoria.getSelectedItem().toString());

				return Integer.parseInt(Herramientas.getListaCategoria().get(posSpinnerCategoria).getId());
			}

			private int buscarCodPoblacion() {
				int posSpinnerPoblacion = adaptadorPoblacion
						.getPosition(spinnerPoblacion.getSelectedItem().toString());

				return listaPoblacion.get(posSpinnerPoblacion).getIdPoblacion();
			}
		});


		Herramientas.rescatarPoblacionProvincia(contexto);

		rellenarSpinners();
		
		

	}

	private void rellenarSpinners() {
		rellenarSpinnerProvincia();
		rellenarSpinnerPoblacion();
		rellenarSpinnerMateria();
		rellenarSpinnerCategoria("1");

	}

	private void rellenarSpinnerCategoria(String id) {
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=17&idmateria="+id);

		Herramientas.parsearCategoria(resultJson, getActivity());

		adaptadorCategoria = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item,
				Herramientas.getListaCategoriaString());
		spinnerCategoria.setAdapter(adaptadorCategoria);
		adaptadorCategoria.notifyDataSetChanged();
		
		spinnerCategoria.setSelection(0);
		
	}

	private void rellenarSpinnerMateria() {
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=16");

		Herramientas.parsearMateria(resultJson, getActivity());

		adaptadorMateria = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item,
				Herramientas.getListaMateriaString());
		spinnerMateria.setAdapter(adaptadorMateria);
		adaptadorMateria.notifyDataSetChanged();
		
		spinnerMateria.setSelection(0);
		
	}

	private void actualizarPoblacion(int idPoblacion) {
		String resultJson = Herramientas
				.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=15&idprovincia="
						+ idPoblacion);

		if (adaptadorPoblacion != null) {
			adaptadorPoblacion.clear();
		}

		listaPoblacion = (ArrayList<Poblacion>) Herramientas.parsearPoblacion(
				resultJson, getActivity());
		spinnerPoblacion.setSelection(0);

	}

	private void rellenarSpinnerPoblacion() {
		// Descargar Poblacion
		listaPoblacion = new ArrayList<Poblacion>();

		// Mostrar la provincia del usuario
		int posSpinnerProvincia = adaptadorProvincia.getPosition(Herramientas
				.getPoblacionProvinciaUsuario()[1]);
		spinnerProvincia.setSelection(posSpinnerProvincia);

		// Buscar el id de la provincia
		int idProvincia = Herramientas.getListaProvincia()
				.get(posSpinnerProvincia).getIdProvincia();

		// Capturar listaPoblaciones
		actualizarPoblacion(idProvincia);

		adaptadorPoblacion = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item,
				Herramientas.getListaPoblacionString());
		spinnerPoblacion.setAdapter(adaptadorPoblacion);
		adaptadorPoblacion.notifyDataSetChanged();

	}

	private void rellenarSpinnerProvincia() {

		// Descargar Provincias
		Herramientas.descargarListaProvincia(getActivity());

		adaptadorProvincia = new ArrayAdapter(getActivity(),
				android.R.layout.simple_spinner_item,
				Herramientas.getListaProvinciaString());
		spinnerProvincia.setAdapter(adaptadorProvincia);
		adaptadorProvincia.notifyDataSetChanged();

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