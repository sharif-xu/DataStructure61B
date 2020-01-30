public class hw0 {
    static int max(int[] a) {
        int max_int;
        max_int=a[0];
        for (int i = 0; i < a.length ; i++) {
            if(max_int<=a[i])
                max_int=a[i];
        }
        return max_int;
    }

     static boolean Three_SUM(int[] b){
        int val1, val2, val3=0;
        for (int i = 0; i < b.length; i++) {
            val1=b[i];
            for (int j = 0; j <b.length ; j++) {
                val2=b[j];
                for (int k = 0; k < b.length ; k++) {
                    val3=b[k];
                    if (val1+val2+val3==0)
                        return true;
                }
            }
        }
        return false;
    }


    static boolean Three_SUM_DISTINCT(int[] c){
        int val1, val2, val3 = 0;
        for (int i = 0; i <c.length ; i++) {
            val1=c[i];
            for (int j = i+1; j <c.length ; j++) {
                val2 = c[j];
                for (int k = j+1; k <c.length ; k++)
                    val3=c[k];
                    if(val1+val2+val3==0)
                        return true;
                }
            }
            return false;
        }

}


