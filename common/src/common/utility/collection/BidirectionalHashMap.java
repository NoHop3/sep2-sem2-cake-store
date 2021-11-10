package common.utility.collection;

import java.util.HashMap;

public class BidirectionalHashMap<K, V> extends HashMap<K, V> {

    private HashMap<V, K> inverseMap;

    public BidirectionalHashMap() {
        super();
        inverseMap = new HashMap<>();
    }

    public V getValue(K key) {
        return super.get(key);
    }

    public K getKey(V value) {
        return inverseMap.get(value);
    }

    @Override
    public int size() {
        return super.size();
    }

    @Override
    public boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    public V remove(Object key) {
        V value = super.remove(key);
        inverseMap.remove(value);
        return value;
    }

    @Override
    public V put(K key, V value) {
        inverseMap.put(value, key);
        return super.put(key, value);
    }
}
