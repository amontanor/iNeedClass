package dotidapp.newineedclass;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

public class Fragment_datosProfesor extends Fragment {

	private static Context contexto;
	private View V;
	private String titulo;
	private static TextView textoNombre;
	private static TextView textoApellido1;
	private static TextView textoNombreCurso;
	private static TextView textoPrecio;
	private static TextView textoComentario;
	private static RadioButton radioHombre;
	private static RadioButton radioMujer;
	private static TextView textoFechaNacimiento;
	
	Fragment fragment = this;
	private TextView ofrezco;
	private TextView textoBbusco;
	private TextView datosAcademicos;
	private ImageView imageCall;
	private ImageView imageMail;
	private Profesor usuario;
	private Intent intent;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		contexto = container.getContext();
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");

		V = inflater.inflate(R.layout.fragment_datosprofesor, container, false);

		return V;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ofrezco = (TextView) getActivity().findViewById(R.id.ofrezco);
		textoBbusco = (TextView) getActivity().findViewById(R.id.textoBusco);
		datosAcademicos = (TextView) getActivity().findViewById(R.id.datosAcademicos);
		// Capturar objetos de la pantalla
		textoNombre = (TextView) getActivity().findViewById(R.id.textoNombre);
		textoApellido1 = (TextView) getActivity().findViewById(
				R.id.textoApellido1);
		textoFechaNacimiento = (TextView) getActivity().findViewById(
				R.id.textoFechaNacimiento);
		radioHombre = (RadioButton) getActivity().findViewById(R.id.radioHombre);
		radioHombre.setEnabled(false);
		radioMujer = (RadioButton) getActivity().findViewById(R.id.radioMujer);
		radioMujer.setEnabled(false);
		textoNombreCurso = (TextView) getActivity().findViewById(
				R.id.textoNombreCurso);
		textoPrecio = (TextView) getActivity().findViewById(
				R.id.textoPrecio);
		textoComentario = (TextView) getActivity().findViewById(
				R.id.textoComentario);
		imageCall = (ImageView) getActivity().findViewById(R.id.imageCall);
		imageMail = (ImageView) getActivity().findViewById(R.id.imageMail);
		
		imageCall.setOnClickListener(new View.OnClickListener(){
	
			public void onClick(View v) {
		    	intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + usuario.getTlf()));
		    	startActivity(intent);
		    }
		});
		
		imageMail.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	intent = new Intent(Intent.ACTION_VIEW);
		    	intent.setType("plain/text");
		    	intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
		    	intent.putExtra(Intent.EXTRA_EMAIL, new String[] { usuario.getMail() });
		    	startActivity(intent);
		    }
		});
		
		// Buscar datos
				usuario = Herramientas.getProfesor();
				rescatarDatos(usuario);
				AQuery aq = new AQuery(view);
		
		if (!Herramientas.getEsCurso()) {
			ofrezco.setVisibility(TextView.VISIBLE);
			textoPrecio.setVisibility(TextView.INVISIBLE);
			textoBbusco.setVisibility(TextView.VISIBLE);
			datosAcademicos.setText(getActivity().getString(R.string.cabeceraBusco));
			textoBbusco.setText(usuario.getOfrezco());
		}
		
		//Image Call and image mail
		if (usuario.getTlf().equals("null"))
		{
			imageCall.setVisibility(ImageView.INVISIBLE);
		}
		if (usuario.getMail().equals("null"))
		{
			imageMail.setVisibility(ImageView.INVISIBLE);
		}
		
		
		
		ImageOptions options = new ImageOptions();
		options.round = 1000;
		options.memCache = true;
		options.fileCache = true;
		options.fallback = R.drawable.contact;
		
		String urlImagen = contexto.getResources().getString(R.string.urlImages)
				+ usuario.getMail() + ".jpg";

		// invoke it with an image view
		aq.id(R.id.imageFotoProfesor).image(urlImagen,options);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.perfil, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	private void rescatarDatos(Profesor profesor) {

		// Se usan los datos del usuario en session
		textoNombre.setText(profesor.getNombre().replace("%20", " ").replace("%30", "n"));
		textoApellido1.setText(profesor.getApellido1().replace("%20", " ").replace("%30", "n"));
		if (profesor.getSexo().equals("H"))
		{
			radioHombre.setChecked(true);
			radioMujer.setChecked(false);
		}
		else
		{
			radioHombre.setChecked(false);
			radioMujer.setChecked(true);
		}
		textoFechaNacimiento.setText(profesor.getFnac());
		textoNombreCurso.setText(profesor.getNombreAsignatura().replace("%20", " ").replace("%30", "n"));
		textoPrecio.setText(profesor.getPrecio()+" €");
		textoComentario.setText(profesor.getComentario().replace("%20", " ").replace("%30", "n"));
	}

}