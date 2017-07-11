package net.wefu.test.minesweeper;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by wefu on 2017-07-09.
 */
public class BombAdapter extends ArrayAdapter<Cell>{

    private Context mContext;

    public BombAdapter(Context context, int resource, List<Cell> cellList) {
        super(context, resource, cellList);
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.adapter_cell, null);
            ViewHolder holder =  new ViewHolder();
            holder.tvCell = (TextView) convertView.findViewById(R.id.tv_cell);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) convertView.getTag();
        Cell item = getItem(position);

        if (item.isFlag()) {
            holder.ivIcon.setVisibility(View.VISIBLE);
            holder.ivIcon.setImageResource(R.drawable.ic_flag);
            return convertView;
        }


        if (item.isSelected()) {
            holder.tvCell.setBackgroundColor(Color.WHITE);
            if (item.isBomb()) {
                holder.ivIcon.setVisibility(View.VISIBLE);
                holder.ivIcon.setImageResource(R.drawable.ic_bomb);
            } else {
                if (item.getBombCount() == 0) {
                    holder.tvCell.setText("");
                } else {
                    holder.tvCell.setText(String.valueOf(item.getBombCount()));
                }
            }
        } else {
            holder.ivIcon.setVisibility(View.GONE);
            holder.tvCell.setBackgroundResource(R.color.colorPrimary);
            holder.tvCell.setText("");
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tvCell;
        public ImageView ivIcon;
    }


}
