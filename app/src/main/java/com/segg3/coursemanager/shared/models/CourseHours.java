package com.segg3.coursemanager.shared.models;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.Duration;
import java.util.Comparator;
import java.util.Date;

import kotlin.collections.IntIterator;

public class CourseHours {

    public final DayOfWeek weekDay;

    public final Time start;

    public final int durations;//minutes

    public int compare(CourseHours courseHours) {

        if(this.weekDay.compareTo(courseHours.weekDay) != 0)
        {
            return this.weekDay.compareTo(courseHours.weekDay);
        }

        Time startTmp1 = start.clone();
        Time startTmp2 = courseHours.start.clone();

        Time endTmp1 = start.clone();
        boolean endTmp1Overflow = endTmp1.addMinute(this.durations);

        Time endTmp2 = courseHours.start.clone();
        boolean endTmp2Overflow = endTmp2.addMinute(courseHours.durations);

        if(endTmp1.compare(startTmp2) == -1)
        {
            return -1;
        }
        else if(endTmp2.compare(startTmp1) == -1)
        {
            return 1;
        }

        return 0;
    }

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

        private boolean addHour(int hour)
        {
            boolean overFlow = this.hour+hour>24;

            this.hour = (this.hour+hour) % 24;

            return overFlow;
        }

        private boolean addMinute(int minute)
        {
            boolean overflow = false;

            if(minute >= 60)
            {
                overflow = addHour(minute/60);
                addMinute(minute%60);
            }
            else
            {
                this.minute = this.minute+minute;
            }

            return overflow;
        }

        public Time clone()
        {
            return new Time(hour,minute);
        }

        @Override
        public String toString() {
            return hour+":"+minute;
        }

        public int compare(Time t) {

            if(t == null)
            {
                throw new NullPointerException();
            }

            if(this.hour < t.hour)
            {
                return -1;
            }
            else if(this.hour > t.hour)
            {
                return 1;
            }

            if(this.minute < t.minute)
            {
                return -1;
            }
            else if(this.minute > t.minute)
            {
                return 1;
            }

            return 0;
        }
    }

    public CourseHours(DayOfWeek weekDay, Time start, Integer durations)
    {
        this.weekDay = weekDay;
        this.start = start;
        this.durations = durations;
    }

    public CourseHours(String CourseHoursRaw)
    {
        String[] tmpParam = CourseHoursRaw.split("|");

        this.weekDay = DayOfWeek.valueOf(tmpParam[0]);

        this.start = new Time(tmpParam[1]);

        Integer duration = new Integer(tmpParam[2]);

        this.durations = duration.intValue();
    }

    @NotNull
    public String toString()
    {
        return weekDay.name() + "|" + start.toString() + "|" + durations;
    }
}
