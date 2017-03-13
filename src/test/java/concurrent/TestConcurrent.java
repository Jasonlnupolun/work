package concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/3/13.
 */
public class TestConcurrent {


//    .newFixedThreadPool创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程。
    public static void test01(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 20; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                   System.out.println(Thread.currentThread().getName());
                }
            };
            executorService.execute(syncRunnable);
        }
    }

//    2.newCachedThreadPool创建一个可缓存线程池，如果线程池长度超过处理需要，可灵活回收空闲线程
//
public static void test02() {
    ExecutorService executorService = Executors.newCachedThreadPool();
    for (int i = 0; i < 100; i++) {
        Runnable syncRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        executorService.execute(syncRunnable);
    }
}

//    运行结果：可以看出缓存线程池大小是不定值，可以需要创建不同数量的线程，在使用缓存型池时，
// 先查看池中有没有以前创建的线程，如果有，就复用.如果没有，就新建新的线程加入池中，
// 缓存型池子通常用于执行一些生存期很短的异步型任务


//    下面给出一个Executor执行Callable任务的示例代码：

    public static void test03(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<String>> resultList = new ArrayList<Future<String>>();

        //创建10个任务并执行
        for (int i = 0; i < 10; i++){
            //使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
            Future<String> future = executorService.submit(new TaskWithResult(i));
            //将任务执行结果存储到List中
            resultList.add(future);
        }

        //遍历任务的结果
        for (Future<String> fs : resultList){
            try{
                while(!fs.isDone());//Future返回如果没有完成，则一直循环等待，直到Future返回完成
                System.out.println(fs.get());     //打印各个线程（任务）执行的结果
            }catch(InterruptedException e){
                e.printStackTrace();
            }catch(ExecutionException e){
                e.printStackTrace();
            }finally{
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务
                executorService.shutdown();
            }
        }
    }

//
//    3.newScheduledThreadPool创建一个定长线程池，支持定时及周期性任务执行
//
//    schedule(Runnable command,long delay, TimeUnit unit)创建并执行在给定延迟后启用的一次性操作

//    示例：表示从提交任务开始计时，5000毫秒后执行
    public static void test04() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < 20; i++) {
            Runnable syncRunnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                }
            };
            executorService.schedule(syncRunnable, 5000, TimeUnit.MILLISECONDS);
        }
    }

//    运行结果和newFixedThreadPool类似，不同的是newScheduledThreadPool是延时一定时间之后才执行
//    scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnitunit)
//
//    创建并执行一个在给定初始延迟后首次启用的定期操作，后续操作具有给定的周期；也就是将在 initialDelay 后开始执行，然后在initialDelay+period 后执行，接着在 initialDelay + 2 * period 后执行，依此类推
    public static void test05() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        Runnable syncRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println( Thread.currentThread().getName());
            }
        };
        executorService.scheduleAtFixedRate(syncRunnable, 5000, 3000, TimeUnit.MILLISECONDS);


    }

//     scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit);
//    创建并执行一个在给定初始延迟后首次启用的定期操作，随后，在每一次执行终止和下一次执行开始之间都存在给定的延迟
public static void test06() {
    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
    Runnable syncRunnable = new Runnable() {
        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };
    executorService.scheduleWithFixedDelay(syncRunnable, 5000, 3000, TimeUnit.MILLISECONDS);
}
//    4.newSingleThreadExecutor创建一个单线程化的线程池，它只会用唯一的工作线程来执行任务，保证所有任务按照指定顺序(FIFO, LIFO, 优先级)执行
public static void test07() {
    ExecutorService executorService = Executors.newSingleThreadExecutor();
    for (int i = 0; i < 20; i++) {
        Runnable syncRunnable = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        executorService.execute(syncRunnable);
    }
}

/*
    ExecutorService接口继承自Executor接口，它提供了更丰富的实现多线程的方法，
    比如，ExecutorService提供了关闭自己的方法，以及可为跟踪一个或多个异步任务执行状况而生成 Future 的方法。
    可以调用ExecutorService的shutdown（）方法来平滑地关闭 ExecutorService，调用该方法后，
    将导致ExecutorService停止接受任何新的任务且等待已经提交的任务执行完成(已经提交的任务会分两类：一类是已经在执行的，另一类是还没有开始执行的)，
    当所有已经提交的任务执行完毕后将会关闭ExecutorService。因此我们一般用该接口来实现和管理多线程。

    ExecutorService的生命周期包括三种状态：运行、关闭、终止。创建后便进入运行状态，当调用了shutdown（）方法时，
    便进入关闭状态，此时意味着ExecutorService不再接受新的任务，但它还在执行已经提交了的任务，当素有已经提交了的任务执行完后，便到达终止状态。
    如果不调用shutdown（）方法，ExecutorService会一直处在运行状态，不断接收新的任务，执行新的任务，服务器端一般不需要关闭它，保持一直运行即可。*/

}

class TaskWithResult implements Callable<String> {
    private int id;

    public TaskWithResult(int id){
        this.id = id;
    }

    /**
     * 任务的具体过程，一旦任务传给ExecutorService的submit方法，
     * 则该方法自动在一个线程上执行
     */
    public String call() throws Exception {
        System.out.println("call()方法被自动调用！！！    " + Thread.currentThread().getName());
        //该返回结果将被Future的get方法得到
        return "call()方法被自动调用，任务返回的结果是：" + id + "    " + Thread.currentThread().getName();
    }
}

