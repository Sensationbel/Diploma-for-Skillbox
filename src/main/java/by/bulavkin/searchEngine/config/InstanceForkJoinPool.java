package by.bulavkin.searchEngine.config;

import java.util.concurrent.*;

public class InstanceForkJoinPool {
    private static ForkJoinPool pool;

    private InstanceForkJoinPool(){
    }

    public static ForkJoinPool getMyForkJoinPool(){
        if(pool == null){
            pool = new ForkJoinPool();
        }
        return pool;
    }
}
