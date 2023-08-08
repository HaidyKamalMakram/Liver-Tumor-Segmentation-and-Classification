package app.ij.mlwithtensorflowlite;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<UserModel> userModelArrayList;
    private int custom_list_layout;

    public CustomAdapter(Context context, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
    }

    public CustomAdapter(GetSavedDataActivity context, int custom_list_layout, ArrayList<UserModel> userModelArrayList) {
        this.context = context;
        this.userModelArrayList = userModelArrayList;
        this.custom_list_layout = custom_list_layout;
    }

    @Override
    public int getCount() {
        return userModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            convertView = inflater.inflate(R.layout.lv_item, null, true);

            holder.tvdate = (TextView) convertView.findViewById(R.id.date);
            holder.tvcity = (TextView) convertView.findViewById(R.id.city);
            holder.petImageView = (ImageView) convertView.findViewById(R.id.loadedImg);

            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvdate.setText("Date: " + userModelArrayList.get(position).getDate());
        holder.tvcity.setText("Stage: " + userModelArrayList.get(position).getClassImg());


        byte[] recordImage = userModelArrayList.get(position).getImage();
        Bitmap bitmap = BitmapFactory.decodeByteArray(recordImage, 0, recordImage.length);
        holder.petImageView.setImageBitmap(bitmap);


        return convertView;
    }

    private class ViewHolder {

        protected TextView tvcity, tvdate;
        protected ImageView petImageView;

    }

}
