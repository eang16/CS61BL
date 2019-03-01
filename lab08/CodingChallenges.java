import java.util.ArrayList;
import java.util.List;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N - 1 containing all
     * the values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        int length = values.length;
        List<Integer> list = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            list.add(values[i]);
        }
        for (int i = 0; i < length; i++) {
            if (!list.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     */
    public static boolean sumTo(int[] values, int n) {
        int length = values.length;
        for (int i = 0; i < length; i++) {
            for (int k = 0; k < length; k++) {
                if (i != k && (values[i] + values[k] == n)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return false;
        }
        char[] a = s1.toCharArray();
        char[] b = s2.toCharArray();
        List<Character> alist = new ArrayList<>(a.length);
        List<Character> blist = new ArrayList<>(b.length);
        for (int i = 0; i < a.length; i++) {
            alist.add(i, a[i]);
            blist.add(i, b[i]);
        }
        for (int i = 0; i < a.length; i++) {
            Character x = a[i];
            if (alist.contains(x) && blist.contains(x)) {
                alist.remove(x);
                blist.remove(x);
            }
        }
        return (alist.isEmpty() && blist.isEmpty());

    }

    /**
     public static void main(String[] args) {
     int[] a1 = new int[]{0, 1, 2, 3, 4, 5};
     int[] a2 = new int[]{0, 1, 2, 4, 5};
     int[] a3 = new int[]{1, 2, 3, 4, 5};
     int[] a4 = new int[]{0, 4, 2, 3};
     System.out.println(missingNumber(a1));
     System.out.println(missingNumber(a2));
     System.out.println(missingNumber(a3));
     System.out.println(missingNumber(a4));
     System.out.println();
     System.out.println(sumTo(a1, 3));
     System.out.println(sumTo(a1, 10));
     System.out.println(sumTo(a1, 1));
     System.out.println(sumTo(a1, 20));
     System.out.println();
     String a = "abcd";
     String b = "abcd";
     String c = "abc";
     String d = "ab";
     String e = "a";
     String f = "";
     String g = "dcba";
     String h = "aaab";
     String i = "baaa";
     System.out.println(isPermutation(a, b));    //true
     System.out.println(isPermutation(a, c));    //false
     System.out.println(isPermutation(a, d));    //false
     System.out.println(isPermutation(a, e));    //false
     System.out.println(isPermutation(a, f));    //false
     System.out.println(isPermutation(a, g));    //true
     System.out.println(isPermutation(a, h));    //false
     System.out.println(isPermutation(h, i));    //true
     }
     */
}
