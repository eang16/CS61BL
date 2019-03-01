

public class HelloNumbers {
    public static void main(String[] args) {
        int x = 0;
        
        while (x < 10) {
            
            if(x == 0){
            System.out.print(x + " ");
            	x = x + 1;
            } else {

            	int c = 0;
            	for(int i = x; i>0; i--){
            		c = c+i;
            	}
            	System.out.print(c + " ");
            	x++;
            }
        }
        System.out.println();
    }
}
