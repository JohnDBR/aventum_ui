package com.projects.juan.journeys.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.projects.juan.journeys.R;
import com.projects.juan.journeys.models.Journey;

import java.util.ArrayList;

/**
 * Created by juan on 13/02/18.
 */

public class JourneysAdapter extends RecyclerView.Adapter<JourneysAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Journey> cours;
    private int layout;
    private OnClickListener clickListener;

    public JourneysAdapter(ArrayList<Journey> cours, int layout, OnClickListener clickListener) {
        this.cours = cours;
        this.layout = layout;
        this.clickListener = clickListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(cours.get(position), clickListener);
    }

    @Override
    public int getItemCount() {
        return cours.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView code;
        public TextView title;
        public TextView capacity;
        public TextView price;
        public TextView duration;

        public ViewHolder(View itemView) {
            super(itemView);
            code = (TextView) itemView.findViewById(R.id.code_journey);
            title = (TextView) itemView.findViewById(R.id.title_journey);
            capacity = (TextView) itemView.findViewById(R.id.capacity_journey);
            price = (TextView) itemView.findViewById(R.id.price_journey);
            duration = (TextView) itemView.findViewById(R.id.duration_journey);
        }

        public void bind(final Journey journey, final OnClickListener clickListener){

            code.setText(journey.getCode());
            title.setText(journey.getStart() + " \u2192 " + journey.getEnd());
            price.setText("Price: " + journey.getPrice());
            capacity.setText("Capacity: " + journey.getCapacity());
            duration.setText("Duration: " + journey.getDuration());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(journey);
                }
            });;
        }
    }

    public interface OnClickListener{
        void onClick(Journey journey);
    }

}
