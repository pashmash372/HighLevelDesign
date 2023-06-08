package com.scaler;

public class BloomFilter {
    final static int sz = 5000000;
    static int[] bitarray = new int[sz];
    static int arrSize = sz;

    // hash 1
    static int h1(String s) {
        long hash = 21;
        for (int i = 0; i < s.length(); i++) {
            hash = hash + (int) (s.charAt(i));
            hash = hash % arrSize;
        }
        return (int) (hash);
    }

    // hash 2
    static int h2(String s) {
        long hash = (int) (1e5 + 7);
        for (int i = 0; i < s.length(); i++) {
            hash = hash * 37 + (int) (s.charAt(i));
            hash = hash % arrSize;
        }
        return (int) (hash) % arrSize;
    }

    // hash 3
    static int h3(String s) {
        long hash = 7;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * 31 + (int) (s.charAt(i))) % arrSize;
        }
        return (int) (hash) % arrSize;
    }

    // hash 4
    static int h4(String s) {
        long hash = 3;
        for (int i = 0; i < s.length(); i++) {
            hash += hash * 7 + (int) (s.charAt(i)) * 19;
            hash = hash % arrSize;
        }
        return (int) (hash);
    }

    // lookup operation
    static Boolean lookup(String s) {
        int a = h1(s);
        int b = h2(s);
        int c = h3(s);
        int d = h4(s);
        return bitarray[a] == 1 && bitarray[b] == 1 && bitarray[c] == 1 && bitarray[d] == 1;
    }

    // insert operation
    static void insert(String s) {

        int a = h1(s);
        int b = h2(s);
        int c = h3(s);
        int d = h4(s);

        bitarray[a] = 1;
        bitarray[b] = 1;
        bitarray[c] = 1;
        bitarray[d] = 1;

    }
}


/**
 * Q1. Bloom Filter
 * Solved
 * feature icon
 * Using hints is now penalty free
 * Use Hint
 * A Bloom filter is a space-efficient probabilistic data structure that is used to test whether an element is a member of a set.
 * <p>
 * Implement a Bloom Filter. Operations that the Bloom Filter should support are :
 * <p>
 * insert(X) - Inserts the element X in the Bloom Filter.
 * lookup(X) - Checks whether the element X is already present in Bloom Filter with a positive false probability. Returns a boolean value.
 * <p>
 * Note:- The Bloom Filter should work with an error rate of less than 5%.
 * Solution Approach
 * Bloom filter is a space-efficient probabilistic data structure that tells whether an element may be in a set or definitely is not.
 * If we look up an item in the Bloom filter, we can get two possible results.
 * <p>
 * The item is not present in the set: True negative.
 * The item might be present in the set: Can be either a False positive or True positive.
 * Suppose we want to compare two strings. Instead of storing the entire string,
 * we compute the hash of the string and then compare the hashes.
 * Computing hash and then comparing them takes O(1) time instead of the O(n) time to compare the strings directly. If both hashes are unequal, we can definitely say that strings are unequal. Otherwise, both strings may be equal!
 */

/**
 * Solution Approach
 * Bloom filter is a space-efficient probabilistic data structure that tells whether an element may be in a set or definitely is not.
 * If we look up an item in the Bloom filter, we can get two possible results.
 *
 * The item is not present in the set: True negative.
 * The item might be present in the set: Can be either a False positive or True positive.
 * Suppose we want to compare two strings. Instead of storing the entire string,
 * we compute the hash of the string and then compare the hashes.
 * Computing hash and then comparing them takes O(1) time instead of the O(n) time to compare the strings directly. If both hashes are unequal, we can definitely say that strings are unequal. Otherwise, both strings may be equal!*/