package com.example.androidproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidproject.Data.Shift;
import com.example.androidproject.R;
import com.example.androidproject.ShiftClickListner;

import java.util.List;

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ShiftViewHolder>
{
    private Shift[] shifts;
    private ShiftClickListner listner;

    public ShiftAdapter(Shift[] shifts, ShiftClickListner listner)

    {
        setShifts(shifts);
        this.listner = listner;
    }

    public void setShifts(Shift[] shifts)
    {
        this.shifts = shifts;
        notifyDataSetChanged();  // This will refresh the RecyclerView
    }

    @NonNull
    @Override
    public ShiftAdapter.ShiftViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shifts_layout, parent, false);//
        // this line tell him wich xml Describes how line looks like, in our case this: R.layout.shifts_layout .

        ShiftViewHolder myViewHolder = new ShiftViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftAdapter.ShiftViewHolder holder, int position)
    // this will position the lines(as we design them) in our recyleView, its active loop that we dont see behind the scene,
    //it will bind the information to that line.
    {
        TextView textViewShiftName = holder.textViewShiftName;
        TextView textViewAlreadyWork = holder.textViewAlreadyWork;
        TextView textViewNeedMoreWorkers = holder.textViewNeedMoreWorkers;

        textViewShiftName.setText(shifts[position].getShiftType());
        textViewAlreadyWork.setText(String.format("%d", shifts[position].getWorkersInShiftList().size()));
        textViewNeedMoreWorkers.setText(String.format("%d", shifts[position].getNumberOfWorkersNeedMoreToShift()));

        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listner.onShiftClicked(shifts[position]);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return shifts != null ? shifts.length : 0;
    }

    public static class ShiftViewHolder extends RecyclerView.ViewHolder
    {
        private TextView textViewShiftName;
        private TextView textViewAlreadyWork;
        private TextView textViewNeedMoreWorkers;
        private CardView cardView;

        public ShiftViewHolder(@NonNull View itemView)
        {
            super(itemView);

            textViewShiftName = itemView.findViewById(R.id.textViewShiftName);
            textViewAlreadyWork = itemView.findViewById(R.id.textViewShiftAlreadyWorkInThisShift);
            textViewNeedMoreWorkers = itemView.findViewById(R.id.textViewShiftNeedMoreForFull);
            cardView = itemView.findViewById(R.id.CardViewShifts);
        }
    }
}
