package edu.drury.mcs.wildlife.JavaClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.drury.mcs.wildlife.Activity.SpeciesDataTable;
import edu.drury.mcs.wildlife.R;

import static android.content.ContentValues.TAG;

/**
 * Created by yma004 on 12/11/16.
 */

public class sAdapter extends RecyclerView.Adapter<sAdapter.sViewHolder> {
    public final static String EXTRA_CURRENTSPECIES = "edu.drury.mcs.wildlife.CURRENTSPECIES";
    public final static int STATIC_INTEGER_VALUE = 100;
    private Context context;
    private LayoutInflater inflater;
    private List<Species> data;
    private CollectionObj currentCollection;

    public sAdapter(Context context, List<Species> data) {
        this.context = context;
        this.data = data;
        this.currentCollection = currentCollection;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public sViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = inflater.inflate(R.layout.species_card, parent, false);
        return new sViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(sViewHolder holder, int position) {
        int id = context.getResources().getIdentifier("species_"+Integer.toString(position),"drawable",context.getPackageName());

        Species current = data.get(position);
        holder.commonName.setText(current.getCommonName());
        holder.scientificName.setText(current.getScientificName());
        holder.speciesImage.setImageResource(id);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class sViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView commonName;
        TextView scientificName;
        ImageView speciesImage;

        public sViewHolder(View itemView) {
            super(itemView);

            commonName = (TextView) itemView.findViewById(R.id.commonName);
            scientificName = (TextView) itemView.findViewById(R.id.scientificName);
            speciesImage = (ImageView) itemView.findViewById(R.id.species_image);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int groupID = data.get(this.getAdapterPosition()).getGroup_ID();
            String scientificName = data.get(this.getAdapterPosition()).getScientificName();
            String commonName = data.get(this.getAdapterPosition()).getCommonName();

            Species currentSpecies = new Species(commonName,scientificName,groupID);
            Intent intent = new Intent(context, SpeciesDataTable.class);
            Bundle sBundle = new Bundle();

            sBundle.putParcelable(EXTRA_CURRENTSPECIES,currentSpecies);
            intent.putExtras(sBundle);
            Log.i(TAG,currentSpecies.getCommonName());
            ((Activity)context).startActivityForResult(intent,STATIC_INTEGER_VALUE);
        }
    }
}
