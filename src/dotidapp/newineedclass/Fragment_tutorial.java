package dotidapp.newineedclass;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class Fragment_tutorial extends Fragment {

	private ShapeDrawable miImagen;
	private Context contexto;
	private View V;
	ImageView image;
	private ViewFlipper viewFlipper;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		contexto = container.getContext();
		V = inflater.inflate(R.layout.fragment_tutorial, container, false);

		return V;
	}

	private void setFlipperImage(int res) {
		ImageView image = new ImageView(getActivity().getApplicationContext());
		image.setBackgroundResource(res);
		viewFlipper.addView(image);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.view_pager);
		ImageSwipeAdapter adapter = new ImageSwipeAdapter(getActivity(), view);
		viewPager.setAdapter(adapter);

	}

}