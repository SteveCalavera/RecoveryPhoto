package com.FutureStack.RecoveryPhoto.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.FutureStack.RecoveryPhoto.Pojo.ImageData;
import com.FutureStack.RecoveryPhoto.R;

import java.util.ArrayList;

import static com.FutureStack.RecoveryPhoto.Activities.ScannerActivity.toolbar;


public class AdapterImage extends BaseAdapter {
    ArrayList<ImageData> alImageData;
    Context context;
    private static LayoutInflater inflater=null;

    public AdapterImage(Context context, ArrayList<ImageData> alImageData){
        this.alImageData = alImageData;
        this.context=context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return alImageData.size();
    }

    @Override
    public Object getItem(int position) {
        return alImageData.get(position);
//        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
//        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        final ImageData imageData = this.alImageData.get(position);
        view = inflater.inflate(R.layout.adapter_image, null);
        ImageView ivPic = view.findViewById(R.id.ivPic);
        final CheckBox cbPic = view.findViewById(R.id.cbPic);

        if (imageData.getSelection()) {
            cbPic.setVisibility(View.VISIBLE);
        } else {
            cbPic.setVisibility(View.GONE);
        }
        view.setTag(viewGroup);
        try {
            GlideApp.with(context).load(alImageData.get(position).getFilePath()).placeholder(R.drawable.no_image).centerCrop().into(ivPic);
        } catch (Exception e){
            //do nothing
            Toast.makeText(context, "Exception: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageData.getSelection()){
                    imageData.setSelction(false);
                    notifyDataSetChanged();
                    toolbar.setTitle(getSelectedItem().size()+" items selected");
                } else {
                    imageData.setSelction(true);
                    notifyDataSetChanged();
                    toolbar.setTitle(getSelectedItem().size()+" items selected");
                }

                if (getSelectedItem().size()>0){
                    toolbar.setVisibility(View.VISIBLE);
                } else {
                    toolbar.setVisibility(View.GONE);
                }

            }
        });
        return view;
    }

    public ArrayList<ImageData> getSelectedItem() {
        ArrayList<ImageData> arrayList = new ArrayList();
        if (this.alImageData != null) {
            for (int i = 0; i < this.alImageData.size(); i++) {
                if ((this.alImageData.get(i)).getSelection()) {
                    arrayList.add(this.alImageData.get(i));
                }
            }
        }
        return arrayList;
    }

    public void setAllImagesUnseleted() {
        if (this.alImageData != null) {
            for (int i = 0; i < this.alImageData.size(); i++) {
                if ((this.alImageData.get(i)).getSelection()) {
                    (this.alImageData.get(i)).setSelction(false);
                }
            }
        }
    }
}
