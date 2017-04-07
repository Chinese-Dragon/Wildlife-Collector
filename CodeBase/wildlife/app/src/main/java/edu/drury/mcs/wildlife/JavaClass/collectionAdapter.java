package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import edu.drury.mcs.wildlife.Fragment.Collection;
import edu.drury.mcs.wildlife.R;

/**
 * Created by mark93 on 12/3/2016.
 */

public class collectionAdapter extends RecyclerView.Adapter<collectionAdapter.cViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private Collection cFragment;
    private List<CollectionObj> collectionData;

    public collectionAdapter(Context context, List<CollectionObj> data, Collection frag) {
        this.context = context;
        this.collectionData = data;
        this.cFragment = frag;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public cViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View holder = inflater.inflate(R.layout.viewhodlder_collection,parent,false);
        return new cViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(cViewHolder holder, int position) {
        List<Address> addresses = null;
        String address_string = "";
        CollectionObj current = collectionData.get(position);
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        int snake= 0 , turtle= 0, salamander = 0, lizard= 0, frog = 0;

        try {
            addresses = geocoder.getFromLocation(current.getLocation().getLatitude(), current.getLocation().getLongitude(), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses == null || addresses.size() == 0) {
            address_string = "Latitude: " + Double.toString(current.getLocation().getLatitude()) + " Longitude: " + Double.toString(current.getLocation().getLongitude());
        } else {
            Address address = addresses.get(0);
            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                address_string += address.getAddressLine(i) + " ";
            }
        }

        //get captured animal data
        for(Species s: current.getSpecies()) {
            switch (s.getGroup_ID()) {
                case 1:
                    salamander = s.getSpecies_Data().size();
                    break;
                case 2:
                    frog = s.getSpecies_Data().size();
                    break;
                case 3:
                    lizard = s.getSpecies_Data().size();
                    break;
                case 4:
                    snake = s.getSpecies_Data().size();
                    break;
                case 5:
                    turtle = s.getSpecies_Data().size();
                    break;
                default:break;
            }
        }

        // setup content in the current card
        holder.collectionName.setText(current.getCollection_name());
        holder.collection_date.setText(current.getDate());
        holder.collection_address.setText(address_string);
        holder.frog_quantity.setText(Integer.toString(frog));
        holder.lizard_quantity.setText(Integer.toString(lizard));
        holder.salamander_quantity.setText(Integer.toString(salamander));
        holder.turtle_quantity.setText(Integer.toString(turtle));
        holder.snake_quantity.setText(Integer.toString(snake));
    }

    @Override
    public int getItemCount() {
        return collectionData.size();
    }


    public void addNewData(CollectionObj newC) {
        this.collectionData.add(0, newC);
        notifyItemInserted(0);
    }

    class cViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{
        TextView collectionName, collection_date, collection_address, frog_quantity, snake_quantity, turtle_quantity, lizard_quantity, salamander_quantity;
        ImageView editOption;
        CardView card;


        public cViewHolder(View itemView) {
            super(itemView);

            collectionName = (TextView) itemView.findViewById(R.id.collection_name);
            editOption = (ImageView) itemView.findViewById(R.id.edit);
            collection_date = (TextView) itemView.findViewById(R.id.collection_date);
            collection_address = (TextView) itemView.findViewById(R.id.collection_address);
            frog_quantity = (TextView) itemView.findViewById(R.id.frog_quantity);
            snake_quantity = (TextView) itemView.findViewById(R.id.snake_quantity);
            turtle_quantity = (TextView) itemView.findViewById(R.id.turtle_quantity);
            lizard_quantity = (TextView) itemView.findViewById(R.id.lizard_quantity);
            salamander_quantity = (TextView) itemView.findViewById(R.id.salamander_quantity);
            card = (CardView) itemView.findViewById(R.id.collection_card);

            editOption.setOnClickListener(this);
            collectionName.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view == editOption) {
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                popup.inflate(R.menu.collection_edit_options);
                popup.setOnMenuItemClickListener(this);
                popup.show();
            }
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                CollectionObj clickedCollection = collectionData.get(this.getAdapterPosition());
                collectionData.remove(this.getAdapterPosition());
                Log.i("readTask", " mainCurrentName " + cFragment.getCurrent_mainCollection().getMain_collection_name());
                clickedCollection.deleteFromDB(context,cFragment.getCurrent_mainCollection());
                Message.showMessage(editOption.getContext(), clickedCollection.getCollection_name() + " is deleted");
                notifyItemRemoved(this.getAdapterPosition());
            }

            return true;
        }


    }
}
