public class es1_7{
   public static void main(String[] args){
      String a = "pis";
      if(scan(a))
         System.out.println("YES");
      else
         System.out.println("NOO");
   }

   public static boolean scan(String a){
      int state = 0;
      int i=0;
      while(state>=0&&i<a.length()){
         char c = a.charAt(i++);
         System.out.println(c);
         switch(state){
            case 0:
               if(c=='p')
                  state = 1;
               else
                  state = 4;
               break;
            case 1:
               if(c=='i')
                  state = 2;
               else
                  state = 5;
               break;
            case 2:
               if(c=='t')
                  state = 3;
               else
                  state = 6;
               break;
            case 3:
               state = 7;
               break;
            case 4:
               if(c=='i')
                  state = 2;
               else
                  state = 7;
               break;
            case 5:
               if(c=='t')
                  state = 3;
               else
                  state = 7;
               break;
            case 6:
               state = 7;
               break;
            case 7:

               break;
         }
      }
      return state==3||state==6;
   }
}