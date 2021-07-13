package com.segg3.coursemanager.shared.filter;

import java.util.ArrayList;
import java.util.List;

public abstract class Filter <E> {
    public abstract List<E> search(Object query);

    public static List<Object> union (List<Object> group1, List<Object> group2)
    {
        List<Object> tmp = new ArrayList<>();

        tmp.addAll(group1);
        for(int i1 = 0; i1 < group2.size(); i1++)
        {
            if(!tmp.contains(group2))
            {
                tmp.add(group2.get(i1));
            }
        }

        return tmp;
    }

    public static List<Object> intersection(List<Object> group1, List<Object> group2)
    {
        List<Object> tmp = new ArrayList<>();

        List<Object> larger;

        if(group1.size() > group2.size())
        {
            larger = group1;
        }
        else
        {
            larger = group2;
        }

        for(int i1 = 0; i1 < larger.size(); i1++)
        {
            if(group2.contains(larger.get(i1)))
            {
                tmp.add(larger.get(i1));
            }
        }

        return tmp;
    }

    public static List<Object> complement(List<Object> group1, List<Object> surrounding)
    {
        return diffrence(surrounding, group1);
    }

    public static List<Object> diffrence(List<Object> group1, List<Object> group2)
    {
        List<Object> tmp = new ArrayList<>();

        for(int i1 = 0; i1 < group1.size(); i1++)
        {
            if(!group2.contains(group1.get(i1)))
            {
                tmp.add(group1.get(i1));
            }
        }

        return tmp;
    }

    public static List<Object> symmetricDiffrence(List<Object> group1, List<Object> group2)
    {
        List<Object> tmp1 = Filter.diffrence(group1,group2);
        List<Object> tmp2 = Filter.diffrence(group2, group1);

        return Filter.union(tmp1, tmp2);
    }
}
