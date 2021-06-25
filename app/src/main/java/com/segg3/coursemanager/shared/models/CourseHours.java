package com.segg3.coursemanager.shared.models;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.Date;

import kotlin.collections.IntIterator;

public class CourseHours {

    public final DayOfWeek weekDay;

    public final Time start;

    public final float durations;

    public class Time
    {
        int hour;
        int minute;

        public Time(int hour, int minute)
        {
            setHour(hour);
            setMinute(minute);
        }

        public Time(String timeRaw)
        {
            String[] tmp = timeRaw.split(":");

            Integer hourRaw = new Integer(tmp[0]);
            Integer minuteRaw = new Integer(tmp[1]);

            setHour(hourRaw.intValue());
            setMinute(minuteRaw.intValue());
        }

        private void setHour(int hour)
        {
            if(0 <= hour && hour < 24)
            {
                this.hour = hour;
            }
            else
            {
                throw new IllegalArgumentException("Illegal hour value. 0 <= hour < 24");
            }
        }

        private void setMinute(int minute)
        {
            if(0 <= minute && minute < 60)
            {
                this.minute = minute;
            }
            else
            {
                throw new IllegalArgumentException("Illegal hour value. 0 <= hour < 24");
            }
        }

        @Override
        public String toString() {
            return hour+":"+minute;
        }
    }

    public CourseHours(DayOfWeek weekDay, Time start, float durations)
    {
        this.weekDay = weekDay;
        this.start = start;
        this. durations = durations;
    }

    public CourseHours(String CourseHoursRaw)
    {
        String[] tmpParam = CourseHoursRaw.split("|");

        this.weekDay = DayOfWeek.valueOf(tmpParam[0]);

        this.start = new Time(tmpParam[1]);

        Float duration = new Float(tmpParam[2]);

        this.durations = duration.floatValue();
    }

    @NotNull
    public String toString()
    {
        return weekDay.name() + "|" + start.toString() + "|" + durations;
    }
}
