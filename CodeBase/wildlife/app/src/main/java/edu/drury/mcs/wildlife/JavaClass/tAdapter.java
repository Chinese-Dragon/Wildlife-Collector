package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
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
        SpeciesCollected current = data.get(position);
        holder.scientificName.setText(current.getScientificName());
        holder.commonName.setText(current.getCommonName());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    class tViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        static final int DURATION = 250;
        TextView commonName;
        TextView scientificName;
        ImageView toggle;
        CardView card;
        ViewGroup linearLayoutDetail;

        public tViewHolder(View itemView) {
            super(itemView);

            commonName = (TextView) itemView.findViewById(R.id.commonName);
            scientificName = (TextView) itemView.findViewById(R.id.scientificName);
            toggle = (ImageView) itemView.findViewById(R.id.toggle);
            card = (CardView) itemView.findViewById(R.id.card);
            linearLayoutDetail = (ViewGroup) itemView.findViewById(R.id.lineardetail);

            toggle.setOnClickListener(this);
            card.getPreventCornerOverlap();
        }

        @Override
        public void onClick(View view) {
            if(view == toggle) {
                if(linearLayoutDetail.getVisibility() == View.GONE){
                    ExpandAndCollapseViewUtil.expand(linearLayoutDetail, DURATION);
                    toggle.setImageResource(R.drawable.circled_chevron_down);
                    rotate(-180.0f);
                } else {
                    ExpandAndCollapseViewUtil.collapse(linearLayoutDetail, DURATION);
                    toggle.setImageResource(R.drawable.circled_chevron_up);
                    rotate(180.0f);
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
