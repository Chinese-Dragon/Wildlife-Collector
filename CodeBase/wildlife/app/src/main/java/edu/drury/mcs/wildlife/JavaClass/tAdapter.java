package edu.drury.mcs.wildlife.JavaClass;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
            holder.quantity_rm.setText(Integer.toString(current.getNum_removed()));
            holder.quantity_captured = current.getQuantity();
            holder.quantity_removed = current.getNum_removed();

            if(current.getVoucher_specimen_retained()) {
                Message.showMessage(context, "update specimen: is specimen");
                holder.specimen_yes.setChecked(true);
//                holder.specimen_no.setChecked(false);
            } else {
                Message.showMessage(context, "update specimen: no specimen");
                holder.specimen_no.setChecked(true);
//                holder.specimen_yes.setChecked(false);
            }

            if(current.getIs_blood_taken()) {
                Message.showMessage(context, "update blood radio button: is taken");
                holder.blood_yes.setChecked(true);
//                holder.blood_no.setChecked(false);
            } else {
                Message.showMessage(context, "update blood radio button: not taken");
                holder.blood_no.setChecked(true);
//                holder.blood_yes.setChecked(false);
            }
            holder.disposition_spinner.setSelection(holder.adapter.getPosition(current.getStatus().toString()));
            holder.band_number.setText(current.getBand_num());
            holder.card.setBackgroundColor(Color.parseColor("#a6d8a8"));
        } else {
            // reset view holder
            holder.quantity.setText("0");
            holder.quantity_rm.setText("0");
            holder.quantity_captured = 0;
            holder.quantity_removed = 0;
            holder.radioGroup.clearCheck();
            holder.radioGroup2.clearCheck();
            holder.band_number.setText("");
            holder.disposition_spinner.setSelection(0);

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


    class tViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, AdapterView.OnItemSelectedListener{
        static final int DURATION = 250;
        TextView commonName;
        TextView scientificName;
        ImageView toggle;
        CardView card;
        ViewGroup linearLayoutDetail;
        ImageButton increase,decrease, rm_increase, rm_decrease;
        EditText quantity, quantity_rm, band_number;
        RadioGroup radioGroup, radioGroup2;
        RadioButton specimen_yes, specimen_no, blood_yes, blood_no;
        Spinner disposition_spinner;
        int quantity_removed = 0;
        int quantity_captured = 0;
        ArrayAdapter<CharSequence> adapter;

        public tViewHolder(View itemView) {
            super(itemView);

            commonName = (TextView) itemView.findViewById(R.id.commonName);
            scientificName = (TextView) itemView.findViewById(R.id.scientificName);
            toggle = (ImageView) itemView.findViewById(R.id.toggle);
            card = (CardView) itemView.findViewById(R.id.card);
            linearLayoutDetail = (ViewGroup) itemView.findViewById(R.id.lineardetail);
            increase = (ImageButton) itemView.findViewById(R.id.increase);
            decrease = (ImageButton) itemView.findViewById(R.id.decrease);
            quantity = (EditText) itemView.findViewById(R.id.quantity_captured);
            rm_increase = (ImageButton) itemView.findViewById(R.id.rm_increase);
            rm_decrease = (ImageButton) itemView.findViewById(R.id.rm_decrease);
            quantity_rm = (EditText) itemView.findViewById(R.id.quantity_removed);
            band_number = (EditText) itemView.findViewById(R.id.band_num);

            // detect edittext change and then change corresponding data
            band_number.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length() > 0) {
                        data.get(getAdapterPosition()).setBand_num(band_number.getText().toString());
                    }
                }
            });

            radioGroup = (RadioGroup) itemView.findViewById(R.id.radio_group);
            radioGroup2 = (RadioGroup) itemView.findViewById(R.id.radio_group_2);
            specimen_yes = (RadioButton) itemView.findViewById(R.id.specimen_yes);
            specimen_no = (RadioButton) itemView.findViewById(R.id.specimen_no);
            blood_yes = (RadioButton) itemView.findViewById(R.id.blood_yes);
            blood_no = (RadioButton) itemView.findViewById(R.id.blood_no);
            disposition_spinner = (Spinner) itemView.findViewById(R.id.disposition);
            adapter = ArrayAdapter.createFromResource(context,R.array.disposition_array,
                    android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            disposition_spinner.setAdapter(adapter);

            disposition_spinner.setOnItemSelectedListener(this);
            card.setOnClickListener(this);
            increase.setOnClickListener(this);
            decrease.setOnClickListener(this);
            rm_increase.setOnClickListener(this);
            rm_decrease.setOnClickListener(this);
            specimen_yes.setOnClickListener(this);
            specimen_no.setOnClickListener(this);
            blood_yes.setOnClickListener(this);
            blood_no.setOnClickListener(this);
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
                if(quantity_captured > 0 && quantity_captured > quantity_removed) {
                    quantity_captured --;
                    quantity.setText(Integer.toString(quantity_captured));
                    data.get(getAdapterPosition()).setQuantity(quantity_captured);
                }
            } else if (view == rm_increase) {
                if (quantity_removed < quantity_captured) {
                    quantity_removed ++;
                    quantity_rm.setText(Integer.toString(quantity_removed));
                    data.get(getAdapterPosition()).setNum_removed(quantity_removed);
                    data.get(getAdapterPosition()).setNum_released(quantity_captured - quantity_removed);
                } else {
                    Message.showMessage(context, "Cannot remove more than captured");
                }
            } else if (view == rm_decrease) {
                if (quantity_removed > 0) {
                    quantity_removed --;
                    quantity_rm.setText(Integer.toString(quantity_removed));
                    data.get(getAdapterPosition()).setNum_removed(quantity_removed);
                    data.get(getAdapterPosition()).setNum_released(quantity_captured - quantity_removed);
                }
            } else if (view == specimen_yes) {
                Message.showMessage(context,"radio button clicked");
                data.get(getAdapterPosition()).setVoucher_specimen_retained(true);
            } else if (view == specimen_no) {
                data.get(getAdapterPosition()).setVoucher_specimen_retained(false);
            } else if (view == blood_yes) {
                data.get(getAdapterPosition()).setIs_blood_taken(true);
            } else if (view == blood_no) {
                data.get(getAdapterPosition()).setIs_blood_taken(false);
            }
        }

        private void rotate(float angle) {
            Animation animation = new RotateAnimation(0.0f, angle, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setFillAfter(true);
            animation.setDuration(DURATION);
            toggle.startAnimation(animation);
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if (adapterView.getId() == disposition_spinner.getId()) {
                String selection = (String) adapterView.getItemAtPosition(i);
                switch (selection) {
                    case "Released":
                        data.get(getAdapterPosition()).setStatus(SpeciesCollected.Disposition.RELEASED);
                        break;
                    case "Held In Captivity":
                        data.get(getAdapterPosition()).setStatus(SpeciesCollected.Disposition.HELD);
                        break;
                    case "Killed For Study Purpose":
                        Message.showMessage(context, "Killed");
                        data.get(getAdapterPosition()).setStatus(SpeciesCollected.Disposition.KILLED);
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

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

    public void setFilter(List<SpeciesCollected> newList) {
        data = new ArrayList<>();
        data.addAll(newList);
        notifyDataSetChanged();
    }
}
