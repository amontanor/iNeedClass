package dotidapp.newineedclass;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Fragment_inicio extends Fragment {

	private ShapeDrawable miImagen;
	private Context contexto;
	private View V;
	private String titulo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		contexto = container.getContext();
		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");

		V = inflater.inflate(R.layout.fragment_inicio, container, false);

		TextView iNeedClass = (TextView) V.findViewById(R.id.txtIneedClass);
		TextView clases = (TextView) V.findViewById(R.id.txtClases);
		TextView intercambio = (TextView) V.findViewById(R.id.txtIntercambio);

		iNeedClass.setTypeface(font);
		clases.setTypeface(font);
		intercambio.setTypeface(font);

		// Mostrar rectangulo azul

		Animation animationRectangular = AnimationUtils.loadAnimation(contexto,
				R.anim.rectanguloinicio);
		ImageView rectangulo = (ImageView) V.findViewById(R.id.rectangulo);
		rectangulo.startAnimation(animationRectangular);

		return V;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ImageView imagen1 = (ImageView) getActivity().findViewById(
				R.id.imageInicio2);
		ImageView imagen2 = (ImageView) getActivity().findViewById(
				R.id.imageInicio3);
		ImageView imagen3 = (ImageView) getActivity().findViewById(
				R.id.imageInicio4);

		imagen1.setOnClickListener(new View.OnClickListener() {
			private Fragment_tutorial fragment = new Fragment_tutorial();
			public void onClick(View v) {
				// Mostrar circulo azul
				Animation animationCirculo = AnimationUtils.loadAnimation(
						contexto, R.anim.circulosinicio);
				ImageView circulo = (ImageView) V.findViewById(R.id.circulo1);
				circulo.setVisibility(ImageView.VISIBLE);
				circulo.startAnimation(animationCirculo);
				
				titulo = getResources().getString(R.string.desTutorial);
				
				((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(titulo);
				
				final FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				
				new Thread(new Runnable() {
				    public void run() {
				        try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right,
								R.anim.slide_out_left, 0, 0)
						.replace(R.id.content_frame, fragment).addToBackStack(getActivity().getString(R.string.desHogar)).commit();
				    }
				}).start();
			}
		});

		imagen2.setOnClickListener(new View.OnClickListener() {
			private Fragment_buscadorProfesor fragment = new Fragment_buscadorProfesor();

			public void onClick(View v) {
				// Mostrar circulo azul
				Animation animationCirculo = AnimationUtils.loadAnimation(
						contexto, R.anim.circulosinicio);
				ImageView circulo = (ImageView) V.findViewById(R.id.circulo2);
				circulo.setVisibility(ImageView.VISIBLE);
				circulo.startAnimation(animationCirculo);

				final FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				
				new Thread(new Runnable() {
				    public void run() {
				        try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        Herramientas.setEsCurso(true);
				        Bundle bundle = new Bundle();
				        bundle.putInt("modo", 1);
				        fragment.setArguments(bundle);
				        
				        fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right,
								R.anim.slide_out_left, 0, 0)
						.replace(R.id.content_frame, fragment).addToBackStack(getActivity().getString(R.string.desHogar)).commit();
				    }
				}).start();
				
			}
		});

		imagen3.setOnClickListener(new View.OnClickListener() {
			private Fragment_buscadorProfesor fragment = new Fragment_buscadorProfesor();
			public void onClick(View v) {
				// Mostrar circulo azul
				Animation animationCirculo = AnimationUtils.loadAnimation(
						contexto, R.anim.circulosinicio);
				ImageView circulo = (ImageView) V.findViewById(R.id.circulo3);
				circulo.setVisibility(ImageView.VISIBLE);
				circulo.startAnimation(animationCirculo);
				
				final FragmentManager fragmentManager = getActivity()
						.getSupportFragmentManager();
				
				new Thread(new Runnable() {
				    public void run() {
				        try {
							Thread.sleep(800);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				        Herramientas.setEsCurso(false);
				        Bundle bundle = new Bundle();
				        bundle.putInt("modo", 2);
				        fragment.setArguments(bundle);
				        
				        fragmentManager
						.beginTransaction()
						.setCustomAnimations(R.anim.slide_in_right,
								R.anim.slide_out_left, 0, 0)
						.replace(R.id.content_frame, fragment).addToBackStack(getActivity().getString(R.string.desHogar)).commit();
				    }
				}).start();
				
			}
		});
	}

}