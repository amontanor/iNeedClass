package dotidapp.newineedclass;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageSwipeAdapter extends PagerAdapter {
	// ---the images to display---
	Integer[] Images = { R.drawable.dibujo_listview, R.drawable.fondo,
			R.drawable.ic_launcher };

	Context context;
	View view;

	ImageSwipeAdapter(Context context, View view) {
		this.context = context;
		this.view = view;
	}

	@Override
	public int getCount() {
		return Images.length;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((ImageView) object);
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		ImageView imageView = new ImageView(context);
		int pad = 4000;
		//imageView.setPadding(0, pad, pad, pad);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setImageResource(Images[position]);
		((ViewPager) container).addView(imageView, 0);
		return imageView;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((ImageView) object);
	}
}
