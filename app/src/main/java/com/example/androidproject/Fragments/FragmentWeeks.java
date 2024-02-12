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
    private Day dayUserOnRightNow;
    Button dayOne;
    Button dayTwo;
    Button dayThree;
    Button dayFour;
    Button dayFive;
    Button daySix;
    Button daySeven;
    Button createWeakOrLastWeak;
    Button nextWeak;
    Button salaryCalc;
    EditText editTextDay;
    EditText editTextMonth;
    EditText editTextYear;
    TextView dateDayOne;
    TextView dateDayTwo;
    TextView dateDayThree;
    TextView dateDayFour;
    TextView dateDayFive;
    TextView dateDaySix;
    TextView dateDaySeven;
    private DataBase dataBase;

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
        View v = inflater.inflate(R.layout.fragment_weeks, container, false);
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                dataBase = DataBase.getInstance();
            }
        });

        thread.start();
        dayOne = v.findViewById(R.id.buttonDayOne);
        dayTwo = v.findViewById(R.id.buttonDayTwo);
        dayThree = v.findViewById(R.id.buttonDayThree);
        dayFour = v.findViewById(R.id.buttonDayFour);
        dayFive = v.findViewById(R.id.buttonDayFive);
        daySix = v.findViewById(R.id.buttonDaySix);
        daySeven = v.findViewById(R.id.buttonDaySeven);
        createWeakOrLastWeak = v.findViewById(R.id.buttonCreateOrWeekBefore);
        nextWeak = v.findViewById(R.id.buttonNextWeek);
        salaryCalc = v.findViewById(R.id.buttonCalculateSalery);
        editTextDay = v.findViewById(R.id.editTextTextNumverOfDay);
        editTextMonth = v.findViewById(R.id.editTextTextNumverOfMonth);
        editTextYear = v.findViewById(R.id.editTextTextNumverOfYear);
        dateDayOne = v.findViewById(R.id.textViewDateDayOne);
        dateDayTwo = v.findViewById(R.id.textViewDateDayTwo);
        dateDayThree = v.findViewById(R.id.textViewDateDayThree);
        dateDayFour = v.findViewById(R.id.textViewDateDayFour);
        dateDayFive = v.findViewById(R.id.textViewDateDayFive);
        dateDaySix = v.findViewById(R.id.textViewDateDaySix);
        dateDaySeven = v.findViewById(R.id.textViewDateDaySeven);

        try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
            Toast.makeText(v.getContext(), "Unexpeted error", Toast.LENGTH_LONG).show();
        }

        if(dataBase.getNextWeaks().size() == 0)
        {
            closeDays();
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
                    if(weakList.size() == 0)//create first weak
                    {
                        currentWeak[0] = dataBase.CreateFirstWeakAndGetIt(editTextDay.getText().toString(),
                                editTextMonth.getText().toString(), editTextYear.getText().toString());
                        createWeakOrLastWeak.setText("Create Shifts");
                        setReadyForNewWeak();
                    }
                    else if(weakList.get(0).getASpecificDay(1).getShiftsToday() == null)//have weak without shifts
                    {
                        fillUIWithData();
                        createWeakOrLastWeak.setText("Previous week");
                        createWeakOrLastWeak.setTextSize(14);
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
                        currentWeak[0] = weakList.get(0);
                    }
            }
        });

        dayOne.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(1);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        dayTwo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(2);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        dayThree.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(3);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        dayFour.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(4);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        dayFive.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(5);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        daySix.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(6);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        daySeven.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dayUserOnRightNow = checkIfHaveWeekAndGetDay(7);
                setAdapterAndRecyle(v, dayUserOnRightNow);
                activeRecycle();
            }
        });

        nextWeak.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    if(recyclerViewDays.getVisibility() == View.VISIBLE)
                    {
                        currentWeak[0] = dataBase.CreateNextWeakAndGetIt();
                        closeDays();
                        createWeakOrLastWeak.setVisibility(View.INVISIBLE);
                        createWeakOrLastWeak.setClickable(false);
                        recyclerViewDays.setClickable(false);
                        recyclerViewDays.setVisibility(View.INVISIBLE);
                        closeDates();
                        setReadyForNewWeak();
                        nextWeak.setText("Create shifts");
                        nextWeak.setTextSize(13);
                    }
                    else
                    {
                        fillUIWithData();
                        openDates();
                        nextWeak.setText("Next Week");
                        createWeakOrLastWeak.setVisibility(View.VISIBLE);
                        createWeakOrLastWeak.setClickable(true);
                    }
                }
                catch (Exception ex)
                {
                    Toast.makeText(v.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        salaryCalc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(), "Your salary is: " + loginUser.getNumberOfShiftsWorkedThatMonth() * 8 * 40,
                        Toast.LENGTH_LONG).show();
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
            if(loginUser.getFirstName().equalsIgnoreCase("Robi"))
            {
                loginUser.setRole("Admin");
            }
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return v;
    }

    private void openDates()
    {
        dateDayOne.setVisibility(View.VISIBLE);
        dateDayTwo.setVisibility(View.VISIBLE);
        dateDayThree.setVisibility(View.VISIBLE);
        dateDayFour.setVisibility(View.VISIBLE);
        dateDayFive.setVisibility(View.VISIBLE);
        dateDaySix.setVisibility(View.VISIBLE);
        dateDaySeven.setVisibility(View.VISIBLE);
    }

    private void activeRecycle()
    {
        recyclerViewDays.setClickable(true);
        recyclerViewDays.setVisibility(View.VISIBLE);
    }

    private void closeDates()
    {
        dateDayOne.setVisibility(View.GONE);
        dateDayTwo.setVisibility(View.GONE);
        dateDayThree.setVisibility(View.GONE);
        dateDayFour.setVisibility(View.GONE);
        dateDayFive.setVisibility(View.GONE);
        dateDaySix.setVisibility(View.GONE);
        dateDaySeven.setVisibility(View.GONE);
    }

    private void setReadyForNewWeak()
    {
        editTextDay.setText("");
        editTextDay.setTextSize(16);
        editTextDay.setVisibility(View.VISIBLE);
        editTextDay.setHint("How much shifts\n per day?");

        editTextMonth.setText("");
        editTextMonth.setTextSize(16);
        editTextMonth.setVisibility(View.VISIBLE);
        editTextMonth.setHint("How much workers\n for each shift?");

        editTextYear.setVisibility(View.GONE);
    }

    private void closeDays()
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
    }

    private void fillUIWithData()
    {
        try
        {
            Weak firstWeak = currentWeak[0];
            firstWeak.FillAllDaysWithShifts(editTextDay.getText().toString(),
                    editTextMonth.getText().toString());

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

            nextWeak.setClickable(true);
            nextWeak.setVisibility(View.VISIBLE);
        }
        catch (Exception ex)
        {
            Toast.makeText(this.getContext(), ex.getMessage(), Toast.LENGTH_LONG).show();
        }
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
            if(loginUser.getRole().equalsIgnoreCase("Admin"))
            {
                showAdminOptionDialog(this.getContext(), shift);
            }
            else
            {
                showOptionDialog(this.getContext(), shift);
            }
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

    private void showAdminOptionDialog(Context v, Shift shift) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle("What you want to do?"); // Set the title

        // Set the options and their click listeners
        builder.setItems(new String[]{"Join shift", "Remove me from shift", "Show who work in this shift"
        , "Change Shift name"}, new DialogInterface.OnClickListener() {
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
                else if(which == 3)
                {
                    shift.setShiftType("Robi -" + shift.getMyId());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        builder.create().show();
    }
}