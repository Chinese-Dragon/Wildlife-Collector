package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import edu.drury.mcs.wildlife.Activity.MainActivity;
import edu.drury.mcs.wildlife.R;

/**
 * Created by mark93 on 5/11/2017.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MLViewHolder>{
    private List<MainCollectionObj> data;
    private Context context;
    private LayoutInflater inflater;
    private String current_email;

    public MainListAdapter(Context context, List<MainCollectionObj> data) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        SharedPreferences shared = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        this.current_email = shared.getString(MainActivity.CURRENT_EMAIL, "");
    }


    @Override
    public MLViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = inflater.inflate(R.layout.swith_dialog_row, parent, false);

        return new MLViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(MLViewHolder holder, int position) {
        MainCollectionObj main = data.get(position);
        holder.email.setText(main.getEmail());
        holder.name.setText(main.getMain_collection_name());

        if(Objects.equals(current_email, main.getEmail())) {
            holder.check.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MLViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView email, name;
        private LinearLayout row;
        private ImageView check;

        MLViewHolder(View itemView) {
            super(itemView);
            row = (LinearLayout) itemView.findViewById(R.id.row);
            email = (TextView) itemView.findViewById(R.id.mainC_email);
            name = (TextView) itemView.findViewById(R.id.mainC_name);
            check = (ImageView) itemView.findViewById(R.id.check_mark);
            row.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == row.getId()) {

            }
        }
    }
}
