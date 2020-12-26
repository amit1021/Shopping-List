//package com.example.shoplist;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import org.w3c.dom.Text;
//
//import java.util.ArrayList;
//
//public class ItemListAdapter extends ArrayAdapter<Item> {
//
//    private static final String TAG = "ItemListAdapter";
//
//    private Context mContext;
//    int mResource;
//
//    public ItemListAdapter(@NonNull Context context, int resource, ArrayList<Item> objects) {
//        super(context, resource);
//        mContext = context;
//        mResource = resource;
//    }
//
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        //get the item information
//        String name = getItem(position).getName();
//        int quantity = getItem(position).getQuantity();
//
//        Item item = new Item(name,quantity);
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        convertView = inflater.inflate(mResource, parent, false);
//
//        TextView t6 = (TextView) convertView.findViewById(R.id.textView6);
//        TextView t7 = (TextView) convertView.findViewById(R.id.textView7);
//
//        t6.setText(name);
//        t7.setText(quantity);
//
//        return convertView;
//    }
//}
