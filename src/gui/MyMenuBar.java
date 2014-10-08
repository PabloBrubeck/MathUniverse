package gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractAction;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public class MyMenuBar extends JMenuBar {
    private static Font font=new Font("arial.ttf",0,12);
    public static class MyMenuItem extends JMenuItem{
        private Method method;
        private Object target;
        private Object[] params;
        public MyMenuItem(String label, String keyStroke, String methodName, Object obj, Object... args){
            target=obj;
            params=args;
            Class<?>[] types=new Class[args.length];
            for(int i=0; i<args.length; i++){
                types[i]=args[i].getClass();
            }
            try{
                method=obj.getClass().getMethod(methodName, types);
            }catch(NoSuchMethodException | SecurityException e) {
                System.err.println(e);
            }
            AbstractAction a=(new AbstractAction(label){
                @Override
                public void actionPerformed(ActionEvent ae){
                    invokeMethod();
                }
            });
            if(keyStroke!=null? !keyStroke.isEmpty(): false){
                a.putValue(AbstractAction.ACCELERATOR_KEY, KeyStroke.getAWTKeyStroke(keyStroke));
            }
            setAction(a);
            setFont(font);
        }
        private void invokeMethod(){
            try{
                method.invoke(target, params);
            }catch(IllegalAccessException | IllegalArgumentException | InvocationTargetException e){
                System.err.println(e);
            }
        }
    }
    public static class MyMenu extends JMenu{
        private final JMenuItem[] item;
        public MyMenu(String label, JMenuItem... items) {
            super(label);
            item=new JMenuItem[items.length];
            for(int i=0;  i<items.length; i++) {
                item[i]=items[i];
                add(item[i]);
            }
            setFont(font);
        }
    }
    
    private final MyMenu[] menu;
    public MyMenuBar(String[] labels, JMenuItem[][] items){
        menu=new MyMenu[labels.length];
        for (int i=0; i<labels.length; i++) {
            menu[i]=new MyMenu(labels[i], items[i]);
            add(menu[i]);
        }
        setFont(font);
    }
}