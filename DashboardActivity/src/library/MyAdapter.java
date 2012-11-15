package library;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter{

	private String[] imageUrls;
	private String[] userIDs;
	
	private class ViewHolder{
		public TextView text;
		public ImageView image;
	}
	
	
	public int getCount() {
		return imageUrls.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}

}
