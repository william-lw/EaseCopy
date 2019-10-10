package com.william.androidsdk.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {
    public CollectionUtils() {
    }

    public static boolean isNullOrEmpty(Collection list) {
        return list == null || list.size() <= 0;
    }

    public static boolean isNullOrEmpty(Map map) {
        return map == null || map.size() <= 0;
    }


    public static boolean contains(Set<String> set, String value) {
        return set != null && set.size() > 0 ? set.contains(value) : true;
    }

    public static boolean contains(Set<String> source, Collection<String> target) {
        if (source == null && target == null) {
            return true;
        } else if (source != null && target != null) {
            Iterator var2 = target.iterator();

            String string;
            do {
                if (!var2.hasNext()) {
                    return false;
                }

                string = (String) var2.next();
            } while (!source.contains(string));

            return true;
        } else {
            return false;
        }
    }

    public static boolean equals(Set firstSet, Set secondSet) {
        return firstSet != null && secondSet != null ? firstSet.equals(secondSet) : false;
    }

    public static boolean contains(List<String> list, String string) {
        return list == null ? true : list.contains(string);
    }

    public static int getSize(Collection collection) {
        return collection == null ? 0 : collection.size();
    }

    public static boolean safelyContains(Set<String> set, String string) {
        return set != null && set.contains(string);
    }

    public static boolean safelyContains(List<String> list, String string) {
        return list == null ? true : list.contains(string);
    }

    public static void diff(HashSet<String> origin, HashSet<String> target, HashSet<String> diff) {
        Iterator var3 = target.iterator();

        while (var3.hasNext()) {
            String s = (String) var3.next();
            if (!origin.contains(s)) {
                diff.add(s);
            }
        }

    }
}
