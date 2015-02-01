package dotidapp.newineedclass;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_nuevo_usuario extends Fragment {

	private Context contexto;
	private View V;
	private Button botonRegistro;
	private TextView txtMail, txtPass, txtRePass;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		contexto = container.getContext();

		Typeface font = Typeface.createFromAsset(contexto.getAssets(),
				"fonts/Montserrat-Bold.ttf");

		V = inflater.inflate(R.layout.fragment_nuevo_usuario, container, false);
		return V;
	}


	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		txtMail = (TextView) getActivity().findViewById(R.id.txtUsuario);
		txtPass = (TextView) getActivity().findViewById(R.id.txtPassword);
		txtRePass = (TextView) getActivity().findViewById(R.id.txtRePassword);
		
		botonRegistro = (Button) getActivity().findViewById(R.id.buttonRegistro);
		botonRegistro.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String mail,pass,repass;
				
				mail = txtMail.getText().toString();
				pass = txtPass.getText().toString();
				repass = txtRePass.getText().toString();
				
				int res = registro(mail,pass,repass);
				
				switch (res) {
				case 0:
					Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.registroCorrecto),
							Toast.LENGTH_SHORT).show();
					//Retornamos atras
					//Retornamos atras
					FragmentManager fragmentManager = ((FragmentActivity) contexto).getSupportFragmentManager();
					fragmentManager.popBackStack();
					break;
				case -1:
					Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.registroError),
							Toast.LENGTH_SHORT).show();
					break;
				case -2:
					Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.registroPasswordIncorrecto),
							Toast.LENGTH_SHORT).show();
					break;
				case -3:
					Toast.makeText(getActivity().getApplicationContext(), getActivity().getString(R.string.registroDuplicado),
							Toast.LENGTH_SHORT).show();
					break;
				}
			}

			private int registro(String mail, String pass, String repass) {
				
				if (!pass.equals(repass))
				{
					return -2;
				}
				
				//Comprobar si ya existe el usuario
				String resultJson = Herramientas
						.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=11&usuario="
								+ mail);
				if (resultJson.length()!=3)
				{
					return -3;
				}
				
				//Registar
				resultJson = Herramientas
						.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=10&mail="
								+ mail + "&password=" + pass);
				//Comprobar si se ha creado
				resultJson = Herramientas
						.jsonLoad("http://s425938729.mialojamiento.es/webs/inc/consultaAndroid.php?opcion=0&usuario="
								+ mail + "&password=" + pass);

				Usuario usuario = Herramientas.parsearUsuario(resultJson,getActivity());
				if (usuario == null) {
					return -1;
				}
				return 0;
				
			}
		
			});
	}
}