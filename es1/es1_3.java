public class es1_3{
   public static void main(String[] args){
      String a = "654321Rossi";
      if(scan(a))
         System.out.println("YES");
      else
         System.out.println("NOO");
   
   }

   public static boolean testAK(char c){
      String s = "abcdefghijkABCDEFGHIJK";
      for(int i=0;i<s.length();i++){
         if(s.charAt(i)==c)
            return true;
      }
      return false;
   }

   public static boolean testLZ(char c){
      String s = "lmnopqrstuvxyzLMNOPQRSTUVXYZ";
      for(int i=0;i<s.length();i++){
         if(s.charAt(i)==c)
            return true;
      }
      return false;
   }

   public static boolean testP(char c){
      String s = "24680";
      for(int i=0;i<s.length();i++){
         if(s.charAt(i)==c)
            return true;
      }
      return false;
   }

   public static boolean testD(char c){
      String s = "13579";
      for(int i=0;i<s.length();i++){
         if(s.charAt(i)==c)
            return true;
      }
      return false;
   }

   public static boolean lettera(char c){
      return testAK(c)||testLZ(c);
   }

   public static boolean scan(String a){
      int state = 0;
      int i=0;
      while(state>=0&&i<a.length()){
         char c = a.charAt(i++);
         switch(state){
            case 0:
               System.out.println(0);
               if(lettera(c))
                  state = 1;
               else if(testP(c))
                  state = 2;  
               else if(testD(c))
                  state = 3;
               break;
            case 1:
               System.out.println(1);
               break;
            case 2:
               System.out.println(2);
               if(testAK(c))
                  state = 4;
               else if(testLZ(c))
                  state = 1;
               else if(testP(c))
                  state = 2;  
               else if(testD(c))
                  state = 3;
               break;
            case 3:
               System.out.println(3);
               if(testAK(c))
                  state = 1;
               else if(testLZ(c))
                  state = 4;
               else if(testP(c))
                  state = 2;  
               else if(testD(c))
                  state = 3;
               break;
            case 4:
               System.out.println(4);
               if(!lettera(c))
                  state = 1;
               break;
         }
         
      }
      return state==4;
   }
}