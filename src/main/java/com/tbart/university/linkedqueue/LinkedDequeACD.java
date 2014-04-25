package com.tbart.university.linkedqueue;

import java.util.AbstractCollection;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by tbart on 3/15/14.
 */
public class LinkedDequeACD<E> extends AbstractCollection<E> {
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

    private int size;
    private final Node head;
    private final Node tail;


    public LinkedDequeACD() {
        size = 0;
        head = new Node();
        tail = new Node();
        head.next = tail;
        tail.prev = head;
    }

    @Override
    public Iterator<E> iterator() {
        return new LinkedDequeIterator();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean add(E elem) {
        if (elem == null) throw new NullPointerException();
        Node node = new Node(elem);
        insertBefore(tail, node);
        size++;
        return true;
    }


    private void insertBefore(Node oldNode, Node newNode) {
        newNode.prev = oldNode.prev;
        newNode.next = oldNode;

        oldNode.prev.next = newNode;
        oldNode.prev = newNode;
    }

    private void insertAfter(Node oldNode, Node newNode) {
        newNode.next = oldNode.next;
        newNode.prev = oldNode;
        oldNode.next.prev = newNode;
        oldNode.next = newNode;
    }

    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
        size--;
    }

    public void addFirst(E elem) {
        if (elem == null) throw new NullPointerException();
        Node node = new Node(elem);
        insertAfter(head, node);
        size++;
    }

    public void addLast(E elem) {
        add(elem);
    }

    public E getFirst() {
        if (size == 0) throw new NoSuchElementException();
        return head.next.val;
    }

    public E getLast() {
        if (size == 0) throw new NoSuchElementException();
        return tail.prev.val;
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


}
