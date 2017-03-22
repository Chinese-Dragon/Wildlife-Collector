package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import edu.drury.mcs.wildlife.Fragment.Collection;
import edu.drury.mcs.wildlife.R;

/**
 * Created by mark93 on 12/3/2016.
 */

public class collectionAdapter extends RecyclerView.Adapter<collectionAdapter.cViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    private Collection cFragment;
    List<CollectionObj> collectionData;

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
        CollectionObj current = collectionData.get(position);
        holder.collectionName.setText(current.getCollection_name());
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
        TextView collectionName;
        ImageView editOption;

        public cViewHolder(View itemView) {
            super(itemView);

            collectionName = (TextView) itemView.findViewById(R.id.collection_name);
            editOption = (ImageView) itemView.findViewById(R.id.edit);

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
            } else if (view == collectionName) {
                //start new activity to view collection info
                Message.showMessage(collectionName.getContext(), "view collection");
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
            } else if (item.getItemId() == R.id.action_edit) {
                // edit the collection
                Message.showMessage(editOption.getContext(), "Edit collection");
            }

            return true;
        }


    }
}
