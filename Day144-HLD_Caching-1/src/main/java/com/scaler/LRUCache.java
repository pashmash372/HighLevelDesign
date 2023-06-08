package com.scaler;

import java.util.HashMap;

public class LRUCache {
}

class Solution {

    Node head;
    Node tail;
    int N;
    int MAX;
    HashMap<Integer, Node> mMap;
    public Solution(int capacity) {
        head = null;
        tail = null;
        MAX = capacity;
        N = 0;
        mMap = new HashMap<>();
    }

    public void updateAccessList(Node node) {
        Node temp = node.prev;
        temp.next = node.next;
        temp = node.next;
        if (temp != null) temp.prev = node.prev;

        node.next = head;
        head.prev = node;
        node.prev = null;
        head = node;
    }

    public int get(int key) {
        if (N == 0) return -1;

        if (mMap.containsKey(key)) {
            Node node = mMap.get(key);

            if (key == head.key) {
                return node.val;
            }
            if (tail.key == key) {
                tail = tail.prev;
            }

            updateAccessList(node);

            return node.val;
        }


        return -1;
    }

    public void set(int key, int value) {

        if (mMap.containsKey(key)) {

            Node node = mMap.get(key);

            if (node.key == head.key) {
                node.val = value;
                return;
            }

            if (tail.key == key) {
                tail = tail.prev;
            }

            updateAccessList(node);

            node.val = value;

            return;
        }

        if (N == MAX) {
            if (tail != null) {
                mMap.remove(tail.key);
                tail = tail.prev;

                if (tail != null) {
                    tail.next.prev = null;
                    tail.next = null;
                }
                N--;
            }
        }

        Node node = new Node(key, value);
        node.next = head;
        if (head != null) head.prev = node;

        head = node;
        N++;

        if (N == 1) tail = head;

        mMap.put(key, node);
    }

    static class Node {
        int key;
        int val;
        Node prev, next;

        public Node(int key, int val) {
            this.key = key;
            this.val = val;
        }
    }
}

/** https://www.scaler.com/academy/mentee-dashboard/class/56977/assignment/problems/239?navref=cl_tt_lst_nm*/

/**
 * Q1. LRU Cache
 * Solved
 * feature icon
 * Using hints is now penalty free
 * Use Hint
 * Design and implement a data structure for Least Recently Used (LRU) cache. It should support the following operations: get and set.
 *
 * get(key) - Get the value (will always be positive) of the key if the key exists in the cache, otherwise return -1.
 * set(key, value) - Set or insert the value if the key is not already present. When the cache reaches its capacity, it should invalidate the least recently used item before inserting the new item.
 * The LRUCache will be initialized with an integer corresponding to its capacity. Capacity indicates the maximum number of unique keys it can hold at a time.
 *
 * Definition of "least recently used" : An access to an item is defined as a get or a set operation of the item. "Least recently used" item is the one with the oldest access time.
 *
 * NOTE: If you are using any global variables, make sure to clear them in the constructor.
 *
 * Example :
 *
 * Input :
 *          capacity = 2
 *          set(1, 10)
 *          set(5, 12)
 *          get(5)        returns 12
 *          get(1)        returns 10
 *          get(10)       returns -1
 *          set(6, 14)    this pushes out key = 5 as LRU is full.
 *          get(5)        returns -1
 * Expected Output
 * Enter your input as per the following guideline:
 * There are 1 lines in the input
 *
 * Line 1 ( Corresponds to arg 1 ) : The line starts with a pair of number numOperations, capacity. capacity is the number your constructor is initialized with.
 * Then numOperation operations follow.
 * Each operation is either :
 *  * G  : This corresponds to a function call get()
 *  * S   : This corresponds to a function call set(num1, num2)
 * Note that the function calls are made in order.  */


/**
 * Lets approach this question bit by bit.
 * If lets say you never had to remove entries from the LRU cache because we had enough space, what would you use to support and get and set ?
 * A simple map / hashmap would suffice.
 * <p>
 * Alright, lets now look at the second part which is where the eviction comes in. We need a data structure which at any given instance can give me the least recently used objects in order. Lets see if we can maintain a linked list to do it. We try to keep the list ordered by the order in which they are used. So whenever, a get operation happens, we would need to move that object from a certain position in the list to the front of the list. Which means a delete followed by insert at the beginning.
 * <p>
 * Insert at the beginning of the list is trivial. How do we achieve erase of the object from a random position in least time possible ?
 * <p>
 * How about we maintain another map which stores the pointer to the corresponding linked list node.
 * <p>
 * Ok, now when we know the node, we would need to know its previous and next node in the list to enable the deletion of the node from the list. We can get the next in the list from next pointer ? What about the previous node ? To encounter that, we make the list doubly linked list.
 * <p>
 * Now, can you think of an approach using doubly linked list and the map(s) ? Solution Approach
 * As discussed in the previous hint, we solve this problem using :
 * <p>
 * originalMap : A hashmap which stores the orginial key to value mapping
 * accessList : A doubly linked list which has keys ordered by their access time ( Most recently used key in front of the list to least recently used key at the end of the list ).
 * addressMap : A hashmap which saves the mapping of key to address of the key in accessList.
 * Lets see now how the get and set operation would work :
 * <p>
 * get(key) :
 * Look for the value corresponding to key in originalMap.
 * If key is not found, we don’t need to change accessList. So, return -1.
 * If key is found, then we need to move the node corresponding to the key to front of the list in accessList.
 * To do that, we find the address of the node for key from addressMap. We make the node->prev->next = node->next;, node->next->prev = node->prev;, node->prev = NULL; node->next = head; head->prev = node;
 * We update the head of the accessList to be node now.
 * <p>
 * set(key, value)
 * If the key is a new entry (it does not exist in the originalMap) AND the cache is full(size = capacity), then we would need to remove the least recently used key lkey.
 * We know that this key is the key corresponding to the last node in accessList which is accessible in O(1). To evict, we delete the last node from accessList, and the entry corresponding to lkey(from last node) in addressMap and originalMap.
 * <p>
 * Post this, there are 2 cases.
 * <p>
 * key is a new entry and is not present in originalMap. In this case, a new node is created for key and inserted at the head of accessList. A new (key,value) entry is created into originalMap and addressMap accordingly.
 * key is present in the map, in which case the value needs to be updated. We update the value in the originalMap and then we update the accessList for key exactly the way we did in the case of get operation.
 * A couple of insights for clean code :
 * <p>
 * Note that the update of accessList for key when key is present is a common operation in get and a subcase of set function. We should create a function for it and call that function in both cases to reduce redundancy.
 * Also, note that originalMap and addressMap are always of the same size with the same keys ( One with value and the other with address in linkedlist ). If you want to manage less maps, you can just have a masterMap which maps key to a pair of (value, address_in_list)
 */


/** Solution Approach
 * As discussed in the previous hint, we solve this problem using :
 *
 * originalMap : A hashmap which stores the orginial key to value mapping
 * accessList : A doubly linked list which has keys ordered by their access time ( Most recently used key in front of the list to least recently used key at the end of the list ).
 * addressMap : A hashmap which saves the mapping of key to address of the key in accessList.
 * Lets see now how the get and set operation would work :
 *
 * get(key) :
 * Look for the value corresponding to key in originalMap.
 * If key is not found, we don’t need to change accessList. So, return -1.
 * If key is found, then we need to move the node corresponding to the key to front of the list in accessList.
 * To do that, we find the address of the node for key from addressMap. We make the node->prev->next = node->next;, node->next->prev = node->prev;, node->prev = NULL; node->next = head; head->prev = node;
 * We update the head of the accessList to be node now.
 *
 * set(key, value)
 * If the key is a new entry (it does not exist in the originalMap) AND the cache is full(size = capacity), then we would need to remove the least recently used key lkey.
 * We know that this key is the key corresponding to the last node in accessList which is accessible in O(1). To evict, we delete the last node from accessList, and the entry corresponding to lkey(from last node) in addressMap and originalMap.
 *
 * Post this, there are 2 cases.
 *
 * key is a new entry and is not present in originalMap. In this case, a new node is created for key and inserted at the head of accessList. A new (key,value) entry is created into originalMap and addressMap accordingly.
 * key is present in the map, in which case the value needs to be updated. We update the value in the originalMap and then we update the accessList for key exactly the way we did in the case of get operation.
 * A couple of insights for clean code :
 *
 * Note that the update of accessList for key when key is present is a common operation in get and a subcase of set function. We should create a function for it and call that function in both cases to reduce redundancy.
 * Also, note that originalMap and addressMap are always of the same size with the same keys ( One with value and the other with address in linkedlist ). If you want to manage less maps, you can just have a masterMap which maps key to a pair of (value, address_in_list)*/