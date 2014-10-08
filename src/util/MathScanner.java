package util;

public class MathScanner {
    private String s;
    private int pointer;
    
    public MathScanner(String str){
        pointer=0;
        s=str;
    }
    
    public String next(){
        boolean first=(pointer==0);
        int start=pointer, end;
        if(s.charAt(start)=='('){
            end=indexClose(s,start++);
            pointer=end+1;
        }else{
            end=s.length();
            while(pointer<end){
                switch(s.charAt(pointer)){
                    case '[':
                        pointer=indexClose(s,pointer)+1;
                        end=pointer;
                        break;
                    case '-':
                        if(pointer==0){
                            break;
                        }
                    case '^':                       
                    case '+':
                    case '*':
                    case ' ':
                    case '/':
                    case '(':
                        end=pointer;
                }
                pointer++;
            }
            pointer=end;
        }
        if(start==end){
            pointer++;
            return Character.toString(s.charAt(start));
        }
        if(hasNext()){
            if(!first && s.charAt(pointer)=='^'){
                pointer++;
                return "("+s.substring(start,end)+")^"+next(); 
            }
        }
        return s.substring(start,end);
    }
    public String nextTerm(){
        int start=pointer, end=s.length();
        while(pointer<end){
            switch(s.charAt(pointer)){
                case '(':
                case '[':
                    pointer=indexClose(s,pointer)+1;
                    break;
                case '-':
                    if(pointer==start){
                        pointer++;
                    }else{
                        end=pointer;
                    }
                    break;
                case '+':
                    end=pointer;
                default:
                    pointer++;
            }
        }
        return s.substring(start,end);
    }
    public boolean hasNext(){
        return pointer<s.length();
    }
    public String rest(){
        return s.substring(pointer);
    }
    
    public static int indexClose(String s, int indexOpen){
        char open=s.charAt(indexOpen);
        char close=(open=='(')? ')': ']';
        char c;
        int nest=0;
        do{
            c=s.charAt(indexOpen++);
            if(c==open){
                nest++;
            }else if(c==close){
                nest--;
            }
        }while(nest>0);
        return indexOpen-1;
    }
}