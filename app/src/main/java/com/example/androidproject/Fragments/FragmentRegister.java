package com.example.androidproject.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.Data.DataBase;
import com.example.androidproject.R;
import com.example.androidproject.Users.User;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentRegister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentRegister() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentRegister.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentRegister newInstance(String param1, String param2) {
        FragmentRegister fragment = new FragmentRegister();
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
        View v = inflater.inflate(R.layout.fragment_register, container, false);
        Button Register = v.findViewById(R.id.buttonRegister);
        TextView title = v.findViewById(R.id.textViewRegisterTitle);
        EditText idText = v.findViewById(R.id.editTextNumberID);
        TextInputLayout firstNameText = v.findViewById(R.id.TextInputLayoutFirstName);
        TextInputLayout lastNameText = v.findViewById(R.id.TextInputLayoutLastName);
        EditText phoneNumberText = v.findViewById(R.id.editTextNumberPhoneNumber);
        EditText emailText = v.findViewById(R.id.editTextTextEmailAddress);
        EditText passwordText = v.findViewById(R.id.editTextTextPassword);
        Boolean red = true;
        final User[] loginUser = new User[1];

        Register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get strings from them:
                String id = idText.getText().toString();
                String firstName = firstNameText.getEditText().getText().toString();
                String lastName = lastNameText.getEditText().getText().toString();
                String phoneNumber = phoneNumberText.getText().toString();
                String email = emailText.getText().toString();
                String password = passwordText.getText().toString();
                Boolean successfulRegister = true;

                try
                {
                    DataBase dataBase = DataBase.getInstance();
                    loginUser[0] = dataBase.RegisterAndLogin(id, dataBase.CreateUser(id, firstName,
                            lastName, phoneNumber, email, password, "User"));
                }
                catch (Exception ex)
                {
                    successfulRegister = false;
                    String msg = ex.getMessage();
                    title.setText(msg);
                    Register.setClickable(false);
                    if(msg.equalsIgnoreCase("That user already exist"))
                    {
                        Register.setText("You now pass to Login page");
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Navigation.findNavController(v).navigate(R.id.action_fragmentRegister_to_fragmentLogin);
                            }
                        }, 3000);
                    }
                    else if(title.getCurrentTextColor() != -65536)
                    {
                        title.setTextColor(Color.RED);
                        Register.setText("Fix according to the instructions in the title and then try register again");
                        Register.setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                // This code will run after 5 seconds
                                Register.setText("Register");
                                Register.setClickable(true);
                            }
                        }, 5000);
                    }
                    else
                    {
                        title.setTextColor(Color.BLUE);
                        Register.setText("Fix according to the instructions in the title and then try register again");
                        Register.setClickable(false);
                        new Handler().postDelayed(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                Register.setText("Register");
                                Register.setClickable(true);
                            }
                        }, 5000);
                    }
                }

                if(successfulRegister)
                {
                    title.setTextColor(Color.GREEN);
                    Register.setText("Success");
                    title.setText("Success");
                    Register.setTextColor(Color.GREEN);
                    Register.setClickable(false);
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
//                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                            ObjectOutputStream oos = null;
                            try
                            {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeObject(loginUser[0]);
                                byte[] serializedData = baos.toByteArray();
                                Bundle bundle = new Bundle();
                                bundle.putByteArray("serializedData", serializedData);
                                Navigation.findNavController(v).navigate(R.id.
                                        action_fragmentRegister_to_fragmentWeeks, bundle);
                            }
                            catch (IOException e)
                            {
                                Toast.makeText(getContext(), "Unexpeted Error, try again", Toast.LENGTH_LONG).show();
                            }

                        }
                    }, 3000);
                }

            }
        });

        return v;
    }
}