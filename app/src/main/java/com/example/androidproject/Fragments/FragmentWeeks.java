package com.example.androidproject.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Adapters.ShiftAdapter;
import com.example.androidproject.Data.DataBase;
import com.example.androidproject.Data.Day;
import com.example.androidproject.Data.Shift;
import com.example.androidproject.Data.Weak;
import com.example.androidproject.R;
import com.example.androidproject.ShiftClickListner;
import com.example.androidproject.Users.User;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentWeeks#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWeeks extends Fragment implements ShiftClickListner {

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
    private final Weak[] currentWeak = {null};
    private User loginUser;

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
    public static FragmentWeeks newInstance (String param1, String param2) {
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
        TextView dateDayOne = v.findViewById(R.id.textViewDateDayOne);
        TextView dateDayTwo = v.findViewById(R.id.textViewDateDayTwo);
        TextView dateDayThree = v.findViewById(R.id.textViewDateDayThree);
        TextView dateDayFour = v.findViewById(R.id.textViewDateDayFour);
        TextView dateDayFive = v.findViewById(R.id.textViewDateDayFive);
        TextView dateDaySix = v.findViewById(R.id.textViewDateDaySix);
        TextView dateDaySeven = v.findViewById(R.id.textViewDateDaySeven);
        final int[] currentWeakShowBeforeThisWeak = {0};

        if(dataBase.getNextWeaks().size() == 0)
        {
            dayOne.setClickable(false);
            dayOne.setVisibility(View.GONE);
            dayTwo.setVisibility(View.GONE);
            dayTwo.setClickable(false);
            dayThree.setClickable(false);
            dayThree.setVisibility(View.GONE);
            dayFour.setClickable(false);
            dayFour.setVisibility(View.GONE);
            dayFive.setClickable(false);
            dayFive.setVisibility(View.GONE);
            daySix.setClickable(false);
            daySix.setVisibility(View.GONE);
            daySeven.setClickable(false);
            daySeven.setVisibility(View.GONE);
            nextWeak.setClickable(false);
            nextWeak.setVisibility(View.GONE);
        }

        createWeakOrLastWeak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                boolean successCreatedWeak = true;
                ArrayList<Weak> weakList = dataBase.getNextWeaks();

                try
                {
                    if(weakList.size() == 0)
                    {
                        dataBase.CreateFirstWeakAndGetIt(editTextDay.getText().toString(),
                                editTextMonth.getText().toString(), editTextYear.getText().toString());
                        createWeakOrLastWeak.setText("Create Shifts");
                        editTextDay.setText("");
                        editTextMonth.setText("");
                        editTextYear.setVisibility(View.GONE);
                        editTextDay.setHint("How much shifts\n per day?");
                        editTextMonth.setHint("How much workers\n for each shift?");
                        editTextDay.setTextSize(16);
                        editTextMonth.setTextSize(16);
                    }
                    else if(weakList.get(0).getASpecificDay(1).getShiftsToday() == null)
                    {
                        Weak firstWeak = weakList.get(0);
                        dataBase.getNextWeaks().get(0).FillAllDaysWithShifts(editTextDay.getText().toString(),
                                editTextMonth.getText().toString());

                        createWeakOrLastWeak.setText("Previous week");
                        createWeakOrLastWeak.setTextSize(14);

                        editTextDay.setVisibility(View.GONE);
                        editTextMonth.setVisibility(View.GONE);

                        dayOne.setClickable(true);
                        dateDayOne.setText(firstWeak.getASpecificDay(1).getDate());
                        dayOne.setVisibility(View.VISIBLE);

                        dayTwo.setClickable(true);
                        dateDayTwo.setText(firstWeak.getASpecificDay(2).getDate());
                        dayTwo.setVisibility(View.VISIBLE);

                        dayThree.setClickable(true);
                        dateDayThree.setText(firstWeak.getASpecificDay(3).getDate());
                        dayThree.setVisibility(View.VISIBLE);

                        dayFour.setClickable(true);
                        dateDayFour.setText(firstWeak.getASpecificDay(4).getDate());
                        dayFour.setVisibility(View.VISIBLE);

                        dayFive.setClickable(true);
                        dateDayFive.setText(firstWeak.getASpecificDay(5).getDate());
                        dayFive.setVisibility(View.VISIBLE);

                        daySix.setClickable(true);
                        dateDaySix.setText(firstWeak.getASpecificDay(6).getDate());
                        daySix.setVisibility(View.VISIBLE);

                        daySeven.setClickable(true);
                        dateDaySeven.setText(firstWeak.getASpecificDay(7).getDate());
                        daySeven.setVisibility(View.VISIBLE);

                        nextWeak.setClickable(true);
                        nextWeak.setVisibility(View.VISIBLE);

                    }
                    else if(dataBase.getFinishedWeaks().size() == 0)
                    {
                        Toast.makeText(v.getContext(), "There no previos week to show, its the first week", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        dataBase.getFinishedWeaks().get(dataBase.getFinishedWeaks().size() - 1);// - currentWeakShowBeforeThisWeak[0]);
                        //createWeakOrLastWeak[0]++;
                    }
                }
                catch (Exception ex)
                {
                    String msg = ex.getMessage();
                    successCreatedWeak = false;

                    if(msg.equalsIgnoreCase("For input string: \"\""))
                    {
                        Toast.makeText(v.getContext(),"Make Sure you fill Day, Month and Year field with valid dates",
                                Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                }

                if(successCreatedWeak)
                    {
                        currentWeak[0] = dataBase.getNextWeaks().get(0);
                    }
            }
        });

        dayOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Day dayOne = checkIfHaveWeekAndGetDay(1);//currentWeak[0].getASpecificDay(1);
                setAdapterAndRecyle(v, dayOne);
            }
        });

        dayTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), "day two is clicked", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewDays = v.findViewById(R.id.RecyclerViewDays);
        layoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewDays.setLayoutManager(layoutManager);
        recyclerViewDays.setItemAnimator(new DefaultItemAnimator());

        try
        {
            Bundle arguments = getArguments();
            byte[] serializedData = arguments.getByteArray("serializedData");
            ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
            ObjectInputStream ois = new ObjectInputStream(bais);
            loginUser = (User) ois.readObject();
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return v;
    }

    private void setAdapterAndRecyle(View v, Day day)
    {
        adapter = new ShiftAdapter(day.getShiftsToday(), this);
        recyclerViewDays.setAdapter(adapter);
    }
    private Day checkIfHaveWeekAndGetDay(int dayNumber)
    {
        return currentWeak[0].getASpecificDay(dayNumber);
    }
    @Override
    public void onShiftClicked(Shift shift)
    {
        try
        {
            showOptionDialog(this.getContext(), shift);
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showOptionDialog(Context v, Shift shift) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("What you want to do?"); // Set the title

        // Set the options and their click listeners
        builder.setItems(new String[]{"Join shift", "Remove me from shift", "Show who work in this shift"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0)
                {
                    try
                    {
                        shift.JoinToShift(loginUser);
                        adapter.notifyDataSetChanged();
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(v, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if (which == 1)
                {
                    try
                    {
                        shift.RemoveFromShift(loginUser);
                        adapter.notifyDataSetChanged();
                    }
                    catch (Exception ex)
                    {
                        Toast.makeText(v, ex.getMessage(), Toast.LENGTH_LONG).show();
                    }
                } else if(which == 2)
                {
                    ArrayList<User> userList = shift.getWorkersInShiftList();
                    if(userList.size() == 0)
                    {
                        Toast.makeText(v, "There no workers in this shift yet", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        for (User usr : userList) {
                            Toast.makeText(v, "Full name: " + usr.getLastName() + " " +
                                    usr.getFirstName() + "\nid: " + usr.getId(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        builder.create().show();
    }

}