package com.segg3.coursemanager.shared.models;

import com.google.common.base.Objects;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.util.List;

public class CourseHours implements Comparable<CourseHours> {

    public final DayOfWeek weekDay;

    public final Time start;

    public final int durations;//minutes

    public CourseHours(DayOfWeek weekDay, Time start, Integer durations) {
        this.weekDay = weekDay;
        this.start = start;
        this.durations = durations;
    }

    public CourseHours(int weekDay, int startHour, int startMinute, int duration) {
        this.weekDay = DayOfWeek.of(weekDay);
        this.start = new Time(startHour, startMinute);
        this.durations = duration;
    }

    public CourseHours(String courseHoursRaw) {
        String[] tmpParam = courseHoursRaw.split("\\|");

        this.weekDay = DayOfWeek.valueOf(tmpParam[0]);
        this.start = new Time(tmpParam[1]);
        this.durations = Integer.parseInt(tmpParam[2]);
    }

    public static boolean courseHoursIntersect(CourseHours course, List<CourseHours> otherCourses) {
        for (CourseHours courseHour : otherCourses) {
            if (course.compareTo(courseHour) == 0) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(weekDay, start, durations);
    }

    @Override
    public int compareTo(CourseHours courseHours) {


        if (this.weekDay.compareTo(courseHours.weekDay) != 0) {
            return Math.max(-1, Math.min(1, this.weekDay.compareTo(courseHours.weekDay)));
        }

        Time startTmp1 = start.clone();
        Time startTmp2 = courseHours.start.clone();

        Time endTmp1 = start.clone();
        boolean endTmp1Overflow = endTmp1.addMinute(this.durations);

        Time endTmp2 = courseHours.start.clone();
        boolean endTmp2Overflow = endTmp2.addMinute(courseHours.durations);

        if (endTmp1.compare(startTmp2) == -1) {
            return -1;
        } else if (endTmp2.compare(startTmp1) == -1) {
            return 1;
        }

        return 0;
    }

    @NotNull
    public String toString() {
        return weekDay.name() + "|" + start.toString() + "|" + durations;
    }

    public static class Time {
        public int hour;
        public int minute;

        public Time(int hour, int minute) {
            setHour(hour);
            setMinute(minute);
        }

        public Time(String timeRaw) {
            String[] tmp = timeRaw.split("\\:");


            setHour(Integer.parseInt(tmp[0]));
            setMinute(Integer.parseInt(tmp[1]));
        }

        public void setHour(int hour) {
            if (0 <= hour && hour < 24) {
                this.hour = hour;
            } else {
                throw new IllegalArgumentException("Illegal hour value. 0 <= hour < 24");
            }
        }

        public void setMinute(int minute) {
            if (0 <= minute && minute < 60) {
                this.minute = minute;
            } else {
                throw new IllegalArgumentException("Illegal hour value. 0 <= hour < 24");
            }
        }

        public boolean addHour(int hour) {
            boolean overFlow = this.hour + hour >= 24;

            this.hour = (this.hour + hour) % 24;

            return overFlow;
        }

        public boolean addMinute(int minute) {
            boolean overflow = false;

            if (this.minute + minute >= 60) {
                overflow = addHour((this.minute + minute) / 60);
                this.minute = (this.minute + minute) % 60;
            } else {
                this.minute = this.minute + minute;
            }

            return overflow;
        }

        public Time clone() {
            return new Time(hour, minute);
        }

        @Override
        public String toString() {
            if (minute < 10)
                return hour + ":0" + minute;
            else
                return hour + ":" + minute;
        }

        public int compare(Time t) {

            if (t == null) {
                throw new NullPointerException();
            }

            if (this.hour < t.hour) {
                return -1;
            } else if (this.hour > t.hour) {
                return 1;
            }

            if (this.minute < t.minute) {
                return -1;
            } else if (this.minute > t.minute) {
                return 1;
            }

            return 0;
        }
    }
}
