package com.example.androidproject.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidproject.Adapters.ShiftAdapter;
import com.example.androidproject.Data.DataBase;
import com.example.androidproject.Data.Day;
import com.example.androidproject.Data.Weak;
import com.example.androidproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentWeeks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWeeks extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerViewDays;
    private LinearLayoutManager layoutManager;
    private ShiftAdapter adapter;
    private DataBase dataBase = DataBase.getInstance();

    public FragmentWeeks() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentWeeks.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentWeeks newInstance(String param1, String param2) {
        FragmentWeeks fragment = new FragmentWeeks();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weeks, container, false);
        DataBase dataBase = DataBase.getInstance();
        Button dayOne = v.findViewById(R.id.buttonDayOne);
        Button dayTwo = v.findViewById(R.id.buttonDayTwo);
        Button dayThree = v.findViewById(R.id.buttonDayThree);
        Button dayFour = v.findViewById(R.id.buttonDayFour);
        Button dayFive = v.findViewById(R.id.buttonDayFive);
        Button daySix = v.findViewById(R.id.buttonDaySix);
        Button daySeven = v.findViewById(R.id.buttonDaySeven);
        Button createWeakOrLastWeak = v.findViewById(R.id.buttonCreateOrWeekBefore);
        Button nextWeak = v.findViewById(R.id.buttonNextWeek);
        EditText editTextDay = v.findViewById(R.id.editTextTextNumverOfDay);
        EditText editTextMonth = v.findViewById(R.id.editTextTextNumverOfMonth);
        EditText editTextYear = v.findViewById(R.id.editTextTextNumverOfYear);
        Weak currentWeak;
        Boolean haveCurrentWeak = false;

        createWeakOrLastWeak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {

                }
                if(!haveCurrentWeak)
                {
                    currentWeak = dataBase.CreateFirstWeakAndGetIt(editTextDay.getText(), editTextMonth.getText(),
                            editTextYear.getText());
                }
                else
                {

                }
            }
        });

        dayOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //dataBase

            }
        });
        int i;
        Weak weak;
        Day fortry = null;

        try
        {
            weak = new Weak("8", "2", "2024");
            weak.FillAllDaysWithShifts(3, 32);
            fortry = weak.getASpecificDay(1);
        }
        catch (Exception e)
        {
            Toast toast = new Toast(getContext());
            toast.setText(e.getMessage());
            toast.show();
        }
        try
        {
        recyclerViewDays = v.findViewById(R.id.RecyclerViewDays);
        layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDays.setLayoutManager(layoutManager);
        recyclerViewDays.setItemAnimator(new DefaultItemAnimator());

        ShiftAdapter adapter = new ShiftAdapter(fortry.getShiftsToday());
        recyclerViewDays.setAdapter(adapter);
        }
        catch (Exception ex)
        {
            int a = 5;
        }

        try {
            return v;
        }
        catch (Exception ex)
        {
            int a = 5;
        }
        return v;
    }

}