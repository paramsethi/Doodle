package com.ms.doodle.app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

/**
 * Created by prgirase on 4/30/14.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter (Context c)
    {
        mContext = c;
    }
    public int getCount()
    {
       return 0;
    }
    public Object getItem(int Position)
    {
        return null;
    }
    public long getItemId(int Position)
    {
        return 0;
    }
    public View getView(int Position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if (convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        }
        else
        {
            imageView = (ImageView) convertView;
        }

        //Uri targetUri = Uri.parse(ImageActivity.mUrls[Position]);
        return null;
    }
}
