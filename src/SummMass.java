import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

import static java.lang.Thread.sleep;

/**
 * Created by 16683189 on 27.09.2018.
 */
public class SummMass extends RecursiveTask {

    private int[] mas;
    private int sum;
    private int count=2;
    private int cas=1;

    private List<Integer> list;

    public SummMass(List<Integer> list) {
        this.list = list;
    }

    public SummMass(int[] mas, List<Integer> list) {
        this.mas = mas;
        this.list = inToList(mas);
    }

    public SummMass(int[] mas, int count, int cas) {
        this.mas = mas;
        this.list = inToList(mas);
        this.count=count;
    }

    private List inToList (int[] mas)
    {
        List list = new ArrayList();
        for (int i=0; i < mas.length; i++)
        {
            list.add(mas[i]);
        }
        return list;
    }


    private SummMass createSubTask(int start, int end)
    {
        SummMass subMass = new SummMass(this.list.subList(start,end));

     /*   try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        subMass.sum = sumSubMass(subMass);
        return subMass;
    }

    private int sumSubMass(SummMass subMass)
    { int summ=0;
        for (int a: subMass.list)
        {summ+=a; }
        return summ;
    }

    @Override
    protected Integer compute() {
       List<SummMass> subTasks = new LinkedList<>();
        // деление по модулю
       int dola = (int) mas.length/count;
        int sum = 0;
        int j=0;

        for (int i=0; i<count; i++ )
        {
            SummMass iTask = createSubTask(j, j+dola);
            // проверка в части выполнения методом fork т.е. параллельно
       //    iTask.fork();

            //запускаем параллельное выполнение.

            // дабавляем iTask в List всех подтасков
            subTasks.add(iTask);

            j=j+dola;
                    }

       /* SummMass leftTask =  createSubTask(0, dola);
        leftTask.fork();
       subTasks.add(leftTask);
        SummMass rightTask =  createSubTask(dola, mas.length);
        rightTask.fork();
        subTasks.add(rightTask);*/

       // подсчет суммы массива циклом по листу субтасков
        for(SummMass subTask: subTasks)
        {
          //  subTask.join();
            sum+=subTask.sum;
        }

        System.out.println("Сумма посчитанная методом ForkJoin - " + sum);
        return sum;

    }

    private int sumDola(int[] mas, int start, int end)
    {
        int sum=0;

        for (int i=start; i<end; i++)
        {
            sum += mas[i];
        }

        return sum;
    }

}
