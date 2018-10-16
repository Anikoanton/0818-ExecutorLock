import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * Created by 16683189 on 13.09.2018.
 */
public class var {
    private Random rnd = new Random();
    private final Set<String> comments = new HashSet<>();
    private final Set<String> topics = new HashSet<>();

    public synchronized void addComment(String comment)
    {
        System.out.println("addComment(): Добавление значеня '" + comment+ "'");
        try {
            Thread.sleep(1000 * rnd.nextInt(1));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        comments.add(comment);
        System.out.println("addComment(): Добавление комментария произведено '" + comment + "'");

    }

    public synchronized void addTopic(String comment)
    {
        topics.add(comment);
    }

    public synchronized void getComment()
    {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Iterator<String> iteratorComments = comments.iterator();
        int i = 1;
        while (iteratorComments.hasNext())
        {
            System.out.println("getComment(): " + i + " значение: '" + iteratorComments.next() +"'");
            i++;
        }
    }

    int count = 0;

    public int getCount() {
        return count;
    }

    public void setCount(int count, Object monitor) {

        synchronized (monitor) {
            try {
                System.out.println("Object: ожидание вызова " );
                monitor.wait();
                this.count = count;
                System.out.println("Object: count " + count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
