package com.example.androidproject;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.Assert.*;

import com.example.androidproject.Data.Day;
import com.example.androidproject.Data.Shift;
import com.example.androidproject.Data.Weak;
import com.example.androidproject.Users.User;

import java.time.LocalDateTime;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BackTests
{
    boolean bool = false;
    User[] users = null;
    Shift a = null;
    Day b = null;
    Weak c = null;
    public BackTests()
    {
        UserTest();
        ShiftTest();
        DayTest();

    }

    @Test
    public void InitIfNeed()
    {

    }
    private void UserTest()
    {
        try
        {
            users = new User[5];

            for (int i = 0; i < 4; i++)
            {
                String a = String.format("31524685%d", i);
                users[i] = new User(a, "Robi", "Bar", "0503333424",
                        "1robib1@gmail.com", "hey", "Admin");
            }

            users[4] = new User("316043986", "Robi", "Bar", "0503333424",
                    "1robib1@gmail.com", "hey1", "Admin");
            assertEquals(users[0].CheckIfPasswordIsCorrect("hey"), true);
            assertEquals(users[0].CheckIfPasswordIsCorrect("Hey"), false);
            assertEquals(users[4].CheckIfPasswordIsCorrect("hey1"), true);
        }
        catch (Exception ex)
        {
            assertNull("something fail in test", ex);
        }
    }

    private void ShiftTest()
    {
        try
        {
            a = new Shift("Morning", 3);

            for (int i = 0; i < 3; i++)//full shifts workers, change 3 to 4 if want get exepction
            {
                if (i == 3)
                {
                    System.out.println("now that should drop an exepteion");
                }

                a.JoinToShift(users[i]);
            }

            assertEquals(a.RemoveFromShift(users[1]), true);
            assertEquals(a.RemoveFromShift(users[1]), false);//fasle cos we already removed him.


            a.JoinToShift(users[1]);
        }
        catch (Exception ex)
        {
            assertNull("something fail in test", ex);
        }
    }

    private void DayTest()
    {
        try
        {
            b = new Day("Sunday","1", "1", "2024");
            Day b1 = new Day("Monday", "3", "1", "2024");

            b.CreateShifts(4,3);
            Exception thrown = assertThrows(Exception.class, () -> b.CreateShifts
                            (4,3),
                    "Expected CreateShifts() to throw, becuase cant use " +
                            "that function after use her/CreateSingleShift() ones");
            assertTrue(thrown.getMessage().contains("shifts in this day"));
            b.CreateSingleShift("morning", 6);
            thrown = assertThrows(IllegalArgumentException.class,() -> b.CreateSingleShift
                    ("morning", 4), "Should Throw IllegalArgumentException" +
                    "becuase that name of shift already exist in this day and it should be uniqe");
            assertTrue(thrown.getMessage().contains("In this day have already shift with this name"));

            b.CreateSingleShift("afterNon", 6);
            thrown = assertThrows(Exception.class,() -> b.CreateSingleShift
                    ("afterNon", 6), "Should Throw IllegalArgumentException" +
                    "becuase that name of shift already exist in this day and it should be uniqe");
            assertTrue(thrown.getMessage().contains("This day full of shift's"));

            b1.CreateSingleShift("morning", 5);
            thrown = assertThrows(Exception.class, () -> b1.CreateShifts
                            (3,5),
                    "Expected CreateShifts() to throw, becuase cant use " +
                            "that function after use her/CreateSingleShift() ones");
            assertTrue(thrown.getMessage().contains("shifts in this day"));

            assertEquals("1", b.getShiftByName("1").getShiftType());
            assertEquals(null, b.getShiftByName("5"));//will return null cos no shift with this name.
            b.ChangeShiftName("1", "evening");
            thrown = assertThrows(IllegalArgumentException.class, () -> b.ChangeShiftName
                            ("1", "8"),"Expected to throw ex," +
                    " becuase no shift with that old name in this day");
            assertTrue(thrown.getMessage().contains("There no shift with that old name in this day"));
            //d = b.ChangeShiftName("2", "evening");//In this day have already shift with this new name
            thrown = assertThrows(IllegalArgumentException.class, () -> b.ChangeShiftName
                    ("2", "evening"),"Expected to throw ex," +
                    " becuase In this day have already shift with this new name");
            assertTrue(thrown.getMessage().contains("In this day have already shift with this new name"));
        }
        catch (Exception ex)
        {}
    }

    private void WeakTest()
    {
        try
        {
//            Exception thrown = assertThrows(Exception.class, () -> b.CreateShifts
//                            (4,3),
//                    "Expected CreateShifts() to throw, becuase cant use " +
//                            "that function after use her/CreateSingleShift() ones");
//            assertTrue(thrown.getMessage().contains("shifts in this day"));
            b.CreateSingleShift("morning", 6);
            c = new Weak("31", "2", "2024");//drop ex - cos no day 31 in month 2.

            c = new Weak("4", "1", "2024");
            Day b1 = c.getASpecificDay(2);
            c.FillAllDaysWithShifts(5, 7);
            c.CleanShiftsFromAllDays();
            //create next weak after we have 1+ weaks:
            LocalDateTime l = c.getLastDay().plusDays(1);
            c = null;
            c = new Weak(String.format("%d", l.getDayOfMonth()),String.format("%d", l.getMonthValue())
                    ,String.format("%d", l.getYear()));
            //end create next weak
            c.getASpecificDay(1).CreateSingleShift("Morning", 5);
            c.FillAllDaysWithShifts(3, 14);//will delete morning shift and fill.
        }
        catch (Exception ex)
        {
            String bl = ex.getMessage();
            int g = 1;
        }
    }
}