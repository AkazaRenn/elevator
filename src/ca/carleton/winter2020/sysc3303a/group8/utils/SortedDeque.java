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
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return first();
    }

    @Override
    public T getFirst() {
        return pollFirst();
    }
    
    public T getHigher(T e) {
        return higher(e);
    }
    
    public T getLower(T e) {
        return lower(e);
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
        return removeFirst();
    }

    @Override
    public T removeFirst() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return pollFirst();
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return remove(o);
    }

    @Override
    public T removeLast() {
        if(isEmpty()) {
            throw new NoSuchElementException();
        }
        return pollLast();
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return remove(o);
    }


}
