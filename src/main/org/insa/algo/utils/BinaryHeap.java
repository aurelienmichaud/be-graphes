//
// ******************PUBLIC OPERATIONS*********************
// void insert( x ) --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( ) --> Return smallest item
// boolean isEmpty( ) --> Return true if empty; else false
// ******************ERRORS********************************
// Throws RuntimeException for findMin and deleteMin when empty

package org.insa.algo.utils;

import java.util.ArrayList;

/**
 * Implements a binary heap. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @author Mark Allen Weiss
 * @author DLB
 */
public class BinaryHeap<E extends Comparable<E>> implements PriorityQueue<E> {

    // Number of elements in heap.
    private int currentSize;

    // The heap array.
    private final ArrayList<E> array;

    /**
     * Construct a new empty binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new ArrayList<E>();
    }

    /**
     * Construct a copy of the given heap.
     * 
     * @param heap Binary heap to copy.
     */
    public BinaryHeap(BinaryHeap<E> heap) {
        this.currentSize = heap.currentSize;
        this.array = new ArrayList<E>(heap.array);
    }

    /**
     * Set an element at the given index.
     * 
     * @param index Index at which the element should be set.
     * @param value Element to set.
     */
    private void arraySet(int index, E value) {
        if (index == this.array.size()) {
            this.array.add(value);
        }
        else {
            this.array.set(index, value);
        }
    }

    /**
     * @return Index of the parent of the given index.
     */
    private int index_parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * @return Index of the left child of the given index.
     */
    private int index_left(int index) {
        return index * 2 + 1;
    }

    /**
     * Internal method to percolate up in the heap.
     * 
     * @param index Index at which the percolate begins.
     */
    private void percolateUp(int index) {
        E x = this.array.get(index);

        for (; index > 0
                && x.compareTo(this.array.get(index_parent(index))) < 0; index = index_parent(
                        index)) {
            E moving_val = this.array.get(index_parent(index));
            this.arraySet(index, moving_val);
        }

        this.arraySet(index, x);
    }

    /**
     * Internal method to percolate down in the heap.
     * 
     * @param index Index at which the percolate begins.
     */
    private void percolateDown(int index) {
        int ileft = index_left(index);
        int iright = ileft + 1;

        if (ileft < this.currentSize) {
            E current = this.array.get(index);
            E left = this.array.get(ileft);
            boolean hasRight = iright < this.currentSize;
            E right = (hasRight) ? this.array.get(iright) : null;

            if (!hasRight || left.compareTo(right) < 0) {
                // Left is smaller
                if (left.compareTo(current) < 0) {
                    this.arraySet(index, left);
                    this.arraySet(ileft, current);
                    this.percolateDown(ileft);
                }
            }
            else {
                // Right is smaller
                if (right.compareTo(current) < 0) {
                    this.arraySet(index, right);
                    this.arraySet(iright, current);
                    this.percolateDown(iright);
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    @Override
    public int size() {
        return this.currentSize;
    }

    @Override
    public void insert(E x) {
        int index = this.currentSize++;
        this.arraySet(index, x);
        this.percolateUp(index);
    }

    @Override
    public void remove(E x) throws ElementNotFoundException {
    	
        int index;
        
        /* On verifie que x est dans le tableau */
    	for(index=0; index < this.currentSize; index++) {
    		if(this.array.get(index).equals(x))
    			break;
    	}
    	
    	if(index >= this.currentSize) {
    		throw new ElementNotFoundException(x);
    	}
    	
    	if (index == 0) {
    		deleteMin();
    	}
    	else {
	    	/* On remplace x par le dernier element */
	    	E lastItem = this.array.get(--(this.currentSize));
	    	this.arraySet(index, lastItem);
	    	
	    	this.percolateDown(index);
	    	this.percolateUp(index);
    	}
    	
    }

    @Override
    public E findMin() throws EmptyPriorityQueueException {
        if (isEmpty())
            throw new EmptyPriorityQueueException();
        return this.array.get(0);
    }

    @Override
    public E deleteMin() throws EmptyPriorityQueueException {
        E minItem = findMin();
        E lastItem = this.array.get(--this.currentSize);
        this.arraySet(0, lastItem);
        this.percolateDown(0);
        return minItem;
    }

    /**
     * Prints the heap
     */
    public void print() {
        System.out.println();
        System.out.println("========  HEAP  (size = " + this.currentSize + ")  ========");
        System.out.println();

        for (int i = 0; i < this.currentSize; i++) {
            System.out.println(this.array.get(i).toString());
        }

        System.out.println();
        System.out.println("--------  End of heap  --------");
        System.out.println();
    }

    /**
     * Prints the elements of the heap according to their respective order.
     */
    public void printSorted() {

        BinaryHeap<E> copy = new BinaryHeap<E>(this);

        System.out.println();
        System.out.println("========  Sorted HEAP  (size = " + this.currentSize + ")  ========");
        System.out.println();

        while (!copy.isEmpty()) {
            System.out.println(copy.deleteMin());
        }

        System.out.println();
        System.out.println("--------  End of heap  --------");
        System.out.println();
    }
    
    public boolean isValide(int index) {
    	
    	int ileft = this.index_left(index);
    	int iright = ileft + 1;
    
    	if (index < this.size()) {
    		
    		if (ileft < this.size() && iright < this.size()) {
	
	    			return  this.array.get(ileft).compareTo(this.array.get(index)) > 0 &&
	    					this.array.get(iright).compareTo(this.array.get(index)) > 0 &&
	    					isValide(ileft) &&
	    					isValide(iright);

    		}
    		else if (ileft < this.size()) {
    			
    			return this.array.get(ileft).compareTo(this.array.get(index)) > 0 && 
    					isValide(ileft);
    		}
    		else if (iright < this.size()) {
    			
    			return this.array.get(iright).compareTo(this.array.get(index)) > 0 && 
    					isValide(iright);
    		}
    		else
    			return true;
    	}
    	else
    		return true;
    }
}
