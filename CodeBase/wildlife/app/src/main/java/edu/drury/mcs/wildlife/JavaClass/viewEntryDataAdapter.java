package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.drury.mcs.wildlife.R;

/**
 * Created by mark93 on 4/11/2017.
 */

public class viewEntryDataAdapter extends RecyclerView.Adapter<viewEntryDataAdapter.viewEntryDataViewHolder> {
    private Context context;
    private List<SpeciesCollected> data;
    private LayoutInflater inflater;
    private int group_id;

    public viewEntryDataAdapter(Context _context, List<SpeciesCollected> _data, int _group_id) {
        this.context = _context;
        this.data = _data;
        this.inflater = LayoutInflater.from(_context);
        this.group_id = _group_id;
    }


    @Override
    public viewEntryDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = inflater.inflate(R.layout.viewsccard, parent, false);
        return new viewEntryDataViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(viewEntryDataViewHolder holder, int position) {
        SpeciesCollected sc = data.get(position);
        int resource_id = getResourceId(group_id);
        if (resource_id != -100) {
            holder.species_image.setImageResource(resource_id);
        }
        holder.scientific_name.setText(sc.getScientificName());
        holder.num_remove.setText(Integer.toString(sc.getNum_removed()));
        holder.num_capture.setText(Integer.toString(sc.getQuantity()));
        holder.num_release.setText(Integer.toString(sc.getNum_released()));
        holder.band_num.setText(sc.getBand_num());
        holder.status.setText(sc.getStatus().name());

        if(sc.getIs_blood_taken()) {
            holder.blood.setText("Yes");
        } else {
            holder.blood.setText("No");
        }

        if(sc.getVoucher_specimen_retained()) {
            holder.specimen.setText("Yes");
        } else {
            holder.specimen.setText("No");
        }

        // hide the divider when for the last one
        if( position == data.size() - 1) {
            holder.divider.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private int getResourceId(int _group_id) {
        switch (_group_id) {
            case 1:
                return context.getResources().getIdentifier("salamander_big","drawable", context.getPackageName());
            case 2:
                return context.getResources().getIdentifier("frog","drawable", context.getPackageName());
            case 3:
                return context.getResources().getIdentifier("lizard_big","drawable", context.getPackageName());
            case 4:
                return context.getResources().getIdentifier("snake","drawable", context.getPackageName());
            case 5:
                return context.getResources().getIdentifier("turtle","drawable", context.getPackageName());
            default:
                return -100;
        }
    }

    class viewEntryDataViewHolder extends RecyclerView.ViewHolder {
        private ImageView species_image;
        private TextView num_capture, num_release, num_remove, band_num,scientific_name, specimen, blood, status;
        private View divider;

        public viewEntryDataViewHolder(View itemView) {
            super(itemView);
            species_image = (ImageView) itemView.findViewById(R.id.species_image);
            num_capture = (TextView) itemView.findViewById(R.id.num_capture);
            num_release = (TextView) itemView.findViewById(R.id.num_release);
            num_remove = (TextView) itemView.findViewById(R.id.num_remove);
            scientific_name = (TextView) itemView.findViewById(R.id.data_name);
            specimen = (TextView) itemView.findViewById(R.id.specimen);
            blood = (TextView) itemView.findViewById(R.id.blood);
            status = (TextView) itemView.findViewById(R.id.status);
            band_num = (TextView) itemView.findViewById(R.id.band_num);
            divider = itemView.findViewById(R.id.divider);
        }
    }
}
