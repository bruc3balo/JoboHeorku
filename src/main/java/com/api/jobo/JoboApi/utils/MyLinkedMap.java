package com.api.jobo.JoboApi.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class MyLinkedMap<K,V> extends LinkedHashMap<K, V> {

    public V getValue(int i) {

        Map.Entry<K, V> entry = this.getEntry(i);
        if(entry == null) return null;

        return entry.getValue();
    }

    public K getKey(int i) {

        Map.Entry<K, V>entry = this.getEntry(i);
        if(entry == null) return null;

        return entry.getKey();
    }

    public void setValue(int i, V value) {

        Map.Entry<K, V>entry = this.getEntry(i);
        entry.setValue(value);
        System.out.println(entry.getKey()+ " value set to "+value.toString());
    }



    public Map.Entry<K, V> getEntry(int i) {
        // check if negetive index provided
        Set<Map.Entry<K,V>> entries = entrySet();
        int j = 0;

        for(Map.Entry<K, V>entry : entries)
            if(j++ == i)return entry;

        return null;

    }

}
