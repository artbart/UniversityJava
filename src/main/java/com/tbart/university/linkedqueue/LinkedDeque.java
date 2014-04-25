package com.tbart.university.linkedqueue;

import java.util.*;

/**
 * Created by tbart on 3/7/14.
 * LinkedDeque linear collection that supports element insertion and removal at
 * both ends. For storing element two way linkedlist is used.
 * <p/>
 * Methods:
 * <ul>
 * <li>addFirst, addLast, getFirst, getLast, pollFirst, pollLast, peekFirst, peekLast - O(1)</li>
 * <li>size, isEmpty - O(1)</li>
 * <li>clear - O(N)</li>
 * <li>toString - O(N)</li>
 * </ul>
 * <p/>
 */
public class LinkedDeque<E> implements Deque<E> {

    /**
     * Represent Node in list
     */
    private class Node {
        public E val;
        public Node next;
        public Node prev;

        private Node() {
        }

        private Node(E val) {
            this.val = val;
        }
    }

    private class LinkedDequeIterator implements Iterator<E> {
        private Node current;
        private boolean isRemoved;

        private LinkedDequeIterator() {
            this.current = head;
            isRemoved = false;
        }

        @Override
        public boolean hasNext() {
            return current.next != tail;
        }

        @Override
        public E next() {
            if (current.next == tail) throw new NoSuchElementException();
            current = current.next;
            isRemoved = false;
            return current.val;
        }

        @Override
        public void remove() {
            if (isRemoved || current == head) throw new IllegalStateException();
            isRemoved = true;
            removeNode(current);
        }
    }

    /**
     * Descendant iterator for LinkedDeque
     */
    private class LinkedDequeDescendantIterator implements Iterator<E> {
        private Node current;
        private boolean isRemoved;

        private LinkedDequeDescendantIterator() {
            this.current = tail;
            isRemoved = false;
        }

        @Override
        public boolean hasNext() {
            return current.prev != head;
        }

        @Override
        public E next() {
            if (current.prev == head) throw new NoSuchElementException();
            current = current.prev;
            isRemoved = false;
            return current.val;
        }

        @Override
        public void remove() {
            if (isRemoved || current == tail) throw new IllegalStateException();
            isRemoved = true;
            removeNode(current);
        }
    }

    private int size;
    private final Node head;
    private final Node tail;


    public LinkedDeque() {
        size = 0;
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    // deque
    public void addFirst(E elem) {
        if (elem == null) throw new NullPointerException();
        Node node = new Node(elem);
        insertAfter(head, node);
        size++;
    }

    public void addLast(E elem) {
        add(elem);
    }

    @Override
    public boolean offerFirst(E e) {
        addFirst(e);
        return true;
    }

    @Override
    public boolean offerLast(E e) {
        addLast(e);
        return true;
    }

    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.next.val;
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.prev.val;
    }

    @Override
    public E peekFirst() {
        if (size == 0) return null;
        return getFirst();
    }

    @Override
    public E peekLast() {
        if (size == 0) return null;
        return getLast();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        Iterator<E> it = iterator();
        while (it.hasNext()) {
            if (it.next().equals(o)) {
                it.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        Iterator<E> it = descendingIterator();
        while (it.hasNext()) {
            if (it.next().equals(o)) {
                it.remove();
                return true;
            }
        }
        return false;
    }


    public E removeFirst() {
        if (size == 0) throw new NoSuchElementException();
        Node node = head.next;
        removeNode(node);
        return node.val;
    }

    public E removeLast() {
        if (size == 0) throw new NoSuchElementException();
        Node node = tail.prev;
        removeNode(node);
        return node.val;
    }

    @Override
    public E pollFirst() {
        if (size == 0) return null;
        return removeFirst();
    }

    @Override
    public E pollLast() {
        if (size == 0) return null;
        return removeLast();
    }


    // collection

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean contains(Object o) {
        return findNode(o) != null;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new LinkedDequeDescendantIterator();
    }

    @Override
    public Object[] toArray() {
        Object[] arr = new Object[size];
        Iterator<E> it = iterator();
        int ind = 0;
        while (it.hasNext()) arr[ind++] = it.next();
        return arr;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        if (a == null) throw new NullPointerException();
        T[] arr;
        if (a.length >= size()) {
            arr = a;
        } else {
            arr = (T[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        Iterator<E> it = iterator();
        int ind = 0;
        while (it.hasNext()) arr[ind++] = (T) it.next();
        return arr;
    }

    @Override
    public boolean add(E elem) {
        if (elem == null) throw new NullPointerException();
        Node node = new Node(elem);
        insertBefore(tail, node);
        size++;
        return true;
    }

    @Override
    public boolean offer(E e) {
        return offerLast(e);
    }

    @Override
    public E remove() {
        return removeFirst();
    }

    @Override
    public E poll() {
        return pollFirst();
    }

    @Override
    public E element() {
        return getFirst();
    }

    @Override
    public E peek() {
        return peekFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        Node node = findNode(o);
        if (node == null) return false;
        removeNode(node);
        return true;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
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
        boolean modified = false;
        for (Object o : c) {
            Node node = findNode(o);
            if (node == null) continue;
            removeNode(node);
            modified = true;
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
        head.next = tail;
        tail.prev = head;
        size = 0;
    }

    /**
     * Insert new node in list before the old node
     *
     * @param oldNode - node in list
     * @param newNode - node for insertion
     */
    private void insertBefore(Node oldNode, Node newNode) {
        newNode.prev = oldNode.prev;
        newNode.next = oldNode;

        oldNode.prev.next = newNode;
        oldNode.prev = newNode;
    }

    /**
     * Insert new node in list after the old node
     *
     * @param oldNode - node in list
     * @param newNode - node to be inserted
     */
    private void insertAfter(Node oldNode, Node newNode) {
        newNode.next = oldNode.next;
        newNode.prev = oldNode;
        oldNode.next.prev = newNode;
        oldNode.next = newNode;
    }

    /**
     * remove node from list
     *
     * @param node - node to be inserted
     */
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    /**
     * Find node which key equals to the o
     *
     * @param o - object to be found
     * @return null if there is no node with such key or Node if such node exists
     */
    private Node findNode(Object o) {
        Node node = head.next;
        while (node != tail && !node.val.equals(o)) node = node.next;
        if (node != tail) return node;
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LinkedDeque)) return false;
        Iterator tit = iterator();
        Iterator nit = ((LinkedDeque) obj).iterator();


        while (tit.hasNext()) {
            if (!nit.hasNext()) return false;
            if (!tit.next().equals(nit.next())) return false;
        }

        return !nit.hasNext();
    }

    @Override
    public String toString() {
        Iterator<E> it = iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        while (it.hasNext()) {
            sb.append(it.next().toString()).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }
}
