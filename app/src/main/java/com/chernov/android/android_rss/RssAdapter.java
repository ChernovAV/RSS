package com.chernov.android.android_rss;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Android on 29.10.2015.
 */
public class RssAdapter extends ArrayAdapter<Item> {

    private final List<Item> items;
    private final Context context;

    public RssAdapter(Context context, List<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.author = (TextView) convertView.findViewById(R.id.author);
            holder.image = (ImageView) convertView.findViewById(R.id.nss);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.title.setText(items.get(position).getTitle());
        holder.author.setText(items.get(position).getAuthor());
        holder.date.setText(items.get(position).getDate());

        Picasso.with(context)
                .load(items.get(position).getImage())
                .tag(RssActivity.class)
                //.resize(120, 100)
                .transform(new RssTransformation())
                //.centerCrop()
                .error(R.mipmap.news)
                .into(holder.image);

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView date;
        TextView author;
        ImageView image;
    }
}
