package com.example.androidproject.Data;

import com.example.androidproject.Users.User;

import java.util.ArrayList;

public class Shift {
    private static double id = 0;
    private final double myId;
    private String shiftType;
    private int numberOfWorkersNeedMoreToShift;
    private ArrayList<User> workersInShiftList;
    private static int maxWorkersNumberForOneShift = 128;

    public static int getMaxWorkersNumberForOneShift() {
        return maxWorkersNumberForOneShift;
    }

    public void setShiftType(String shiftType) throws NullPointerException
    {
        CheckShiftNameIsValid(shiftType);
        this.shiftType = shiftType;
    }

    public void setNumberOfWorkersNeedMoreToShift(int numberOfWorkersNeedMoreToShift) throws Exception
    {
        CheckNumberOfWorkersNeedMoreToShift(numberOfWorkersNeedMoreToShift);
        this.numberOfWorkersNeedMoreToShift = numberOfWorkersNeedMoreToShift;
    }

    private void CheckNumberOfWorkersNeedMoreToShift(int numberOfWorkersNeedMoreToShift) throws Exception
    {
        if (numberOfWorkersNeedMoreToShift < 0 || numberOfWorkersNeedMoreToShift > getMaxWorkersNumberForOneShift())
        {
            throw new Exception(String.format("The number of worker's for shift most be in range of: 0 to {0}"
                    , getMaxWorkersNumberForOneShift()));
        }
    }

    public Shift(String shiftType, int numberOfWorkersCanWorkOnTheShift)
            throws NullPointerException, Exception
    {
        setShiftType(shiftType);
        setNumberOfWorkersNeedMoreToShift(numberOfWorkersCanWorkOnTheShift);
        workersInShiftList = new ArrayList<User>(numberOfWorkersCanWorkOnTheShift);
        myId = id;
        id++;
    }

    public double getMyId()
    {
        return myId;
    }
    public double getId() {
        return id;
    }

    public String getShiftType() {
        return shiftType;
    }

    public int getNumberOfWorkersNeedMoreToShift() {
        return numberOfWorkersNeedMoreToShift;
    }

    public ArrayList<User> getWorkersInShiftList() {
        return workersInShiftList;
    }

    public void setWorkersInShiftList(ArrayList<User> workersInShiftList) {
        this.workersInShiftList = workersInShiftList;
    }

    private void setOneUserToArray(User user)
    {
        getWorkersInShiftList().add(user);
    }

    private boolean removeOneUserFromArray(User user)
    {
        return getWorkersInShiftList().remove(user);
    }

    private void CheckShiftNameIsValid(String name) throws NullPointerException
    {
        if(name == null || name.trim().isEmpty())
        {
            throw new NullPointerException("You must enter name for shift");
        }
    }

    public synchronized boolean JoinToShift(User user) throws Exception
    {
        boolean userAddSeccsefuly = false;
        int checkIfAddUser = getNumberOfWorkersNeedMoreToShift();

        if(checkIfAddUser < 1)
        {
            throw new Exception("The shift is full, cant add anymore workres");
        }
        else
        {
            for (User user1: getWorkersInShiftList())
            {
                if(user.getId() == user1.getId())
                {
                    throw new Exception("This user already work in this shift");
                }
            }

            setOneUserToArray(user);
            setNumberOfWorkersNeedMoreToShift(checkIfAddUser - 1);
            userAddSeccsefuly = true;
        }

        if(userAddSeccsefuly)
        {
            user.setNumberOfShiftsWorkedThatMonth(user.getNumberOfShiftsWorkedThatMonth() + 1);
        }

        return userAddSeccsefuly;
    }

    public synchronized boolean RemoveFromShift(User user) throws Exception
    {
        boolean userRemoveSeccsefuly = removeOneUserFromArray(user);

        if(userRemoveSeccsefuly)
        {
            setNumberOfWorkersNeedMoreToShift(getNumberOfWorkersNeedMoreToShift() + 1);
            user.setNumberOfShiftsWorkedThatMonth(user.getNumberOfShiftsWorkedThatMonth() - 1);
        }

        return userRemoveSeccsefuly;
    }
}
