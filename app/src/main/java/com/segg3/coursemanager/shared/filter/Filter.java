package com.segg3.coursemanager.shared.filter;

import java.util.ArrayList;
import java.util.List;

public abstract class Filter <E> {
    public abstract List<E> search(Object query, List<E> unfiltered);

    public List<E> union (List<E> a, List<E> b)
    {
        List<E> tmp = new ArrayList<>();

        tmp.addAll(a);
        for(int i1 = 0; i1 < b.size(); i1++)
        {
            if(!tmp.contains(b.get(i1)))
            {
                tmp.add(b.get(i1));
            }
        }

        return tmp;
    }

    public List<E> intersection(List<E> a, List<E> b)
    {
        List<E> tmp = new ArrayList<>();

        List<E> larger;

        if(a.size() > b.size())
        {
            larger = a;
        }
        else
        {
            larger = b;
        }

        for(int i1 = 0; i1 < larger.size(); i1++)
        {
            if(b.contains(larger.get(i1)))
            {
                tmp.add(larger.get(i1));
            }
        }

        return tmp;
    }

    public List<E> complement(List<E> a, List<E> surrounding)
    {
        return diffrence(surrounding, a);
    }

    public List<E> diffrence(List<E> a, List<E> b)
    {
        List<E> tmp = new ArrayList<>();

        for(int i1 = 0; i1 < a.size(); i1++)
        {
            if(!b.contains(a.get(i1)))
            {
                tmp.add(a.get(i1));
            }
        }

        return tmp;
    }

    public List<E> symmetricDiffrence(List<E> group1, List<E> group2)
    {
        List<E> tmp1 = this.diffrence(group1,group2);
        List<E> tmp2 = this.diffrence(group2, group1);

        return this.union(tmp1, tmp2);
    }
}
