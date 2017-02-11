package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.drury.mcs.wildlife.R;

/**
 * Created by mark93 on 2/10/2017.
 */

public class tAdapter extends RecyclerView.Adapter<tAdapter.tViewHolder>{
    private List<SpeciesCollected> data;
    private Context context;
    private LayoutInflater inflater;

    public tAdapter(Context context, List<SpeciesCollected> data) {
        this.context = context;
        this.data = data;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public tViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = inflater.inflate(R.layout.viewholder_datatable,parent,false);
        return new tViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(tViewHolder holder, int position) {
        SpeciesCollected current = data.get(position);
        holder.scientificName.setText(current.getScientificName());
        holder.commonName.setText(current.getCommonName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class tViewHolder extends RecyclerView.ViewHolder implements AdapterView.OnItemSelectedListener{
        TextView commonName;
        TextView scientificName;
        Spinner quantify_spinner;

        public tViewHolder(View itemView) {
            super(itemView);

            commonName = (TextView) itemView.findViewById(R.id.certain_commonName);
            scientificName = (TextView) itemView.findViewById(R.id.certain_scientificName);
            quantify_spinner = (Spinner) itemView.findViewById(R.id.number_captured);
            ArrayAdapter<Integer> spinner_adapter = new ArrayAdapter<Integer>(context, android.R.layout.simple_spinner_item, get_spinner_array(100));
            spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            quantify_spinner.setAdapter(spinner_adapter);

            quantify_spinner.setOnItemSelectedListener(this);

        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (i != 0) {
                SpeciesCollected current = data.get(this.getAdapterPosition());
                int quantity = (int) adapterView.getSelectedItem();
                current.setQuantity(quantity);
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    }


    private List<Integer> get_spinner_array(int maxNumber) {
        List<Integer> result = new ArrayList<>();
        int i = 0;
        while(i < maxNumber) {
            result.add(i);
            i++;
        }
        return result;
    }

    public List<SpeciesCollected> getLastestItems() {
        List<SpeciesCollected> result = new ArrayList<>();

        for(int i = 0; i < getItemCount(); i ++) {
            if(data.get(i).getQuantity() != 0) {
                result.add(data.get(i));
            }
        }

        return result;
    }
}
