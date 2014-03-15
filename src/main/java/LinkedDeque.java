import java.util.*;

/**
 * Created by artbart on 3/7/14.
 */
public class LinkedDeque<E> implements Collection<E> {
    private class Node{
        E val;
        Node next;
        Node prev;

        private Node() {
        }
        private Node(E val) {
            this.val = val;
        }
    }

    private class LinkedDequeIterator implements Iterator<E>{
        private Node current;
        private boolean isRemoved;

        private LinkedDequeIterator() {
            this.current = head;
            isRemoved=false;
        }

        @Override
        public boolean hasNext() {
            return current.next!=tail;
        }

        @Override
        public E next() {
            if (current.next==tail) throw new NoSuchElementException();
            current=current.next;
            isRemoved=false;
            return current.val;
        }

        @Override
        public void remove() {
            if (isRemoved || current==head ) throw new IllegalStateException();
            isRemoved=true;
            removeNode(current);
        }
    }

    private int size;
    private Node head;
    private Node tail;


    public LinkedDeque() {
        size=0;
        head=new Node();
        tail=new Node();
        head.next=tail;
        tail.prev=head;
    }

    // deque
    public void addFirst(E elem){
        if (elem==null) throw  new NullPointerException();
        Node node=new Node(elem);
        insertAfter(head, node);
        size++;
    }
    public void addLast(E elem){
        add(elem);
    }

    public E getFirst(){
        if (size==0) throw new NoSuchElementException();
        return head.next.val;
    }
    public E getLast(){
        if (size==0) throw new NoSuchElementException();
        return tail.prev.val;
    }


    public E removeFirst(){
        if (size==0) throw new NoSuchElementException();
        Node node=head.next;
        removeNode(node);
        return node.val;
    }
    public E removeLast(){
        if (size==0) throw new NoSuchElementException();
        Node node=tail.prev;
        removeNode(node);
        return node.val;
    }


    // collection

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean contains(Object o) {
        return findNode(o)!=null;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] arr=new Object[size];
        Iterator<E> it=iterator();
        int ind=0;
        while (it.hasNext()) arr[ind++]=it.next();
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] arr=(T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        Iterator<E> it=iterator();
        int ind=0;
        while (it.hasNext()) arr[ind++]= (T) it.next();
        return arr;
    }

    @Override
    public boolean add(E elem) {
        if (elem==null) throw  new NullPointerException();
        Node node=new Node(elem);
        insertBefore(tail,node);
        size++;
        return true;
    }

    @Override
    public boolean remove(Object o) {
        Node node=findNode(o);
        if (node==null) return false;
        removeNode(node);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c){
            if (!contains(o)) return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E o : c) add(o);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean modified=false;
        for (Object o : c){
            Node node=findNode(o);
            if (node==null) continue;
            removeNode(node);
            modified=true;
        }
        return modified;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (!c.contains(it.next())) {
                it.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        head.next=tail;
        tail.prev=head;
        size=0;
    }

    private void insertBefore(Node oldNode, Node newNode){
        newNode.prev=oldNode.prev;
        newNode.next=oldNode;

        oldNode.prev.next=newNode;
        oldNode.prev=newNode;
    }

    private void insertAfter(Node oldNode, Node newNode){
        newNode.next=oldNode.next;
        newNode.prev=oldNode; 
        oldNode.next.prev=newNode;
        oldNode.next=newNode;
    }

    private void removeNode(Node node){
        node.prev.next=node.next;
        node.next.prev=node.prev;
        size--;
    }

    private Node findNode(Object o){
        Node node=head.next;
        while (node!=tail && !node.val.equals(o)) node=node.next;
        if (node!=tail) return node;
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkedDeque)) return false;
        Iterator tit=iterator();
        Iterator nit=((LinkedDeque) obj).iterator();


        while (tit.hasNext()){
            if (!nit.hasNext()) return false;
            if (!tit.next().equals(nit.next())) return false;
        }

        return !nit.hasNext();
    }

    @Override
    public String toString() {
        Iterator<E> it=iterator();
        StringBuilder sb=new StringBuilder();
        sb.append("[");
        while (it.hasNext()){
            sb.append(it.next().toString()).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
