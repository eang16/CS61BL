public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        for(int i = 0; i < values.length; i++){

            if(i >= pos){
                if(i == values.length-1){
                    values[i] = 0;
                    break;
                } else {
                values[i] = values[i + 1];
            }
            }
        }
        //values[values.length-1] = 0;
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        // TODO: YOUR CODE HERE
        int temp1 = 0;

        for(int i = 0; i < values.length; i++){
            if(i == pos){
                temp1 = values[i]; 
                values[i] = newInt; 
            }
            if (i > pos){
                int temp2 = temp1;
                temp1 = values[i];
                values[i] = temp2;
            }
        }
    }
}
