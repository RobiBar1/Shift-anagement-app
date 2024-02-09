package com.example.androidproject.Data;

import java.util.ArrayList;
import java.time.LocalDateTime;

public class Weak
{
    private static double id = 0;
    private ArrayList<Day> days = new ArrayList<Day>(7);
    private LocalDateTime lastDay = null;

    private void setLastDay(LocalDateTime lastDay) {
        this.lastDay = lastDay;
    }

    public static double getId() {
        return id;
    }

    public ArrayList<Day> getDays() {
        return days;
    }

    public Weak(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        createDays(day, month, year);
        id++;
    }

    private void createDays(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            setLastDay(LocalDateTime.of(Day.checkYear(year), Day.checkMonth(month),//months represent by 0-11.
                    Day.checkDayMember(day), 0, 0));
            createSunday(day, month, year);
            setLastDay(getLastDay().plusDays(1));
            createMonday(String.format("%d", getLastDay().getDayOfMonth()), String.format("%d", getLastDay().getMonthValue()),
                    String.format("%d", getLastDay().getYear()));
            setLastDay(getLastDay().plusDays(1));
            createTuesday(String.format("%d", getLastDay().getDayOfMonth()), String.format("%d", getLastDay().getMonthValue()),
                    String.format("%d", getLastDay().getYear()));
            setLastDay(getLastDay().plusDays(1));
            createWednesday(String.format("%d", getLastDay().getDayOfMonth()), String.format("%d", getLastDay().getMonthValue()),
                    String.format("%d", getLastDay().getYear()));
            setLastDay(getLastDay().plusDays(1));
            createThursday(String.format("%d", getLastDay().getDayOfMonth()), String.format("%d", getLastDay().getMonthValue()),
                    String.format("%d", getLastDay().getYear()));
            setLastDay(getLastDay().plusDays(1));
            createFriday(String.format("%d", getLastDay().getDayOfMonth()), String.format("%d", getLastDay().getMonthValue()),
                    String.format("%d", getLastDay().getYear()));
            setLastDay(getLastDay().plusDays(1));
            createSaturday(String.format("%d", getLastDay().getDayOfMonth()), String.format("%d", getLastDay().getMonthValue()),
                    String.format("%d", getLastDay().getYear()));
        }
        else
        {
            throw new Exception("Fall in Anroid if in Weak class");
        }
    }

    private void createSunday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Sunday", day, month, year));
    }

    private void createMonday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Monday", day, month, year));
    }

    private void createTuesday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Tuesday", day, month, year));
    }

    private void createWednesday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Wednesday", day, month, year));
    }

    private void createThursday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Thursday", day, month, year));
    }

    private void createFriday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Friday", day, month, year));
    }

    private void createSaturday(String day, String month, String year)
            throws NumberFormatException, Exception
    {
        getDays().add(new Day("Saturday", day, month, year));
    }

    public Day getASpecificDay(int numberOfDayYouWant)
    {
        if(numberOfDayYouWant < 1 || numberOfDayYouWant > 7)
        {
            throw new IllegalArgumentException("The number of day most be between 1 - 7");
        }

        return getDays().get(numberOfDayYouWant - 1);
    }

    public LocalDateTime getLastDay()
    {
        return lastDay;
    }

    public void FillAllDaysWithShifts(int numberOfShiftsToAddPerDay, int numberOfWorkersCanWorkOnEachShift )
    throws IllegalArgumentException, Exception//if was shifts in any day they will remove.
    {
        for (Day day: getDays())
        {
            if(day.getNumberOfShitsInThisDay() > 0)
            {
                day.CleanAllShiftsFromThatDay();
            }

            day.CreateShifts(numberOfShiftsToAddPerDay, numberOfWorkersCanWorkOnEachShift);
        }
    }

    public void CleanShiftsFromAllDays()
    {
        for (Day day: getDays())
        {
            day.CleanAllShiftsFromThatDay();
        }
    }
}
