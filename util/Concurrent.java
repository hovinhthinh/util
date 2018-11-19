package util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Concurrent {
    public static boolean runAndWait(Runnable run, int nThreads) {
        ExecutorService service = Executors.newFixedThreadPool(nThreads);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < nThreads; ++i) {
            futures.add(service.submit(run));
        }
        boolean result = true;
        try {
            for (Future f : futures) {
                f.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            result = false;
        }
        service.shutdown();
        return result;
    }
}
