package top.blogapi.context;

import org.springframework.stereotype.Component;
import top.blogapi.model.entity.Guest;

public class GuestContext {
    private static final ThreadLocal<Guest> holder = new ThreadLocal<>();

    public static Guest get(){
        return holder.get();
    }

    public static Long getId(){
        Guest g = holder.get();
        if(g == null) return null;
        return g.getId();
    }

    public static void set(Guest guest){
        holder.set(guest);
    }

    public static void clear(){
        holder.remove();
    }
}
