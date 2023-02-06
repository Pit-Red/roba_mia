public class es1_6{
   public static void main(String[] args){
      String a = "abbababababbbabbaab";
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
               if(c=='a')
                  state = 1;
               else if(c=='b')
                  state = 0;
               else
                  state = -1;
               break;
            case 1:
               if(c=='a')
                  state = 1;
               else if(c=='b')
                  state = 2;
               else
                  state = -1;
               break;
            case 2:
               if(c=='a')
                  state = 1;
               else if(c=='b')
                  state = 3;
               else
                  state = -1;
               break;
            case 3:
               if(c=='a')
                  state = 1;
               else if(c=='b')
                  state = 0;
               else   
                  state = -1;
               break;
         }
      }
      return state==3||state==2||state==1;
   }
}