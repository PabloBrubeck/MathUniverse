package test;

import java.util.*;
import java.lang.reflect.*;

public class ProxyTest {
    public static class ProxyBuilder<T>{
        public static <T> T newProxyInterface(T u, Class<?>... interfaces){
            return (T)MyDynamicProxyClass.newInstance(u, interfaces);
        }
    }
    public static class MyDynamicProxyClass implements InvocationHandler{
        Object obj;
        public MyDynamicProxyClass(Object obj){
            this.obj = obj;
        }
        @Override
        public Object invoke(Object proxy, Method m, Object... args) throws Throwable{
            try{
                return m.invoke(obj, args);
            }catch(InvocationTargetException e){
                throw e.getTargetException();
            }catch(IllegalAccessException | IllegalArgumentException e){
                throw e;
            }
        }
        public static Object newInstance(Object obj, Class[] interfaces){
            return Proxy.newProxyInstance(obj.getClass().getClassLoader(), interfaces, new MyDynamicProxyClass(obj));
        }
    }
    public static class ViewProxy implements InvocationHandler {
        private Map map;
        public static Object newInstance(Map map, Class[] interfaces) {
            return Proxy.newProxyInstance(map.getClass().getClassLoader(), interfaces, new ViewProxy(map));
        }
        public ViewProxy(Map map) {
            this.map = map;
        }
        @Override
        public Object invoke(Object proxy, Method m, Object... args) throws Throwable {
            Object result;
            String methodName = m.getName();
            if (methodName.startsWith("get")) {
                String name = methodName.substring(methodName.indexOf("get") + 3);
                return map.get(name);
            } else if (methodName.startsWith("set")) {
                String name = methodName.substring(methodName.indexOf("set") + 3);
                map.put(name, args[0]);
                return null;
            } else if (methodName.startsWith("is")) {
                String name = methodName.substring(methodName.indexOf("is") + 2);
                return (map.get(name));
            }
            return null;
        }
    }

    public interface Algebra{
        
    }
    public interface Complex extends Algebra{
        
    }
    public interface Real extends Complex{
        
    }
    
    //Primitives
    public static class Fraction implements Real{
        
    }
    public static class Rectangular implements Complex{
        
    }
    public static class Polar implements Complex{
        
    }
    
    //Proxies
    public static class Sum implements Algebra{
        
    }
    public static class Product implements Algebra{
        
    }
    public static class Quotient implements Algebra{
        
    }
    public static class Polynomial implements Algebra{
        
    }
    public static class Symbol implements Algebra{
        
    }
    public static class Function implements Algebra{
        
    }
    
    public static void main(String[] args){
        Sum sigma=new Sum();
        Sum foo = ProxyBuilder.newProxyInterface(sigma, Real.class);
        
        System.out.println(foo.getClass());
    }
}
