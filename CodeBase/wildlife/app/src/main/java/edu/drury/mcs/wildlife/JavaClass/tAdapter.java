package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
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
        Log.i("onBindViewHolder"," is called");
        SpeciesCollected current = data.get(position);
        holder.scientificName.setText(current.getScientificName());
        holder.commonName.setText(current.getCommonName());
        // problems with just if statement is that
        // once we chaned edtix view and color
        // when this holder is using by other data it won't eiter change back the edittext to 0
        // or reset the background color to default
        // so it shows messed content where there shouldnt be greenout but it did even if the data is still normal
        // NOTE: Thus we need to add a else statement to reset its state
        if(current.getQuantity() > 0) {
            holder.quantity.setText(Integer.toString(current.getQuantity()));
            holder.quantity_captured = current.getQuantity();
            holder.card.setBackgroundColor(Color.parseColor("#a6d8a8"));
        } else {
            holder.quantity.setText("0");
            holder.quantity_captured = 0;
            holder.card.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    @Override
    public void onViewDetachedFromWindow(tViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.linearLayoutDetail.setVisibility(View.GONE);
        holder.toggle.setImageResource(R.drawable.circled_chevron_down);
        Log.i("Message",holder.commonName.getText().toString() + " is detached from window");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void swap(List<SpeciesCollected> newData) {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
        Log.i("SWAP"," is called ");
    }


    class tViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        static final int DURATION = 250;
        TextView commonName;
        TextView scientificName;
        ImageView toggle;
        CardView card;
        ViewGroup linearLayoutDetail;
        ImageView increase,decrease;
        EditText quantity;
        int quantity_captured = 0;

        public tViewHolder(View itemView) {
            super(itemView);

            commonName = (TextView) itemView.findViewById(R.id.commonName);
            scientificName = (TextView) itemView.findViewById(R.id.scientificName);
            toggle = (ImageView) itemView.findViewById(R.id.toggle);
            card = (CardView) itemView.findViewById(R.id.card);
            linearLayoutDetail = (ViewGroup) itemView.findViewById(R.id.lineardetail);
            increase = (ImageView) itemView.findViewById(R.id.increase);
            decrease = (ImageView) itemView.findViewById(R.id.decrease);
            quantity = (EditText) itemView.findViewById(R.id.quantity_captured);
            quantity.setText("0");

            card.setOnClickListener(this);
            increase.setOnClickListener(this);
            decrease.setOnClickListener(this);

            card.getPreventCornerOverlap();
        }

        @Override
        public void onClick(View view) {
            if(view == card) {
                if(linearLayoutDetail.getVisibility() == View.GONE){
                    ExpandAndCollapseViewUtil.expand(linearLayoutDetail, DURATION);
                    toggle.setImageResource(R.drawable.circled_chevron_down);
                    rotate(-180.0f);
                } else {
                    ExpandAndCollapseViewUtil.collapse(linearLayoutDetail, DURATION);
                    toggle.setImageResource(R.drawable.circled_chevron_up);
                    rotate(180.0f);
                }
            } else if (view == increase){
                quantity_captured ++;
                quantity.setText(Integer.toString(quantity_captured));
                data.get(getAdapterPosition()).setQuantity(quantity_captured);

            } else  if (view == decrease){
                if(quantity_captured > 0) {
                    quantity_captured --;
                    quantity.setText(Integer.toString(quantity_captured));
                    data.get(getAdapterPosition()).setQuantity(quantity_captured);

                }
            }
        }

        private void rotate(float angle) {
            Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(DURATION);
            toggle.startAnimation(animation);
        }
    }

    public List<SpeciesCollected> getLatestItems() {
        List<SpeciesCollected> result = new ArrayList<>();

        for(int i = 0; i < getItemCount(); i ++) {
            if(data.get(i).getQuantity() > 0) {
                result.add(data.get(i));
            }
        }

        return result;
    }
}
