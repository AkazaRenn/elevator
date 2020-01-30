package ca.carleton.winter2020.sysc3303a.group8.utils;

import java.util.Deque;
import java.util.NoSuchElementException;
import java.util.TreeSet;


/**
 * Sorted double-ended queue implemented for usage of job queues of the
 * elevator subsystems.
 * Elements inside are sorted in ascending order.
 */
@SuppressWarnings("serial")
public class SortedDeque<T> extends TreeSet<T> implements Deque<T> {

    @Override
    public void addFirst(T e) {
        add(e);
    }

    @Override
    public void addLast(T e) {
        add(e);
    }

    @Override
    public T element() {
        T element = first();
        if(element == null) {
            throw new NoSuchElementException();
        }
        return element;
    }

    @Override
    public T getFirst() {
        return pollFirst();
    }

    @Override
    public T getLast() {
        return pollLast();
    }

    @Override
    public boolean offer(T e) {
        add(e);
        return true;
    }

    @Override
    public boolean offerFirst(T e) {
        return offer(e);
    }

    @Override
    public boolean offerLast(T e) {
        return offer(e);
    }

    @Override
    public T peek() {
        return first();
    }

    @Override
    public T peekFirst() {
        return first();
    }

    @Override
    public T peekLast() {
        return last();
    }

    @Override
    public T poll() {
        return pollFirst();
    }

    @Override
    public T pop() {
        return removeFirst();
    }

    @Override
    public void push(T e) {
        addFirst(e);        
    }

    @Override
    public T remove() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public T removeFirst() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public T removeLast() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        // TODO Auto-generated method stub
        return false;
    }


}
