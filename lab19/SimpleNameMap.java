
import java.util.LinkedList;

public class SimpleNameMap {

    /* Instance variables here? */
    //String[] keyArray;
    LinkedList[] keyArray;

    /* Constructor */
    SimpleNameMap() {
        //keyArray = new T[26];
        LinkedList[] keyArray = (LinkedList[]) new Object[10];
        for (int i = 0; i < keyArray.length; i++) {
            keyArray[i] = new LinkedList<Entry>();
        }
    }

    /* Returns true if the given KEY is a valid name that starts with A - Z. */
    private static boolean isValidName(String key) {
        return 'A' <= key.charAt(0) && key.charAt(0) <= 'Z';
    }


    int hash(String key) {
        return Math.floorMod((key.charAt(0) - 'A'), keyArray.length);
        //for(int i = 0; i<key.length(); i++){
        //    c = c + (10*i)*(key.charAt(i));
        //}
    }

    /* Returns true if the map contains the KEY. */
    boolean containsKey(String key) {
        if (isValidName(key)) {
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
//                if(t.contains(key)){
//                    return true;
//                }
//                return false;
            }
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    String get(String key) {
        if (isValidName(key)) {
            LinkedList<Entry> t = keyArray[hash(key)];
            if (containsKey(key)) {
                for (int i = 0; i < t.size(); i++) {
                    if (t.get(i).key.equals(key)) {
                        return t.get(i).key;
                    }
                }
                return null;
            }
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    void put(String key, String value) {
        if (isValidName(key)) {
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
            }
        }
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    String remove(String key) {
        if (isValidName(key)) {
            if (!containsKey(key)) {
                return null;
            }
            //String rem = keyArray[hash(key)];
            //keyArray[hash(key)] = null;
            //return rem;
            String rem = null;
            LinkedList<Entry> t = keyArray[hash(key)];
            for (int i = 0; i < t.size(); i++) {
                if (t.get(i).key.equals(key)) {
                    rem = t.get(i).value;
                    t.remove(t.get(i));
                }
            }
            return rem;

        }
        return null;
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
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
