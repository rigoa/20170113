package com.rigo.noo.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rigo.noo.R;
import com.rigo.noo.util.AppLog;
import com.rigo.noo.util.AppUtil;

import java.util.List;

/**
 * Created by kbg82 on 2017-01-14.
 */

public class ItemArrayAdapter  extends ArrayAdapter<NormalItem> {
    private static String TAG = ItemArrayAdapter.class.getSimpleName();

    private Context mContext;
    private int mLayoutID;
    private List<NormalItem> mItemList;

    public ItemArrayAdapter(Context context, int resource, List<NormalItem> objects) {
        super(context, resource, objects);
        // TODO Auto-generated constructor stub
        AppLog.i(TAG, "ItemArrayAdapter");
        mContext = context;
        mLayoutID = resource;
        mItemList = objects;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View nView = convertView;
        if (nView == null) {
            LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            nView = vi.inflate(mLayoutID, null);
        }

        final NormalItem pNormalItem = mItemList.get(position);
        if(pNormalItem != null)
        {
            TextView tvTitle = (TextView) nView.findViewById(R.id.tvHistoryDate);
            tvTitle.setText(pNormalItem.getStartTime());

            TextView tvDate = (TextView) nView.findViewById(R.id.tvHistoryStep);
            tvDate.setText(pNormalItem.getData()+"");

            TextView tvSize = (TextView) nView.findViewById(R.id.tvHistoryDistance);
            tvSize.setText( AppUtil.DistanceFormat(pNormalItem.getData(), pNormalItem.getDistance()) );

        }

        return nView;
    }
}
