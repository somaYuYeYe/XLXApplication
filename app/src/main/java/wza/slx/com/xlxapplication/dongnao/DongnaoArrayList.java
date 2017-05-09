package wza.slx.com.xlxapplication.dongnao;

import java.util.Collection;

/**
 * Created by Administrator on 2017/4/20.
 */

public class DongnaoArrayList<E> {
    int size;
    Object[] array;
    private static final int MIN_CAPACITY_INCREMENT = 12;

    public DongnaoArrayList(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("参数问题");
        }
        array = new Object[capacity];

    }

    public DongnaoArrayList() {
        array = new Object[0];
    }

    public DongnaoArrayList(Collection<? extends E> collection) {
        Object[] a = collection.toArray();
        if (a.getClass() != Object[].class) {
            Object[] newArr = new Object[a.length];

        }
    }

    private static int newCapacity(int currentCapacity) {
//        int increment = (currentCapacity<MIN_CAPACITY_INCREMENT?MIN_CAPACITY_INCREMENT:);

        return 1;
    }

    public boolean add(E object) {
        int s = size;
        Object[] a = array;
        if (s == a.length) {
            Object[] newArr = new Object[s + newCapacity(s)];
            System.arraycopy(a, 0, newArr, 0, s);
            array = a = newArr;|
        }
        a[s] = object;
        size = s + 1;
        return true;
    }

    public int size() {
        return size()
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int indexOf(E o) {
        Object[] a = array;
        int s = size;
        if (o != null) {
            for (int i = 0; i < s; i++) {
                if (o.equals(a[i])) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < s; i++) {
                if (a[i] == null) {
                    return i;
                }
            }
        }
        return -1;
    }

    public int lastIndexOf(E o) {

        return -1;
    }

    public E remove(int index) {
        Object[] a = array;
        int s = size;
        if (index > s) {
            throw new IndexOutOfBoundsException(" index = " + index + " size = " + s);
        }
        E e = (E) a[index];
        System.arraycopy(a, index + 1, a, index, --s - index);
        a[s] = null;
        size = s;
        return  e;
    }

//    public E remove()



    public E get(int index) {
        Object[] a = array;
        int s = size;
        if (index>s) {
            throw new IndexOutOfBoundsException();
        }
        E e = (E) a[index];
        return e;
    }
}
