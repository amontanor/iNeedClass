package dotidapp.newineedclass;

import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorRows extends ArrayAdapter {
	Activity context;
	List<row> rows;

	AdaptadorRows(Activity context, List<row> rows) {
		super(context, R.layout.itemrow);
		this.context = context;
		this.rows = rows;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
	    Typeface font = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Bold.ttf");
		
		LayoutInflater inflater = context.getLayoutInflater();
		View item = inflater.inflate(R.layout.itemrow, null);

		ImageView icono = (ImageView) item.findViewById(R.id.icono);
		icono.setImageDrawable(rows.get(position).getIcono());

		TextView lblTitulo = (TextView) item.findViewById(R.id.titulo);
		lblTitulo.setText(rows.get(position).getTitulo());
		
		lblTitulo.setTypeface(font);

		return (item);
	}
}
