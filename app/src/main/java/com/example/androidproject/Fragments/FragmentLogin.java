package com.example.androidproject.Fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidproject.Data.DataBase;
import com.example.androidproject.R;
import com.example.androidproject.Users.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentLogin#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentLogin extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private DataBase dataBase = DataBase.getInstance();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentLogin() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentLogin.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentLogin newInstance(String param1, String param2) {
        FragmentLogin fragment = new FragmentLogin();
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
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        Button login = v.findViewById(R.id.buttonLogin);
        EditText editTextID = v.findViewById(R.id.EditTextIDLoginPage);
        EditText editTextPassword = v.findViewById(R.id.EditTextPasswordLoginPage);
        final User[] loginUser = new User[1];

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String id = editTextID.getText().toString();
                String password = editTextPassword.getText().toString();
                Boolean successLogin = true;

                try
                {
                    DataBase dataBase = DataBase.getInstance();
                    dataBase.CheckID(id);
                    dataBase.CheckPassword(password);
                    loginUser[0] = dataBase.LoginWithId(id, password);
                }
                catch (Exception ex)
                {
                    successLogin = false;
                    login.setClickable(false);
                    login.setTextColor(Color.RED);
                    Toast.makeText(v.getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            login.setClickable(true);
                            login.setTextColor(Color.WHITE);
                        }
                    }, 4000);
                }

                if(successLogin)
                {
                    login.setClickable(false);
                    login.setText("Success");
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
            }
        });


        return v;
    }
}