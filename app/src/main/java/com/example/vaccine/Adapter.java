package com.example.vaccine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    Context context;
    List<CenterRvModal> modals ;
    itemClickinterface itemClickinterface;
    public Adapter(Context context, List<CenterRvModal> modals,itemClickinterface itemClickinterface) {
        this.context = context;
        this.modals = modals;
        this.itemClickinterface=itemClickinterface;
    }

    @NonNull

    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.center_rv_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull  Adapter.ViewHolder holder, int position) {
    CenterRvModal rvModal=modals.get(position);
    holder.centerNameTV.setText(rvModal.getCenterName());
    holder.centerAddressTV.setText(rvModal.getCenterAddress());
    holder.centerTimings.setText(rvModal.getCenterToTime());
    holder.vaccineNameTV.setText(rvModal.getVaccineName());
    holder.centerAgeLimitTV.setText("Age Group "+rvModal.getAgeLimit());
    holder.centerFeeTypeTV.setText("Price "+rvModal.getFee_type());
    holder.avalabilityTV.setText("Avalaible "+rvModal.getAvailableCapacity()+"");
    holder.date.setText("Date : "+rvModal.getDate());
    }

    @Override
    public int getItemCount() {
        return modals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView centerNameTV;
        TextView centerAddressTV;
        TextView centerTimings;
        TextView vaccineNameTV;
        TextView centerAgeLimitTV;
        TextView centerFeeTypeTV;
        TextView avalabilityTV;
        TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            centerNameTV = itemView.findViewById(R.id.idTVCenterName);
            centerAddressTV = itemView.findViewById(R.id.idTVCenterAddress);
            centerTimings = itemView.findViewById(R.id.idTVCenterTimings);
            vaccineNameTV = itemView.findViewById(R.id.idTVVaccineName);
            centerAgeLimitTV = itemView.findViewById(R.id.idTVAgeLimit);
            centerFeeTypeTV= itemView.findViewById(R.id.idTVFeeType);
            avalabilityTV= itemView.findViewById(R.id.idTVAvaliablity);
            date=itemView.findViewById(R.id.idTVCenterDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickinterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
