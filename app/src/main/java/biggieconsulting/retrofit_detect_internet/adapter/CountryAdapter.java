package biggieconsulting.retrofit_detect_internet.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import biggieconsulting.retrofit_detect_internet.R;
import biggieconsulting.retrofit_detect_internet.model.Country;

/**
 * Author: mzm
 * Created on 18/07/2018 AD
 */
public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.ViewHolder> {
    private Context mContext;

    private List<Country> mDataList;

    private ClickListener clickListener;

    public CountryAdapter(Context context, List<Country> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Country entity = mDataList.get(position);
        holder.text.setText(entity.getName());
    }

    public void setOnClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.text);
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

}
