package com.scaler;

public class TypeheadSearch {

    /** solution in cpp and python
     *  present at the end*/
}


/**
 * Q2. Typehead Search
 * Solved
 * feature icon
 * Using hints is now penalty free
 * Use Hint
 * Problem Description
 *
 * You need to implement search typeahead on a single machine. There are 2 kinds of functions that you have to implement :-
 *
 * incrementSearchTermFrequency(string search_term, int increment) - Increase the frequency of search_term by increment .
 * findTopXSuggestion(string queryPrefix, int X) - Find the top X (X <= 5) search terms i.e. 5 terms that have queryPrefix as a strict prefix and have the highest frequency in the eligible set.
 * Note:- For two strings with the same frequency, we give more preference to the lexicographically larger one. For the second function, if there are fewer than X strings with the given prefix then add empty strings. Also, the strings must be returned after sorting them lexicographically.
 *
 *
 * Constraints
 *
 * Sum of increment over all test cases <= 109
 *
 * 1 <= X <= 5
 *
 * 'a' <= queryPrefix <= 'z'
 *
 *
 * Sample Input
 *
 * incrementSearchTermFrequency(“michelleobama”, 100)
 *
 * incrementSearchTermFrequency(“michaeljackson”, 90)
 *
 * incrementSearchTermFrequency(“michaeljordan”, 120)
 *
 * findTopXSuggestion(“mic”, 2) - This should return (“michaeljordan”, “michelleobama”).*/



/** Hint 1
 * Create a Prefix Trie.
 *
 * Can you store the answer, i.e., the top 5 highest frequency words with each node, by maintaining an array?*/



/**
 * Solution Approach
 * Create a prefix Trie, but the node of a Trie will also contain an array that will denote the answer for each prefix.
 *
 * How?
 *
 * Sort the given dictionary in order of its frequency.
 * Now insert the word in the Trie and update the answer array if its size is less than 5 or its frequency is greater than
 * one of the existing words in the answer array.
 *
 * For each query, search for the prefix. If no word exists in the trie with the given prefix return empty strings.
 * Otherwise, the answer array at the end of the prefix will be the answer.*/

/**
 * struct TrieNode {
 *     TrieNode * child[26];
 *     vector < pair < int, string >> bestFive;
 *     bool isend;
 * };
 *
 * TrieNode * getNode() {
 *     TrieNode * pNode = new TrieNode;
 *     pNode -> isend = false;
 *     for (int i = 0; i < 26; i++)
 *         pNode -> child[i] = NULL;
 *     return pNode;
 * }
 *
 * class TypeHead {
 *     public:
 *         TrieNode * root;
 *     map < string, int > freq;
 *     TypeHead() {
 *         this -> root = getNode();
 *         this -> freq.clear();
 *     }
 *     void incrementSearchTermFrequency(string search_term, int increment) {
 *         TrieNode * temp = this -> root;
 *         this -> freq[search_term] += increment;
 *         for (int i = 0; i < search_term.size(); i++) {
 *             int chr_val = search_term[i] - 'a';
 *             if (temp -> child[chr_val] == NULL) {
 *                 temp -> child[chr_val] = getNode();
 *             }
 *             temp = temp -> child[chr_val];
 *             sort(temp -> bestFive.begin(), temp -> bestFive.end());
 *             int found = 0;
 *             for (auto & x: temp -> bestFive) {
 *                 if (x.second == search_term) {
 *                     found = 1;
 *                     x.first = this -> freq[search_term];
 *                 }
 *             }
 *             if (found == 0 and(temp -> bestFive.size() < 5 or temp -> bestFive[0].first <= this -> freq[search_term])) {
 *                 if (temp -> bestFive.size() < 5) {
 *                     temp -> bestFive.push_back({
 *                         this -> freq[search_term],
 *                         search_term
 *                     });
 *                 } else if (this -> freq[search_term] > temp -> bestFive[0].first or search_term > temp -> bestFive[0].second) {
 *                     temp -> bestFive[0] = {
 *                         this -> freq[search_term],
 *                         search_term
 *                     };
 *                 }
 *             }
 *             sort(temp -> bestFive.begin(), temp -> bestFive.end());
 *         }
 *         temp -> isend = true;
 *     }
 *     vector < string > findTopXSuggestion(string queryPrefix, int X) {
 *         TrieNode * temp = root;
 *         vector < string > ans;
 *         for (int i = 0; i < queryPrefix.size(); i++) {
 *             int chr_val = queryPrefix[i] - 'a';
 *             if (temp -> child[chr_val] == NULL) {
 *                 temp = temp -> child[chr_val];
 *                 break;
 *             }
 *             temp = temp -> child[chr_val];
 *         }
 *         if (temp != NULL) {
 *             vector < pair < int, string >> v = temp -> bestFive;
 *             sort(v.begin(), v.end());
 *             for (int j = v.size() - 1; j >= 0 and ans.size() < X; j--) {
 *                 ans.push_back(v[j].second);
 *             }
 *         }
 *         while (ans.size() < X) {
 *             ans.push_back("");
 *         }
 *         sort(ans.begin(), ans.end());
 *         return ans;
 *     }
 * };*/


/**
 * class TrieNode:
 *     def __init__(self):
 *         self.child = [None for j in range(26)]
 *         self.bestFive = []
 *         self.isend = False
 *
 * class TypeHead:
 *     def __init__(self):
 *         self.root = TrieNode()
 *         self.freq = {}
 *
 *
 *     def incrementSearchTermFrequency(self, search_term, increment):
 *         temp = self.root;
 *         if search_term not in self.freq:
 *             self.freq[search_term] = 0
 *         self.freq[search_term] += increment;
 *
 *         for i in search_term:
 *             chr_val = ord(i) - ord('a')
 *             if (temp.child[chr_val] == None):
 *                 temp.child[chr_val] = TrieNode()
 *             temp = temp.child[chr_val]
 *
 *             temp.bestFive.sort()
 *             found = 0
 *             for i in range(len(temp.bestFive)):
 *                 if(temp.bestFive[i][1] == search_term):
 *                     found = 1
 *                     temp.bestFive[i] = (self.freq[search_term], search_term)
 *
 *             if(found == 0 and (len(temp.bestFive) < 5 or temp.bestFive[0][0] <= self.freq[search_term])):
 *                 if(len(temp.bestFive) < 5):
 *                     temp.bestFive.append((self.freq[search_term], search_term))
 *                 elif(self.freq[search_term] > temp.bestFive[0][0] or search_term > temp.bestFive[0][1]):
 *                     temp.bestFive[0] = (self.freq[search_term], search_term)
 *
 *             temp.bestFive.sort()
 *
 *         temp.isend = True
 *
 *
 *     def findTopXSuggestion(self, queryPrefix, X):
 *         temp = self.root
 *         ans = []
 *
 *         for i in queryPrefix:
 *             chr_val = ord(i) - ord('a')
 *             if (temp.child[chr_val] == None):
 *                 temp = None
 *                 break
 *             temp = temp.child[chr_val];
 *
 *         if (temp != None):
 *             v = temp.bestFive
 *             v.sort()
 *             for j in range(len(v) - 1, -1, -1):
 *                 if (len(ans) == X):
 *                     break
 *                 ans.append(v[j][1])
 *
 *         while (len(ans) < X):
 *             ans.append("")
 *         ans.sort()
 *         return ans
 *       */