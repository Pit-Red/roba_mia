public class es1_9{
   public static void main(String[] args){
      String a = "/***a***//88";
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
         //System.out.println(c);
         switch(state){
            case 0:
               if(c=='/')
                  state = 1;
               else
                  state = 5;
               break;
            case 1:
               if(c=='*')
                  state = 2;
               else
                  state = 5;
               break;
            case 2:
               if(c=='*')
                  state = 3;
               break;
            case 3:
               if(c=='/')
                  state = 4;
               else if(c=='a')
                  state = 3;
               break;
            case 4:
               state = 5;
            case 5:

               break;
         }
      }
      return state==4;
   }
}