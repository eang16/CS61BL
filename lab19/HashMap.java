
import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> implements Map61BL<K, V> {

    /* Instance variables here? */
    //String[] keyArray;
    LinkedList[] keyArray;
    int size;
    int capacity;
    double load = 0.75;

    /* Constructor */
    HashMap() {
        //keyArray = new T[26];
        keyArray = new LinkedList[16];
        for (int i = 0; i < keyArray.length; i++) {
            keyArray[i] = new LinkedList<Entry>();
        }
        size = 0;
        capacity = 16;
    }

    HashMap(int initialCapacity) {
        //keyArray = new T[26];
        keyArray = new LinkedList[initialCapacity];
        for (int i = 0; i < keyArray.length; i++) {
            keyArray[i] = new LinkedList<Entry>();
        }
        size = 0;
        capacity = initialCapacity;
    }

    HashMap(int initialCapacity, float loadFactor) {
        //keyArray = new T[26];
        keyArray = new LinkedList[initialCapacity];
        for (int i = 0; i < keyArray.length; i++) {
            keyArray[i] = new LinkedList<Entry>();
        }
        size = 0;
        capacity = initialCapacity;
        load = loadFactor;
    }

    /* Returns true if the given KEY is a valid name that starts with A - Z. */
//    private static boolean isValidName(K key) {
//        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
//    }


    int hash(K key) {
        return Math.floorMod(key.hashCode(), keyArray.length);
        //for(int i = 0; i<key.length(); i++){
        //    c = c + (10*i)*(key.charAt(i));
        //}
    }

    public void clear() {
        keyArray = new LinkedList[capacity];
        for (int i = 0; i < keyArray.length; i++) {
            keyArray[i] = new LinkedList<Entry>();
        }
        size = 0;
    }


    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
        //if (isValidName(key)) {
        LinkedList<Entry> t = keyArray[hash(key)];
        if (t == null) {
            return false;
        } else {
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i).key.equals(key)) {
                    return true;
                }
            }
            return false;
        }
        //}
        //return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public V get(K key) {
        //if (isValidName(key)) {
        LinkedList<Entry> t = keyArray[hash(key)];
        if (containsKey(key)) {
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i).key.equals(key)) {
                    return (V) t.get(i).value;
                }
            }
            return null;
        }
        //}
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(K key, V value) {
        //if (isValidName(key)) {
        LinkedList<Entry> t = keyArray[hash(key)];
        int code = hash(key);

        if (containsKey(key)) {
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i).key.equals(key)) {
                    t.get(i).value = value;
                }
            }
        } else {
            Entry n = new Entry(key, value);
            t.add(n);
            size++;
        }

        if (((double) size / ((double) keyArray.length)) > load) {
            resize();
        }


        //}
    }

    void resize() {
        int l = keyArray.length;
        LinkedList[] newKey = new LinkedList[keyArray.length * 2];
        System.arraycopy(keyArray, 0, newKey, 0, keyArray.length);
        keyArray = newKey;
        for (int i = l; i < keyArray.length; i++) {
            keyArray[i] = new LinkedList<Entry>();
        }
        capacity = capacity * 2;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public V remove(K key) {
        //if (isValidName(key)) {
        if (!containsKey(key)) {
            return null;
        }
        //String rem = keyArray[hash(key)];
        //keyArray[hash(key)] = null;
        //return rem;
        V rem = null;
        LinkedList<Entry> t = keyArray[hash(key)];
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i).key.equals(key)) {
                rem = (V) t.get(i).value;
                t.remove(t.get(i));
                size--;
            }
        }
        return rem;

        //}
        //return null;
    }

    public boolean remove(K key, V value) {
        //if (isValidName(key)) {
        if (!containsKey(key)) {
            return false;
        }
        //String rem = keyArray[hash(key)];
        //keyArray[hash(key)] = null;
        //return rem;
        LinkedList<Entry> t = keyArray[hash(key)];
        Entry<K, V> n = new Entry<>(key, value);
        for (int i = 0; i < t.size(); i++) {
            if (t.get(i).equals(n)) {
                t.remove(t.get(i));
                size--;
                return true;
            }
        }
        return false;

        //}
        //return null;
    }

    public int size() {
        //double x = size;
        return size;
    }

    int capacity() {
        return capacity;
    }

    public Iterator<K> iterator() {
        return new HashIterator();
    }


    private class HashIterator implements Iterator<K> {

        private int kCount = 0;
        private int vCount = 0;

        public K next() {
            if (hasNext()) {
                if (keyArray[kCount] != null) {
                    if (vCount < keyArray[kCount].size()) {
                        vCount++;
                        Entry<K, V> n = (Entry) keyArray[kCount].get(vCount - 1);
                        return n.key;
                    } else {
                        vCount = 0;
                        kCount++;
                        return next();
                    }
                }

            }
            return null;
        }

        public boolean hasNext() {
            return kCount < size();
        }
    }


    static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }
}
