//Created by:
// Andrew Bostros cssc0727
// Jan Domingo cssc0749
package edu.sdsu.cs.datastructures;

import java.util.LinkedList;

public class UnbalancedMap <K extends Comparable<K>,V> implements IMap<K,V> {

    public UnbalancedMap(IMap<K, V> original) {
        for (K keys : original.keyset()) {
            V value = original.getValue(keys);
            add(keys, value);
        }
    }

    public UnbalancedMap() {
        root = null;
        currentSize = 0;
    }

    //------------Node Class------------
    private class Node<K, V> {
        private K key;
        private V value;
        private Node<K, V> leftChild;
        private Node<K, V> rightChild;

        public Node(K k, V v) {
            this.key = k;
            this.value = v;
            leftChild = rightChild = null;
        }
    }

    Node root;
    int currentSize;


    /**
     * Indicates if the map contains the object identified by the key inside.
     *
     * @param key The object to compare against
     * @return true if the parameter object appears in the structure
     */
    //check if that function return the value or null
    public boolean contains(K key) {
        return containsh(root, key);
    }

    private boolean containsh(Node<K, V> n, K key) {

        if (n == null) {
            return false;
        }
        int cmp = ((Comparable<K>) key).compareTo((K) n.key);
        if (cmp < 0) {
            return containsh(n.leftChild, key);

        } else if (cmp > 0) {
            return containsh(n.rightChild, key);
        } else {
            return true;
        }
    }

    //------------Contains (K key) helper function------------
    public V helperFunction(K key, Node<K, V> node) {
        if (node == null) {
            return null;
        }
        if (key == node.key) {
            return node.value;
        } else if (key.compareTo(node.key) < 0) {
            helperFunction(key, node.rightChild);
        } else if (key.compareTo(node.key) > 0) {
            helperFunction(key, node.leftChild);
        }
        return null;
    }


    /**
     * Adds the given key/value pair to the dictionary.
     *
     * @param key
     * @param value
     * @return false if the dictionary is full, or if the key is a duplicate.
     * Returns true if addition succeeded.
     */
    public boolean add(K key, V value) {
        if (root == null) {
            root = new Node<K, V>(key, value);
            ++currentSize;
            return true;
        } else {
            insert(root, key, value);
            return true;
        }
    }

    private Node insert(Node<K, V> n, K key, V value) {
        if (n == null) {
            Node<K, V> p = new Node<K, V>(key, value);
            n = p;
            currentSize++;
            return n;
        } else {
            int cmp = ((Comparable<K>) key).compareTo((K) n.key);
            if (cmp < 0) {
                ++currentSize;
                n.leftChild = insert(n.leftChild, key, value);
            } else if (cmp > 0) {
                ++currentSize;
                n.rightChild = insert(n.rightChild, key, value);
            } else if (cmp == 0) {
                return n;
            }
        }
        return n;
    }


    /**
     * Deletes the key/value pair identified by the key parameter.
     *
     * @param key
     * @return The previous value associated with the deleted key or null if not
     * present.
     */

    public V delete(K key) {
        if (isEmpty() == true) {
            return null;
        } else {
            currentSize--;
            return (V) deleteHelper(key, root, null, false);
        }
    }

    //n is the current node
    private V deleteHelper(K key, Node<K, V> n, Node<K, V> parent,
                           boolean wasLeft) {
        V returnValue = null;
        if (n == null) {
            return null;
        }

        //Search for the key that needs to be deleted
        //After searching, n will be the node to delete
        else if (key.compareTo(n.key) < 0) {
            return deleteHelper(key, n.leftChild, n, true);
        } else if (key.compareTo(n.key) > 0) {
            return deleteHelper(key, n.rightChild, n, false);
        }


        //----------Deletes the root node if it is the only node----------
        else if (n == root && currentSize == 0) {
            returnValue = n.value;
            root = n = null;
            return returnValue;
        }

        //----------Deletes one child from the root----------
        else if (n == root && ((n.rightChild != null && n.leftChild == null) ||
                (n.rightChild == null && n.leftChild != null))) {
            if (n.rightChild != null && n.leftChild == null) {
                root = n.rightChild;
                return n.value;
            } else {
                root = n.leftChild;
                return n.value;
            }
        }

        //-----Deletes the node with one child and replaces with subchild-----
        else if (n.rightChild == null && n.leftChild != null) {
            parent.leftChild = n.leftChild;
            return n.value;
        } else if (n.rightChild != null && n.leftChild == null) {
            parent.rightChild = n.rightChild;
            return n.value;
        }


        //----------Deletes the root with two subtrees----------
        else if (n.rightChild != null && n.leftChild != null && n == root) {
            V v = null;
            //Step 1: Find the deletion node
            //The deletion node is n

            Node<K, V> successorNode = n.rightChild;
            Node<K, V> successorParent = n;

            //Special case detection of if right subtree has no left branch
            if (successorNode.leftChild == null) {
                n.key = successorNode.key;
                n.rightChild = successorNode.rightChild;
                return n.value;
            }

            //Step 2: Find the successor node in the right subtree of n
            //Traverses to the left child and down until there is no left child
            while (successorNode.leftChild != null) {
                //Keep tracks of parents while traversing
                successorParent = successorNode;
                //Sets the node as its left child and loops again
                successorNode = successorNode.leftChild;
            }

            //Step 3: Put the successor node where n previously was
            //Value of the node to delete which will be returned later
            returnValue = n.value;
            n.key = successorNode.key;
            n.value = successorNode.value;

            //Step 4: Delete the successor node
            successorParent.leftChild = successorNode.rightChild;
            successorNode = null;
            successorParent = null;
            return returnValue;
        }


        //-------Deletes the node that is not the root with two subtrees--------
        else if (n.rightChild != null && n.leftChild != null) {
            V v = null;
            //Step 1: Find the deletion node
            //The deletion node is n

            Node<K, V> successorNode = n;
            Node<K, V> successorParent = n;

            //Step 2: Find the successor node in the right subtree of n
            //Traverses to the left child and down until there is no left child
            while (successorNode.leftChild != null) {
                //Keep tracks of parents while traversing
                successorParent = successorNode;
                //Sets the node as its left child and loops again
                successorNode = successorNode.leftChild;
            }

            //Step 3: Put the successor node where n previously was
            //Value of the node to delete which will be returned later
            returnValue = n.value;
            n.key = successorNode.key;
            n.value = successorNode.value;

            //Step 4: Delete the successor node FIX THIS
            successorParent.leftChild = successorNode.rightChild;
            successorNode = null;
            successorParent = null;
            return returnValue;
        }

        //----------Passes the deletion of odd keys----------
        else if ((n.rightChild == null) && (n.leftChild == null) &&
                wasLeft == true) {
            returnValue = n.value;
            parent.leftChild = null;
            return returnValue;
        } else {
            returnValue = n.value;
            parent.rightChild = null;
            return returnValue;
        }

    }

    /**
     * Retreives, but does not remove, the value associated with the provided
     * key.
     *
     * @param key The key to identify within the map.
     * @return The value associated with the indicated key.
     */
    public V getValue(K key) {
        if (root == null)
            return null;
        Node<K, V> current = root;
        while (((Comparable<K>) current.key).compareTo((K) key) != 0) {
            if (((Comparable<K>) key).compareTo((K) current.key) < 0)
                current = current.leftChild;
            else
                current = current.rightChild;
            if (current == null)
                return null;
        }
        return current.value;
    }

    /**
     * Returns a key in the map associated with the provided value.
     *
     * @param value The value to find within the map.
     * @return The first key found associated with the indicated value.
     */
    public K getKey(V value) {
        if (root == null)
            return null;
        Node<K, V> current = root;
        while (((Comparable<V>) current.value).compareTo((V) value) != 0) {
            if (((Comparable<K>) value).compareTo((K) current.value) < 0)
                current = current.leftChild;
            else
                current = current.rightChild;
            if (current == null)
                return null;
        }
        return current.key;
    }


    /**
     * Returns all keys associated with the indicated value contained within the
     * map.
     *
     * @param value The value to locate within the map.
     * @return An iterable object containing all keys associated with the
     * provided value.
     */


    LinkedList<K> getKeysList = new LinkedList<>();
    public Iterable<K> getKeys(V value) {
        getKeysList.clear();
        inOrderIterator(root, value);
        return getKeysList;
    }

    //return a linked list with all the keys in it
    //use in order traversal to grab the keys and put it in the list
    private void inOrderIterator(Node<K, V> n, V value) {
        if (n != null) {
            inOrderIterator(n.leftChild, value);
            if (n.value == value)
                getKeysList.add(n.key);
            inOrderIterator(n.rightChild, value);
        }
    }


    /**
     * Indicates the count of key/value entries stored inside the map.
     *
     * @return A non-negative number representing the number of entries.
     */
    public int size() {
        if(root==null) {
            return 0;
        }
        else {
            return sizeHelperFunction(root);
        }
    }

    //---------Size Helper Function----------
    private int sizeHelperFunction(Node n) {
        if (n == null)
            return 0;
        else
            return (sizeHelperFunction(n.leftChild) + 1 +
                    sizeHelperFunction(n.rightChild));
    }


    /**
     * Indicates if the dictionary contains any items.
     *
     * @return true if the dictionary is empty, false otherwise.
     */
    public boolean isEmpty() {
        if (root == null && currentSize == 0){
            return true;
        } else {
            return false;
        }
    }


    /***
     * Returns the map to an empty state ready to accept new entries.
     */
    public void clear() {
        root = null;
        currentSize = 0;
        }


        /**
     * Provides an Iterable object of the keys in the dictionary.
     * <p>
     * The keys provided by this method must appear in their natural, ascending,
     * order.
     *
     * @return An iterable set of keys.
     */
    public Iterable<K> keyset() {
        LinkedList<K> keysetList = new LinkedList<>();
        return keyIterator(keysetList, root);

        }

    //---------Key Iterator Helper Class---------
    private Iterable<K> keyIterator (LinkedList<K> keysetList,
                                     Node<K,V> current)
    {
        if (current == null) {
            return keysetList;
        }
        keyIterator(keysetList, current.leftChild);
        keysetList.add(current.key);
        keyIterator(keysetList,current.rightChild);
        return keysetList;
    }


    /**
     * Provides an Iterable object of the keys in the dictionary.
     * <p>
     * The values provided by this method must appear in an order matching the
     * keyset() method. This object may include duplicates if the data structure
     * includes duplicate values.
     *
     * @return An iterable object of all the dictionary's values.
     */
    public Iterable<V> values() {
        LinkedList<V> valueList = new LinkedList<>();
        return valueIterator(valueList, root);
        }

    //------------Value iterator Helper Classes------------
    private Iterable<V> valueIterator (LinkedList<V> valueList,
                                       Node<K,V>
            current)
    {
        if (current == null) {
            return valueList;
        }
        valueIterator(valueList, current.leftChild);
        valueList.add(current.value);
        valueIterator(valueList, current.rightChild);
        return valueList;
    }
}




