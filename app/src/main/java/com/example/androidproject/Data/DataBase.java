package com.example.androidproject.Data;

import com.example.androidproject.Users.User;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class DataBase
{
    private static DataBase singleInstance = null;
    private static int id = 0;
    private String companyName;
    private String placeAddress;
    private HashMap<String, User> usersList = null;
    private ArrayList<Weak> finishedWeaks = null;// 2 way link list
    private ArrayList<Weak> nextWeaks = null;

    public ArrayList<Weak> getFinishedWeaks() {
        return finishedWeaks;
    }

    public void setFinishedWeaks(ArrayList<Weak> finishedWeaks) {
        this.finishedWeaks = finishedWeaks;
    }

    public ArrayList<Weak> getNextWeaks() {
        return nextWeaks;
    }

    public void setNextWeaks(ArrayList<Weak> nextWeaks) {
        this.nextWeaks = nextWeaks;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setPlaceAddress(String placeAddress) {
        this.placeAddress = placeAddress;
    }

    public static int getId() {
        return id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    private DataBase()
    {
        id++;
        setPlaceAddress("1");
        setCompanyName("Enter name");
    }

    public HashMap<String, User> getUsersHashMap() {
        return usersList;
    }

    public void setUsersHashMap(HashMap<String, User> usersList) {
        this.usersList = usersList;
    }

    public static synchronized DataBase getInstance()
    {
        if (singleInstance == null)
        {
            singleInstance = new DataBase();
        }

        return singleInstance;
    }

    public User RegisterAndLogin(String id, User usr) throws IllegalArgumentException
    {
        if(getUsersHashMap() == null)
        {
            setUsersHashMap(new HashMap<String, User>());
            getUsersHashMap().put(id, usr);
        }
        else
        {
            getUsersHashMap().forEach((k, v) ->
            {
                if(k.equalsIgnoreCase(id))
                {
                    throw new IllegalArgumentException("That user already exist");
                }
                if(v.getEmail().equalsIgnoreCase(usr.getEmail()))
                {
                    throw new IllegalArgumentException("That user email already in use");
                }
                if(v.getPhoneNumber().equalsIgnoreCase(usr.getPhoneNumber()))
                {
                    throw new IllegalArgumentException("That user phone already in use");
                }
            });

            getUsersHashMap().put(id, usr);
        }

        return usr;
    }

    public User LoginWithId(String id, String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException, Exception
    {
        User usr = null;
        boolean checkIfRegister = false;

        if(getUsersHashMap() == null)
        {
            throw new Exception("Try register before login");
        }

        User.CheckId(id);
        User.CheckPassword(password);

        usr = getUsersHashMap().get(id);

        if(usr != null)
        {
            if(!usr.CheckIfPasswordIsCorrect(password))
            {
                throw new Exception("You enter wrong password");
            }
        }
        else
        {
            throw new Exception("Try register before login");
        }

        return usr;
    }

    public User CreateUser(String id, String firstName, String lastName,
                           String phoneNumber, String email, String password, String role)
            throws NumberFormatException, Exception
    {
        return new User(id, firstName, lastName, phoneNumber, email, password, role);
    }

    public Weak CreateFirstWeakAndGetIt(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        Weak weakToReturn = null;

        if(getNextWeaks() == null)
        {
            weakToReturn = new Weak(day, month, year);
            addNewWeak(weakToReturn);
        }
        else
        {
            throw new IllegalArgumentException("There already weaks in" +
                    " system use function to create next weak");
        }


        return weakToReturn;
    }

    public Weak CreateNextWeakAndGetIt() throws
            IllegalArgumentException, NumberFormatException, Exception
    {
        Weak weakToReturn = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            LocalDateTime firstDayOfNewWak = GetLastWeakExist().getLastDay().plusDays(1);
            weakToReturn = new Weak(String.format("%d", firstDayOfNewWak.getDayOfMonth()),
                    String.format("%d", firstDayOfNewWak.getMonthValue()),
                    String.format("%d", firstDayOfNewWak.getYear()));
            addNewWeak(weakToReturn);
        }

        return weakToReturn;
    }

    public Weak GetLastWeakExist() throws IllegalArgumentException
    {
        Weak weakToReturn = null;

        if(getNextWeaks() == null)
        {
            throw new IllegalArgumentException("There no weaks in system, " +
                    "use function to create first weak");
        }
        else
        {
           weakToReturn = getNextWeaks().get(getNextWeaks().size() - 1);
        }

        return weakToReturn;
    }


    private void addNewWeak(Weak weak)
    {
        getNextWeaks().add(weak);
    }
}
