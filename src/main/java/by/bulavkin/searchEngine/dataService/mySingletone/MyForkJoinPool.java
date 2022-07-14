package by.bulavkin.searchEngine.dataService.mySingletone;

import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class MyForkJoinPool {
    private static ForkJoinPool pool;

    private MyForkJoinPool(){
    }

    public static ForkJoinPool getMyForkJoinPool(){
        if(pool == null){
            pool = new ForkJoinPool();
        }
        return pool;
    }
}
