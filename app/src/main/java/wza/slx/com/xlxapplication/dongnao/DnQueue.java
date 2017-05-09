package wza.slx.com.xlxapplication.dongnao;

import java.util.NoSuchElementException;

/**
 * Created by Administrator on 2017/4/21.
 */

public class DnQueue<E> {

    int size = 0;
    Link<E> headLink;

    public DnQueue(){
        headLink = new Link<E>(null,null,null);
        headLink.previous = headLink;
        headLink.next = headLink;
    }

    /**
     *  进队
     * @param e
     * @return
     */
    public boolean offer(E e){
        return addLastImpl(e);
    }

    private boolean addLastImpl(E e) {
        Link<E> oldlast = headLink.previous;
        Link<E> newLink = new Link<>(e,oldlast,headLink);

        headLink.previous = newLink;
        oldlast.next = newLink;
        size++;
        return true;
    }

    public E poll(){
        return (size==0)?null:removeFirst();
    }

    private E removeFirst() {
        Link<E> first = headLink.next;
        if(first != headLink){
            headLink.next = first.next;
            headLink.next.previous = headLink;
            size--;
            return first.data;
        }
        throw new NoSuchElementException();
//        return null;
    }

    public int size(){
        return size;
    }


    public static final class Link<ET> {
        ET data;
        Link<ET> previous, next;

        public Link(ET o, Link<ET> p,Link<ET> n){
            this.data = o;
            this.previous = p;
            this.next = n;

        }
    }

}
