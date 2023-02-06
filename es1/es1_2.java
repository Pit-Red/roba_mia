public class es1_2{
   public static void main(String[] args){
      String a = "--";
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
         switch(state){
            case 0:
               if(c=='0'||c=='1'||c=='2'||c=='3'||c=='4'||c=='5'||c=='6'||c=='7'||c=='8'||c=='9')
                  state = 1;
               else
                  state = 2;
               break;
            case 1:
               state = 1;
               break;
            case 2:
               if(c=='_')
                  state = 2;
               else
                  state = 3;
               break;
            case 3:
               state=3;
               break;
         }
         
      }
      return state==3;
   }
}