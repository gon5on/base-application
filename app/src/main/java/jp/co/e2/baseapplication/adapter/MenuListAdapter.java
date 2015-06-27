package jp.co.e2.baseapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import jp.co.e2.baseapplication.R;

/**
 * メニューリストアダプター
 */
public class MenuListAdapter extends BaseAdapter {
    private static final int TITLES[] = {
            R.string.menu_dialog_sample,
            R.string.menu_asynk_task_sample,
            R.string.menu_billing_sample,
            R.string.menu_card_view_sample,
    };
    private static final int ICONS[] = {
            android.R.drawable.ic_menu_compass,
            android.R.drawable.ic_menu_help,
            android.R.drawable.ic_menu_preferences,
            android.R.drawable.ic_menu_add,
    };

    private Context mContext;

    /**
     * コンストラクタ
     */
    public MenuListAdapter(Context context) {
        super();

        mContext = context;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.part_menu_list_row, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.icon = (ImageView) convertView.findViewById(R.id.imageViewIcon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(TITLES[position]);
        holder.icon.setImageResource(ICONS[position]);

        return convertView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        int cnt = 0;

        if (TITLES != null) {
            cnt = TITLES.length;
        }

        return cnt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getItem(int position) {
        String item = null;

        if (TITLES != null) {
            item = mContext.getString(TITLES[position]);
        }

        return item;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * ViewHolder
     */
    private static class ViewHolder {
        ImageView icon;
        TextView text;
    }
}