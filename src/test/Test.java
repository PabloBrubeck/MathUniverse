package test;

public class Test {
    public static class Box<T extends Object>{
        T data;
        public void setData(T obj){
            data=obj;
        }
        public T getData(){
            return data;
        }
    }
    public static class Closet<T extends Object> extends Box<T>{
        Box<T>[] many;
        public Closet(){
            super();
        }
        public void setMany(Box<T>... array){
            many=array;
        }
        public Box<T>[] getMany(){
            return many;
        }
    }    
    public static void main(String[] args){
        Box<Integer> a=new Box(), b=new Box();
        a.setData(7);
        b.setData(-3);
        Closet set= new Closet();
        set.setMany(a, b);
        
        Box<String> c=new Box(), d=new Box();
        c.setData("Hello");
        d.setData("World");
        
        Closet<String> set2=new Closet();
        set2.setMany(c,d);
        
        Box<String> m=set2;
        
        System.out.println(b.getData()+a.getData());
    }
}

