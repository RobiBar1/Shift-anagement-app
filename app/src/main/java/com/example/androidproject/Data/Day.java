package com.example.androidproject.Data;

public class Day
{
    private static double id = 0;
    private String name;
    private String date;
    private int NumberOfShitsInThisDay = 0;
    private int MaxShitsNumberOnThisDay = 6;
    private Shift[] shiftsToday = null;

    public Day(String Name, String Day, String Month, String Year)
            throws NumberFormatException, Exception {
        setName(Name);
        setDate(CheckDate(Day, Month, Year));
        id++;
    }

    public int getMaxShitsNumberOnThisDay() {
        return MaxShitsNumberOnThisDay;
    }

    public int getNumberOfShitsInThisDay() {
        return NumberOfShitsInThisDay;
    }

    public void setNumberOfShitsInThisDay(int numberOfShitsInThisDay) {
        NumberOfShitsInThisDay = numberOfShitsInThisDay;
    }

    public void CreateShifts(int numberOfShiftsToAdd, int NumberOfWorkersCanWorkOnEachShift)
            throws IllegalArgumentException, Exception
    {
        if(getNumberOfShitsInThisDay() == 0)
        {
            if(numberOfShiftsToAdd > MaxShitsNumberOnThisDay || numberOfShiftsToAdd < 1)
            {
                throw new IllegalArgumentException(
                        String.format("The max number of shifts for same day most be in range of: 1 to {0}"
                                , MaxShitsNumberOnThisDay));
            }

            shiftsToday = new Shift[getMaxShitsNumberOnThisDay()];

            setNumberOfShitsInThisDay(numberOfShiftsToAdd);
            for (int i = 0; i < numberOfShiftsToAdd; i++)
            {
                shiftsToday[i] = new Shift(String.format("%d", i), NumberOfWorkersCanWorkOnEachShift);
                setNumberOfShitsInThisDay(i + 1);
            }
        }
        else
        {
            throw new Exception(String.format("There already %d shifts in this day, if you you want add more shifts," +
                    " add it 1 by 1", getNumberOfShitsInThisDay()));
        }
    }

    public void CreateSingleShift(String name, int NumberOfWorkersCanWorkOnEachShift)
            throws NullPointerException, IllegalArgumentException, Exception
    {
        if(getNumberOfShitsInThisDay() == getMaxShitsNumberOnThisDay())
        {
            throw new Exception(String.format("This day full of shift's"));
        }
        else if(getNumberOfShitsInThisDay() == 0)
        {
            shiftsToday = new Shift[getMaxShitsNumberOnThisDay()];
        }
        else
        {
            String oldName;
            cheackThatDoesntExistShiftWithSameName(name);
        }

        int newShiftsNumber = getNumberOfShitsInThisDay();
        shiftsToday[newShiftsNumber] = new Shift(name, NumberOfWorkersCanWorkOnEachShift);
        setNumberOfShitsInThisDay(newShiftsNumber + 1);
    }

    private void cheackThatDoesntExistShiftWithSameName(String name) throws IllegalArgumentException
    {
        String oldName;
        for (Shift shift: getShiftsToday())
        {
            if (shift != null)
            {
                oldName = shift.getShiftType();

                if (oldName.equalsIgnoreCase(name))
                {
                    throw new IllegalArgumentException("In this day have already shift with this name");
                }
            }
        }
    }

    public void ChangeShiftName(String fromName, String newName) throws
            NullPointerException, IllegalArgumentException
    {
        boolean changeNameSeccses = false;
        Shift changeNameShift = null;
        String oldName;

        for (Shift shift: getShiftsToday())
        {
            if(shift != null)
            {
                oldName = shift.getShiftType();

                if(oldName.equalsIgnoreCase(newName))
                {
                    throw new IllegalArgumentException("In this day have already shift with this new name");
                }

                if(!changeNameSeccses && oldName.equalsIgnoreCase(fromName))
                {
                    changeNameShift = shift;
                    changeNameSeccses = true;
                }
            }
        }

        if(changeNameSeccses)
        {
            changeNameShift.setShiftType(newName);
        }
        else
        {
            throw new IllegalArgumentException("There no shift with that old name in this day");
        }
    }

    public static int checkDayMember(String day) throws NumberFormatException
    {
        return Integer.parseInt(day);
    }

    public static int checkMonth(String month) throws NumberFormatException
    {
        return Integer.parseInt(month);
    }

    public static int checkYear(String year) throws NumberFormatException
    {
        return Integer.parseInt(year);
    }
    public static String CheckDate(String Day, String Month, String Year) throws NumberFormatException
    {
        try
        {
            checkDayMember(Day);
        }
        catch (Exception NumberFormatException)
        {
            throw new NumberFormatException("The day most be a number");
        }

        try
        {
            checkMonth(Month);
        }
        catch (Exception NumberFormatException)
        {
            throw new NumberFormatException("The month most be a number");
        }

        try
        {
            checkYear(Year);
        }
        catch (Exception NumberFormatException)
        {
            throw new NumberFormatException("The year most be a number");
        }

        return String.format("%1$s/%2$s/%3$s" ,Day, Month, Year);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setShiftsToday(Shift[] shiftsToday) {
        this.shiftsToday = shiftsToday;
    }

    public static double getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Shift[] getShiftsToday()
    {
        return shiftsToday;
    }

    public Shift getShiftByName(String name) throws IllegalArgumentException
    {
        Shift returnShift = null;

        if(getNumberOfShitsInThisDay() == 0)
        {
            throw new IllegalArgumentException("There no shifts apply for this day");
        }

        for (Shift shift: getShiftsToday())
        {
            if(shift != null && name.equalsIgnoreCase(shift.getShiftType()))
            {
                returnShift = shift;
                break;
            }
        }

        return returnShift;
    }

    public void CleanAllShiftsFromThatDay()
    {
        setShiftsToday(null);
        setNumberOfShitsInThisDay(0);
    }
}
