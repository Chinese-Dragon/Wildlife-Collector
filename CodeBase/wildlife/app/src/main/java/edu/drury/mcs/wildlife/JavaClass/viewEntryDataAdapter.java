package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import edu.drury.mcs.wildlife.Fragment.updateSCDialog;
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

    public void removeItem(int position) {
        // remove from adapter
        data.remove(position);
        notifyItemRemoved(position);
    }

    public void updateItem(SpeciesCollected updateSC, int adapterPosition) {
        //update old item with updated one
        data.set(adapterPosition, updateSC);
        notifyItemChanged(adapterPosition);
    }

    public List<SpeciesCollected> getCurrentAdapterData() {
        return this.data;
    }

    @Override
    public viewEntryDataViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = inflater.inflate(R.layout.viewsccard, parent, false);
        return new viewEntryDataViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(viewEntryDataViewHolder holder, int position) {
        SpeciesCollected sc = data.get(position);

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


    class viewEntryDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        private TextView num_capture, num_release, num_remove, band_num,scientific_name, specimen, blood, status;
        private View divider;
        private ImageButton edit;

        public viewEntryDataViewHolder(View itemView) {
            super(itemView);
            edit = (ImageButton) itemView.findViewById(R.id.edit);
            num_capture = (TextView) itemView.findViewById(R.id.num_capture);
            num_release = (TextView) itemView.findViewById(R.id.num_release);
            num_remove = (TextView) itemView.findViewById(R.id.num_remove);
            scientific_name = (TextView) itemView.findViewById(R.id.data_name);
            specimen = (TextView) itemView.findViewById(R.id.specimen);
            blood = (TextView) itemView.findViewById(R.id.blood);
            status = (TextView) itemView.findViewById(R.id.status);
            band_num = (TextView) itemView.findViewById(R.id.band_num);
            divider = itemView.findViewById(R.id.divider);
            edit.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if(edit.getId() == view.getId()) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.sc_edit_options);
                popup.setOnMenuItemClickListener(this);
                popup.show();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(item.getItemId() == R.id.action_edit) {
                updateSCDialog dialog = new updateSCDialog(context, data.get(getAdapterPosition()),group_id, getAdapterPosition());
                dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "Update an Collected Species");
            }

            return true;
        }
    }
}
