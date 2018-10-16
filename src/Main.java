import javafx.scene.paint.Stop;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.HOURS;

public class Main extends IOException {

       public static void main(String[] args) {



	// write your code here вспомнить Gnerics - ?
        // суппер - это ограничение снизу от класса до object а
        // extends - сверху от Object до класса
        // при этом add запись только когда суппер а read когда extends
     /*   List<Number> ints = new ArrayList<>();
        List<? super Double > list1 = ints;
        list1.add(3.0);*/

     Map<String, String>  map = new HashMap<>();
        int code=0;
        Scanner in = new Scanner(System.in);

           String ss = "str";
           Integer counter = new Integer(5);

           // монитор
           Object monitor = new Object();

        ExecutorService executor = Executors.newFixedThreadPool(8);

        System.out.println("введите code - 1) execReentrantReadWriteLock; 2) execStampedLock; 3) ReentrantLock 4) " +
                "OptimicLock 5) monitor \n 6) CountDownLauncher - блокирует до уменьшения countDown  \n 7) CyclicBarried - выставляет барьер" +
                " \n 8) Semaphore - вход заданного числа потоков в объект \n 9) NonBlock 10) forkJoin \n ===============================");
       while ( code>10 || code<1)
        {
            code=in.nextInt();
        }
        System.out.println("code определен " + code);

    switch (code)
    {
        case 1: execReentrantReadWriteLock(map, executor);
            break;
        case 2: execStampedLock(map, executor);
            break;
        case 3: ReentrantLock(map, executor);
            break;
        case 4: OptimicLock(map, executor, ss, counter);
            break;

        case 5: monitoring(executor, monitor);
            break;

        case 6: CountDownLatch(executor,in);
            break;

        case 7: CyclicBarried(executor,in);
            break;

        case 8: Semaphore(executor,in);
            break;

        case 9: nonBlock(executor,in);
            break;

        case 10: forkJoin(in);
            break;


        default:
            System.out.println("Такого выбора нет");
            break;


    }
    // МЕТОД ОСТАНОВКИ EXECUTORSERVICE
     stop(executor);

    }


    public static void forkJoin(Scanner in)
    {
        //fk();
        System.out.println("Задайте размер массива ");
        int j = pokazatel(in);
        int[] mas = new int[j];

        Random chisl = new Random();

        long time=System.currentTimeMillis();

        for (int i=0; i<j; i++)
        {
            mas[i]=chisl.nextInt(100);

        }

        time = System.currentTimeMillis() - time;
        System.out.println("Время заполнения masive: " + time);

        // сподсчет суммы массива в 1 потоке
        int sum=sumMassive(mas);

        System.out.println("Задайте число потоков ");
        int count = pokazatel(in);

        // Запуск подсчета суммы всех значений массива в ForkJoinPool
        time=System.currentTimeMillis();

     /*   System.out.println("Задайте режим выполнения в потоке - 1, не в потоке - 2");
        int cas = pokazatel(in);*/

        new ForkJoinPool().invoke(new SummMass(mas, count, 1));

        time = System.currentTimeMillis() - time;
        System.out.println("ForkJoinPool: Время подсчета суммы masive в " + count + "потоках ForkJoinPool: " + time );

    }



    static int sumMassive(int[] mas)
    {

      long time=System.currentTimeMillis();
       int sum=0;
        for (int i=0; i<mas.length; i++)
        {
            sum+=mas[i];
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Main: Время подсчета суммы masive в одном потоке: " + time + " значение суммы всех элементов: " + sum);

        return sum;

    }

    static void fk()
    {
        NumberPerHourReport n1 = new NumberPerHourReport();
        int n = 2;
        n1.addForHour(n);
        n1.addForHour(n);
        n1.addForHour(n);
        System.out.println(n + " - n1: Час. Значение события в часе " + n1.getNumberForHour(n));

        NumberPerHourReport n2 = new NumberPerHourReport();

        n2.addForHour(n);
        n2.addForHour(n);
        n2=n2.combine(n2);

        System.out.println(n + " - n2: Час. Значение события в часе " + n2.getNumberForHour(n));

        NumberPerHourReport[] mNP = new NumberPerHourReport[2];
        mNP[0]=n1;
        mNP[1]=n2;

        NumberPerHourReport n3 = new NumberPerHourReport();
        n3=n3.combine(mNP);
        System.out.println(n + " - n3: Час. Значение события в часе " + n3.getNumberForHour(n));
    }


    static NumberPerHourReport combine(NumberPerHourReport ... reports)
    {
        NumberPerHourReport result = new NumberPerHourReport();
        for (NumberPerHourReport report: reports)
        {
            for (int i=0; i<24; i++)
            {result.numbers[i]+=report.numbers[i];}
        }
        return result;

    }

    public static void nonBlock(ExecutorService executor, Scanner in)
    {
        var var = new var();

        System.out.println("Задайте число потоков для добавления коментария ");
        int n = pokazatel(in);

        for (int i=0; i<n; i++)
        {
            executor.submit(()-> {
                System.out.println("Старт потока на запись " + Thread.currentThread().getName());
                var.addComment("Комментарий от потока " + Thread.currentThread().getName() );
            });
        }

        executor.submit(()->{
            System.out.println("Получение записей потоком " + Thread.currentThread().getName());
            var.getComment();
        });

    }

    // блок кода по семафору по доступу к ресурсу
    public static void Semaphore(ExecutorService executor, Scanner in) {

        System.out.println("Введите число потоков для доступа к ресурсу через Semaphore: ");
        int n = pokazatel(in);

        Semaphore s = new Semaphore(n,true);

        // запускаем ровно в 3 раза больше потоков для доступа к ресурсу
        for (int i=0; i<(n*3); i++)
        {
        executor.submit(() ->
        {
            System.out.println("Стартовал поток по обращению к ресурсу - " + Thread.currentThread().getName() );
            try {
                s.acquire();
                resurce();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                s.release();
                System.out.println("Поток завершил обращение к ресурсу - " + Thread.currentThread().getName() );
            }
        });
        }


    }

    // ресурс к которому ограничевается доступ потоков
    private static void resurce()
    {
        System.out.println("Resurce: Поток преступил к выполнению Resurce: " + Thread.currentThread().getName());
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Resurce: Поток завершил выполнение Resurce: " + Thread.currentThread().getName());
    };

    // CyclicBarried - барьер
    public static void CyclicBarried(ExecutorService executor, Scanner in) {
        System.out.println("Введите число для CyclicBarried: ");
        int n = pokazatel(in);

        // ввести объект барьера CyclicBarrier который блокирует
        // все входящие потоки в точке данного барьера, затем выполняется действие actionBarr()
        CyclicBarrier barrier = new CyclicBarrier(n, () -> {actionBarr();}) ;

        // инициализация потоков блокирующихся на барьере
        for (int i=0; i<n; i++)
        {
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            executor.submit(() -> {
                System.out.println("Стартовал новый поток ожидающий барьера: " + Thread.currentThread().getName());
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("Поток преступил к выполнению действия после снятия барьера: " + Thread.currentThread().getName());

                Random rnd = new Random();
                try {
                    sleep(500*(1+rnd.nextInt(5)));
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Поток выполнил действие после снятия барьера: " + Thread.currentThread().getName());

            });
        }


    }

    public static void actionBarr()
    {
        System.out.println("actionBarr: старт действия после снятие барьера ");
        try {
            sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("actionBarr: выполнено действие после снятие барьера ");
    }


    // конец CyclicBarried

    public static void CountDownLatch(ExecutorService executor, Scanner in)
    {
        System.out.println("Введите число для CountDownLatch: ");
        int n = pokazatel(in);

        CountDownLatch doneSignal = new CountDownLatch(n);

        // ПОТОК КОТОРЫЙ ЖДЕТ CountDownLatch ИЛИ WAITER

        executor.submit(()->{
            System.out.println("Waiter: Ожидание до момента отработки всех CountDownLatch;  ");
            try {
                doneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            try {
                sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Waiter: Все CountDownLatch отработали; ");

        });

        for (int i =0; i<n; i++)
        {
            // Notifer, который будит Waiter

            executor.submit(()->{
                System.out.println("Notifer: стартовал поток: " + currentThread().getName());
                try {
                    sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                doneSignal.countDown();
                System.out.println("Notifer: CountDownLatch имеет значение - " + doneSignal.getCount() + " поток: " + currentThread().getName());

            });
        }

    };

    public static void monitoring(ExecutorService executor, Object monitor)
    {

        var obj = new var();

        Thread objectjob = new Thread(()->
        {
            synchronized (monitor) {
                System.out.println("objectjob: Старт обращения к объекту синхронизации ");
                obj.setCount(10, monitor);

                try {
                    monitor.wait();
                    System.out.println("objectjob: Завершение обращения к объекту синхронизации ");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        );

        executor.submit(objectjob);

        Thread waiter = new Thread(new Runnable() {
            @Override
            public void run() {

                    System.out.println("Waiter: Начало старта потока " + Thread.currentThread().getName());
             synchronized (monitor){
                try {
                monitor.wait();
                    System.out.println("Waiter in MONITOR: Отработка потока " + Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
                System.out.println("Waiter: Блок вне монитора " + Thread.currentThread().getName());
            }
        }, "Thread 1");


        Thread notifer = new Thread(new Runnable() {
            @Override
            public void run() {

                  //  System.out.println("Начало старта потока Notifer: " + Thread.currentThread().getName());

                System.out.println("Notifer: Ожидание освобожедния монитора: " + Thread.currentThread().getName());
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (monitor)
                {
                    monitor.notifyAll();
                    System.out.println("Notifer in MONITOR: Освобождение монитора: " + Thread.currentThread().getName());
                }

                System.out.println("Notifer:Блок вне монитора: " + Thread.currentThread().getName());

            }
        }, "Thread 2");

        // Waiter и Notifer
       // executor.submit(waiter);
        executor.submit(notifer);

    }




    public static void OptimicLock(Map<String,String> map, ExecutorService executor, String ss, Integer counter)

    {
        StampedLock lock = new StampedLock();

        executor.submit(()->
        {
            long stamp = lock.tryOptimisticRead();
            try {
                System.out.println("Optomistic lock valid: " + lock.validate(stamp) );
                sleep(3000);
                System.out.println("Optomistic lock valid: " + lock.validate(stamp) );
                sleep(3000);
                System.out.println("Optomistic lock valid: " + lock.validate(stamp) );
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(stamp);
            }
        });



        executor.submit(()->

                {
                    long stamp = lock.readLock();

                    try {
                        System.out.println("Read Lock acquired");
                        sleep(5000);
                    }

                    catch (
                            InterruptedException e) {
                        e.printStackTrace();

                    } finally {
                        lock.unlock(stamp);
                        System.out.println("Read done");
                    }
                }
        );

        executor.submit(()->
        {
            try {
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long stamp = lock.writeLock();

            try {
                System.out.println("Write Lock acquired");
                sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock(stamp);
                System.out.println("Write done \n --------------- ");
            }
        }
        );

        // конверт в блокировку записи
        StampedLock lock2 = new StampedLock();
        var var = new var();

        executor.submit (new Runnable() {
            @Override
            public void run() {

                long stamp = lock2.readLock();

               //int count  = 0;
               if (var.count == 0 )
               {
                   stamp = lock2.tryConvertToWriteLock(stamp);
                   System.out.println(stamp);

                   if (stamp == 0L)
                   {
                       System.out.println("Could not convert to write lock");
                       stamp = lock2.writeLock();
                   }

                    var.count = 23;
                }

                   System.out.println(var.count);
               }  });

      }




    public static void ReentrantLock(Map<String,String> map, ExecutorService executor)
    {

      ReentrantLock reentrantlock = new ReentrantLock();
        Runnable reed = () ->
        {

            try {
                reentrantlock.lock();
                System.out.println("Начало блокировки на чтение " +
                        Thread.currentThread().getName() + "\n  время стара "
                        +  System.currentTimeMillis() + " читаем: " + map.get("fooo") );
                sleep(12000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                reentrantlock.unlock();
                System.out.println("Завершение блокировки на чтение " +
                        Thread.currentThread().getName() + "  время завершения "
                        +  System.currentTimeMillis() + " \n------------------------------------ ");

            }

        };

        executor.submit(reed);
        executor.submit(reed);

        // проверка блокировок
        executor.submit(() ->
        {
            System.out.println("Locked: " + reentrantlock.isLocked());
            System.out.println("Held by me: " + reentrantlock.isHeldByCurrentThread());
            boolean locked = reentrantlock.tryLock();
            System.out.println("Lock acquired: " + locked);
        }
        );

    }

    public static void execStampedLock(Map<String, String> map, ExecutorService executor)
    {
       // ExecutorService executor = Executors.newCachedThreadPool();

        StampedLock stampedLock = new StampedLock();
        executor.submit(writeTask(stampedLock, map));


        Runnable readTask = () -> {

            long stamp = stampedLock.readLock();
            long ttctw;
            try {

                System.out.println("Начало блокировки на чтение " + Thread.currentThread().getName() +
                        " штамп: " + stamp
                        + "\n  время стара " +  System.currentTimeMillis() + " читаем: " + map.get("fooo"));
                sleep(12000);
                ttctw = stampedLock.tryConvertToWriteLock(stamp);
                System.out.println("Попытка сконвертировать блокировку" + Thread.currentThread().getName() + " в блок для записи: " + ttctw );
                ttctw = stampedLock.tryConvertToReadLock(stamp);
                System.out.println("Попытка сконвертировать блокировку" + Thread.currentThread().getName() + " в блок для чтения: " + ttctw );
                ttctw = stampedLock.tryConvertToOptimisticRead(stamp);
                System.out.println("Попытка сконвертировать блокировку" + Thread.currentThread().getName() + " в блок для оптимистичного чтения: " + ttctw );

            }

            catch (InterruptedException e) {
                e.printStackTrace();

            } finally {
                stampedLock.unlockRead(stamp);
                System.out.println("Завершение чтения " + Thread.currentThread().getName() + " штамп: " + stamp
                        + " \n время завершения " +  System.currentTimeMillis() + "\n ---------------");
            }

        };

        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);
        executor.submit(readTask);

        executor.submit(
                () -> {
                    long stamp = stampedLock.writeLock();

                    try {
                        System.out.println("Начало блокировки на запись 2 " + Thread.currentThread().getName() +
                                " штамп: " + stamp + "\n  время стара " +  System.currentTimeMillis() );
                        sleep(12000);
                        map.put("fooo", "bar2");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        stampedLock.unlockWrite(stamp);
                        System.out.println("Завершение записи " + Thread.currentThread().getName() + " штамп: " + stamp
                                + " \n время завершения " +  System.currentTimeMillis() + "\n ---------------");
                    }
                }
        );

    }


    public static Runnable writeTask(StampedLock stampedLock, Map<String, String>  map)
    {
        return (()-> {
            // штамповка для высвобождения рестурсов
            // OptimisticRead - метод для реализации оптимистичной блокировки.
            long stamp = stampedLock.writeLock();

            try {
                System.out.println("Начало блокировки на запись потоком " + Thread.currentThread().getName() +
                        " штамп: " + stamp
                        + "\n  время стара " +  System.currentTimeMillis());
                sleep(1000);
                map.put("fooo", "barr");


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                stampedLock.unlockWrite(stamp);
                System.out.println("Завершение записи " + Thread.currentThread().getName() + " штамп: " + stamp
                        + " \n время завершения " +  System.currentTimeMillis() + "\n ---------------");

            }
        }
        );

    }

    public static void execReentrantReadWriteLock(Map<String, String> map, ExecutorService executor)
    {
        // ExecutorService - формирует пул потоков
       // ExecutorService exexutor = Executors.newFixedThreadPool(3);

        ReadWriteLock lock = new ReentrantReadWriteLock();

        Runnable writeTask = new Runnable() {
            @Override
            public void run() {
                lock.writeLock().tryLock();
                lock.writeLock().lock();
                System.out.println("Начало блокировки на запись потоком " + Thread.currentThread().getName() + " время стара " +  System.currentTimeMillis());
                try {


                    sleep(15000);
                    //currentThread().setName("Поток 1");
                    map.put("foo","bar");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Завершение записи " + Thread.currentThread().getName() + " время завершения " +  System.currentTimeMillis() + "\n ---------------");
                    lock.writeLock().unlock();
                }

            }
        };




        executor.submit(writeTask);
        executor.submit(writeTask);
       // exexutor.submit(writeTask);
        //exexutor.submit(writeTask);

        for (int i=1; i<7; i++)
            executor.submit(threadRead(lock, map));



    }

    public static Thread threadRead(ReadWriteLock lock, Map<String, String> map)
    {
        return new Thread(new Runnable() {
            @Override
            public void run() {
                lock.readLock().lock();
                System.out.println("Начало блокировки на чтение потоком " + Thread.currentThread().getName() + " время стара " +  System.currentTimeMillis());
                try {
                    sleep(10000);
                    //currentThread().setName("Поток 1");
                 //   System.out.println("Завершение чтения  " + map.get("foo"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println("Завершение чтения  " + map.get("foo") + " " + Thread.currentThread().getName() + " время завершения " + System.currentTimeMillis()+ "\n ---------------");
                    lock.readLock().unlock();
                }

            }
        },"поток 2");
    }

    public static void stop(ExecutorService executor) {
        try {
            executor.shutdown();
            executor.awaitTermination(600, TimeUnit.SECONDS);
        }
        catch (InterruptedException e) {
            System.err.println("termination interrupted");
        }
        finally {
            if (!executor.isTerminated()) {
                System.err.println("killing non-finished tasks");
            }
            executor.shutdownNow();
        }
    }

    // показатель для java.concurent
    public static int pokazatel (Scanner in)
    {
        int n= 0;
        while (n<1 )
        {
            System.out.println("Введите число сейчас: ");
            n=in.nextInt();
            System.out.println("Введено число: " + n );
        }
        return n;
    }

}


