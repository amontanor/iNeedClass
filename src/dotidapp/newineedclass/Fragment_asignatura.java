package dotidapp.newineedclass;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 
public class Fragment_asignatura extends Fragment {
 
    private ShapeDrawable miImagen;
    private Context contexto;

	@Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
 
		contexto = container.getContext();

		View V = inflater.inflate(R.layout.fragment_asignatura, container, false);
		
        return V;
    }

	
}