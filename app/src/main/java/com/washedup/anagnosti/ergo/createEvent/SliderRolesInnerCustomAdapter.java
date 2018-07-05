package com.washedup.anagnosti.ergo.createEvent;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.washedup.anagnosti.ergo.R;

import java.util.List;

public class SliderRolesInnerCustomAdapter extends BaseAdapter {

    Activity activity;
    List<CERole> allRoles;
    LayoutInflater inflater;

    public SliderRolesInnerCustomAdapter() {

    }

    public SliderRolesInnerCustomAdapter(Activity activity, List<CERole> allRoles) {
        this.activity = activity;
        this.allRoles = allRoles;

        inflater = activity.getLayoutInflater();
    }

    @Override
    public int getCount() {
        return allRoles.size();
    }

    @Override
    public Object getItem(int i) {
        return allRoles.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        InnerViewHolder holder = null;

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_child_slider_roles_role_list_checkbox_pattern, viewGroup, false);

            holder = new InnerViewHolder();

            holder.irvlistitem = view.findViewById(R.id.irvlistitem);
            holder.irvcheckbox = view.findViewById(R.id.irvcheckbox);

            view.setTag(holder);
        } else {
            holder = (InnerViewHolder) view.getTag();
        }
        CERole currentRole = allRoles.get(i);

        holder.irvlistitem.setText(currentRole.getName());

        if (currentRole.isChecked()) {
            holder.irvcheckbox.setBackgroundResource(R.drawable.checked);
        } else {
            holder.irvcheckbox.setBackgroundResource(R.drawable.check);
        }

        return view;

    }

    public void updateRecords(List<CERole> allRoles) {
        this.allRoles = allRoles;
        notifyDataSetChanged();
    }

    class InnerViewHolder {
        TextView irvlistitem;
        ImageView irvcheckbox;
    }

}
