//Created by:
// Andrew Bostros cssc0727
// Jan Domingo cssc0749
package edu.sdsu.cs.datastructures;

import java.util.ArrayList;
import java.util.TreeMap;

public class BalancedMap <K extends Comparable<K>,V> implements IMap <K,V> {

    private TreeMap<K, V> treeMap;

    public BalancedMap() {
        treeMap = new TreeMap();
    }

    //Puts each key and value from original into treeMap
    public BalancedMap(IMap<K, V> original) {
        treeMap = new TreeMap<K,V>();
        for (K keys : original.keyset()) {
            V value = original.getValue(keys);
            treeMap.put(keys,value);
        }
    }

    /**
     * Indicates if the map contains the object identified by the key inside.
     *
     * @param key The object to compare against
     * @return true if the parameter object appears in the structure
     */
    public boolean contains(K key) {
        return treeMap.containsKey(key);
    }


    /**
     * Adds the given key/value pair to the dictionary.
     *
     * @param key
     * @param value
     * @return false if the dictionary is full, or if the key is a duplicate.
     * Returns true if addition succeeded.
     */
    public boolean add(K key, V value){
        if (contains(key)) {
            return false;
        } else {
            treeMap.put(key, value);
            return true;
        }
    }


    /**
     * Deletes the key/value pair identified by the key parameter.
     *
     * @param key
     * @return The previous value associated with the deleted key or null if not
     * present.
     */
    public V delete(K key){
        return treeMap.remove(key);
    }


    /**
     * Retrieves, but does not remove, the value associated with the provided
     * key.
     *
     * @param key The key to identify within the map.
     * @return The value associated with the indicated key.
     */
    public V getValue(K key){
        return treeMap.get(key);
    }


    /**
     * Returns a key in the map associated with the provided value.
     *
     * @param value The value to find within the map.
     * @return The first key found associated with the indicated value.
     */
    public K getKey(V value) {
        for (K key : treeMap.keySet()) {
            if (getValue(key).equals(value)) {
                return key; //Only returns once which is the first key found
            }
        }
        return null;
    }


    /**
     * Returns all keys associated with the indicated value contained within the
     * map.
     *
     * @param value The value to locate within the map.
     * @return An iterable object containing all keys associated with the
     * provided value.
     */
    //Enhanced for loop to iterate through keys to return ALL keys with the
    //specified value
    public Iterable<K> getKeys(V value){
        ArrayList<K> keys = new ArrayList<>();
        for (K key : treeMap.keySet()) {
            if (getValue(key).equals(value)) {
                keys.add(key);
            }
        }
        return keys;
    }


    /**
     * Indicates the count of key/value entries stored inside the map.
     *
     * @return A non-negative number representing the number of entries.
     */
    public int size(){
        return treeMap.size();
    }


    /**
     * Indicates if the dictionary contains any items.
     *
     * @return true if the dictionary is empty, false otherwise.
     */
    public boolean isEmpty(){
        return treeMap.isEmpty();
    }


    /***
     * Returns the map to an empty state ready to accept new entries.
     */
    public void clear(){
        treeMap.clear();
    }


    /**
     * Provides an Iterable object of the keys in the dictionary.
     * <p>
     * The keys provided by this method must appear in their natural, ascending,
     * order.
     *
     * @return An iterable set of keys.
     */
    public Iterable<K> keyset(){
        return treeMap.keySet();
    }


    /**
     * Provides an Iterable object of the //keys(values) in the dictionary.
     * <p>
     * The values provided by this method must appear in an order matching the
     * keyset() method. This object may include duplicates if the data structure
     * includes duplicate values.
     *
     * @return An iterable object of all the dictionary's values.
     */
    public Iterable<V> values(){
        return treeMap.values();
    }

}
