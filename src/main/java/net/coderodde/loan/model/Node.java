package net.coderodde.loan.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * This class implements a loan graph node.
 *
 * @author coderodde
 * @version 1.6
 */
public class Node implements Iterable<Node> {
    
    /**
     * This is the name of a node. Also, this is treated as the ID of a 
     * node. (Two nodes <code>u</code>, <code>v</code> are considered as 
     * equal if and only if <code>u.name.equals(v.name)</code>.
     */
    private final String name;
    
    /**
     * This is the map from lender to loan amount.
     */
    private final Map<Node, Long> in;
    
    /**
     * This is the map from borrower to resources lent.
     */
    private final Map<Node, Long> out;
    
    /**
     * The graph owning this node, if any.
     */
    protected Graph ownerGraph;

    /**
     * If equity is below 0, this node owes, and, vice versa, if positive,
     * is eligible to receive cash.
     */
    private long equity;

    /**
     * Constructs a new node.
     * 
     * @param name the name of the new node.
     */
    public Node(final String name) {
        this.name = name;
        this.in = new HashMap<Node, Long>();
        this.out = new HashMap<Node, Long>();
    }

    /**
     * Copy-constructs a node.
     * 
     * @param copy the node to share the identity with.
     */
    public Node(final Node copy) {
        this(copy.name);
    }

    /**
     * Gets the name of this node.
     * 
     * @return the name of this node.
     */
    public String getName() {
        return name;
    }

    /**
     * Connects this node to a borrower with the specified amount.
     * 
     * @param borrower the borrower.
     * @param amount the amount of loan.
     */
    public void connectTo(final Node borrower, final long amount) {
        checkAmount(amount);
        checkBorrower(borrower);

        if (out.containsKey(borrower)) {
            out.put(borrower, out.get(borrower) + amount);
            borrower.in.put(this, borrower.in.get(this) + amount);
        } else {
            out.put(borrower, amount);
            borrower.in.put(this, amount);

            if (ownerGraph != null) {
                ownerGraph.edgeAmount++;
            }
        }

        if (ownerGraph != null) {
            ownerGraph.flow += amount;
        }

        equity += amount;
        borrower.equity -= amount;
    }
    
    /**
     * Remove the borrower of this node.
     * 
     * @param borrower the borrower to remove.
     */
    public void removeBorrower(final Node borrower) {
        if (out.containsKey(borrower)) {
            long w = out.get(borrower);
            out.remove(borrower);
            borrower.in.remove(this);
            this.equity -= w;
            borrower.equity += w;

            if (ownerGraph != null) {
                ownerGraph.edgeAmount--;
                ownerGraph.flow -= w;
            }
        }
    }

    /**
     * Clear this node from all loans coming in and out.
     */
    public void clear() {
        Iterator<Node> iterator = iterator();

        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }

        iterator = parentIterable().iterator();

        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
        }
    }

    /**
     * Returns the amount of borrowers.
     * 
     * @return the amount of borrowers.
     */
    public int getBorrowerAmount() {
        return this.out.size();
    }

    /**
     * Returns the amount of lenders.
     * 
     * @return the amount of lenders. 
     */
    public int getLenderAmount() {
        return this.in.size();
    }

    /**
     * Retrieves the loan amount to borrower.
     * 
     * @param borrower the borrower to query.
     * @return the amount of resources lent to borrower.
     */
    public long getLoanTo(final Node borrower) {
        if (out.containsKey(borrower) == false) {
            return 0L;
        }

        return out.get(borrower);
    }

    /**
     * Returns the string representation of this node.
     * @return 
     */
    public String toString() {
        return "[Node " + name + "; equity: " + getEquity() + " ]";
    }

    /**
     * Returns the hash code of this node. Depends only on the name of
     * the node.
     * 
     * @return the hash code of this node. 
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Returns <code>true</code> if and only if the two nodes have the 
     * same name (identity).
     * 
     * @param o the object to test against.
     * @return <code>true</code> if and only if <code>o</code> is a node
     * and has the same name.
     */
    @Override
    public boolean equals(Object o) {
        return ((Node) o).name.equals(this.name);
    }

    /**
     * Returns the iterator over this node's borrowers.
     * 
     * @return the iterator over this node's borrowers.
     */
    @Override
    public Iterator<Node> iterator() {
        return new ChildIterator();
    }

    /**
     * Returns the iterable over this nodes lenders.
     * 
     * @return the iterable over this nodes lenders.
     */
    public Iterable<Node> parentIterable() {
        return new ParentIterable();
    }

    /**
     * Return the equity of this node.
     * 
     * @return the equity of this node.
     */
    public long getEquity() {
        return this.equity;
    }

    /**
     * Checks whether the amount is positive.
     * 
     * @param amount the amount to check. 
     */
    private void checkAmount(final long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Illegal amount given: " + amount);
        }
    }

    /**
     * Checks that borrower is not null and not this.
     * 
     * @param borrower the borrower to check. 
     */
    private void checkBorrower(final Node borrower) {
        if (borrower == null) {
            throw new NullPointerException("Borrower is null.");
        }

        if (borrower == this) {
            throw new IllegalArgumentException("Requesting a self-loop.");
        }
    }

    /**
     * This class implements the iterator over this node's borrowers.
     */
    private class ChildIterator implements Iterator<Node> {

        /**
         * The actual iterator.
         */
        private Iterator<Node> iterator = Node.this.out.keySet().iterator();
        
        /**
         * Holds the node last returned by <code>next</code>.
         */
        private Node lastReturned;

        /**
         * Returns <code>true</code> if and only if there is more 
         * nodes to iterate.
         * 
         * @return <code>true</code> if and only if there is more nodes
         * to iterate.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next node.
         * 
         * @return the next node.
         */
        @Override
        public Node next() {
            return (lastReturned = iterator.next());
        }

        /**
         * Removes the node from this node's borrowers.
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new NoSuchElementException(
                        "There is no current element to remove.");
            }

            long w = Node.this.getLoanTo(lastReturned);

            iterator.remove();
            lastReturned.in.remove(Node.this);
            lastReturned = null;

            if (ownerGraph != null) {
                ownerGraph.edgeAmount--;
                ownerGraph.flow -= w;
            }
        }
    }

    /**
     * Returns the iterable over this node's lenders.
     */
    private class ParentIterable implements Iterable<Node> {
        
        /**
         * Returns the iterator over this node's lenders.
         * 
         * @return the iterator over this node's lenders.
         */
        @Override
        public Iterator<Node> iterator() {
            return new ParentIterator();
        }
    }

    /**
     * This class implements the iterator over this node's lenders.
     */
    private class ParentIterator implements Iterator<Node> {

        /**
         * The actual iterator.
         */
        private Iterator<Node> iterator = Node.this.in.keySet().iterator();
        
        /**
         * The node last returned.
         */
        private Node lastReturned;

        /**
         * Return <code>true</code> if this iterator has more nodes
         * to iterate.
         * 
         * @return <code>true</code> if this iterator has more nodes to
         * iterate.
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Returns the next node or throws 
         * <code>NoSuchElementException</code> if there is no more
         * nodes.
         * 
         * @return the next element. 
         */
        @Override
        public Node next() {
            return (lastReturned = iterator.next());
        }

        /**
         * Removes this lender from this node's lender list.
         */
        @Override
        public void remove() {
            if (lastReturned == null) {
                throw new NoSuchElementException(
                        "There is no current element to remove.");
            }

            long w = lastReturned.getLoanTo(Node.this);
            
            iterator.remove();
            lastReturned.out.remove(Node.this);
            lastReturned = null;

            if (ownerGraph != null) {
                ownerGraph.edgeAmount--;
                ownerGraph.flow -= w;
            }
        }
    }
}
