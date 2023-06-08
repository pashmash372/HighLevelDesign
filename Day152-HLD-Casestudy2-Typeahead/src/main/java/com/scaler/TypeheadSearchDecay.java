package com.scaler;

public class TypeheadSearchDecay {
    /** solution present in cpp and python
     * at the end of the file */
}
/** https://www.scaler.com/academy/mentee-dashboard/class/56966/assignment/problems/39195/?navref=cl_pb_nv_tb*/

/**
 * Q3. Typehead Search Decay
 * Solved
 * feature icon
 * Using hints is now penalty free
 * Use Hint
 * Problem Description
 *
 * You need to implement search typeahead on a single machine with daily frequency decay. Basically, you need to implement 3 kinds of functions. :-
 *
 * incrementSearchTermFrequency(string search_term, int increment) - Increase the frequency of search_term by increment .
 * findTopXSuggestion(string queryPrefix, int X) - Find the top X (X <= 5) search terms i.e. 5 terms that have queryPrefix as a strict prefix and have the highest frequency in the eligible set.
 * dayPasses(decayFactor) - This is an indicator of transition to another day with decayFactor. The frequency of all existing search terms gets divided by decayFactor.
 * Note:- For two strings with the same frequency, we give more preference to the lexicographically larger one. For the second function, if there are fewer than X strings with the given prefix then add empty strings. Also, the strings must be returned after sorting them lexicographically. The division by decayFactor is a float division.
 *
 *
 * Constraints
 *
 * Sum of increment over all test cases <= 109
 *
 * Product of decayFactor over all test cases <= 109
 *
 * 1 <= X <= 5
 *
 * 2 <= decayFactor <= 5
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
 * dayPasses(2)
 *
 * incrementSearchTermFrequency(“mickeymouse”, 51)
 *
 * findTopXSuggestion(“mic”, 2) - This should return (“michaeljordan”, “mickeymouse”).*/


/**
 * Hint 1
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
 * To handle the delay we use lazy propagation, we keep a track of the last updation time for each word. Now when we reach a
 * node, then only we update the frequency of the words there.
 *
 * For each query, search for the prefix. If no word exists in the trie with the given prefix return empty strings.
 * Otherwise, the answer array at the end of the prefix will be the answer.*/


/**
 * struct TrieNode {
 *     TrieNode * child[26];
 *     vector < pair < double, string >> bestFive;
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
 *     TrieNode * root;
 *     map < string, pair < double, int > > freq;
 *     int decay;
 *     TypeHead() {
 *         this -> root = getNode();
 *         this -> freq.clear();
 *         this -> decay = 1;
 *     }
 *     void updateFrequency(string search_term) {
 *         double factor = 1;
 *         if (this -> freq[search_term].first != 0) {
 *             factor = this -> decay / this -> freq[search_term].second;
 *         }
 *         this -> freq[search_term] = {
 *             (this -> freq[search_term].first / factor),
 *             this -> decay
 *         };
 *     }
 *     void incrementSearchTermFrequency(string search_term, int increment) {
 *         TrieNode * temp = this -> root;
 *         updateFrequency(search_term);
 *         this -> freq[search_term].first += increment;
 *         for (int i = 0; i < search_term.size(); i++) {
 *             int chr_val = search_term[i] - 'a';
 *             if (temp -> child[chr_val] == NULL) {
 *                 temp -> child[chr_val] = getNode();
 *             }
 *             temp = temp -> child[chr_val];
 *             for (auto & x: temp -> bestFive) {
 *                 updateFrequency(x.second);
 *                 x.first = this -> freq[x.second].first;
 *             }
 *             sort(temp -> bestFive.begin(), temp -> bestFive.end());
 *             int found = 0;
 *             for (auto & x: temp -> bestFive) {
 *                 if (x.second == search_term) {
 *                     found = 1;
 *                     x.first = this -> freq[search_term].first;
 *                 }
 *             }
 *             if (found == 0 and(temp -> bestFive.size() < 5 or temp -> bestFive[0].first <= this -> freq[search_term].first)) {
 *                 if (temp -> bestFive.size() < 5) {
 *                     temp -> bestFive.push_back({
 *                         this -> freq[search_term].first,
 *                         search_term
 *                     });
 *                 } else if (this -> freq[search_term].first > temp -> bestFive[0].first or search_term > temp -> bestFive[0].second) {
 *                     temp -> bestFive[0] = {
 *                         this -> freq[search_term].first,
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
 *             vector < pair < double, string >> v = temp -> bestFive;
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
 *     void dayPasses(int decayFactor) {
 *         this -> decay *= decayFactor;
 *     }
 * };*/


/**
 * import heapq
 * from collections import defaultdict
 *
 *
 * class TrieNode(defaultdict):
 *     def __init__(self):
 *         super().__init__(TrieNode)
 *         self.is_word = False
 *         self.frequency = 0
 *
 *
 * class Trie:
 *     def __init__(self):
 *         self.root = TrieNode()
 *
 *     def insert(self, word, frequency):
 *         node = self.root
 *         for char in word:
 *             node = node[char]
 *         node.is_word = True
 *         node.frequency += frequency
 *
 *     def search(self, prefix, limit):
 *         node = self.root
 *         for char in prefix:
 *             if char not in node:
 *                 return []
 *             node = node[char]
 *
 *         # Min Heap to limit the words to limit size.
 *         heap = []
 *         self._dfs(node, prefix, limit, heap)
 *
 *         return heap
 *
 *     def _dfs(self, node, prefix, limit, heap):
 *         if node.is_word:
 *             heapq.heappush(heap, (node.frequency, prefix))
 *             if len(heap) > limit:
 *                 heapq.heappop(heap)
 *         for char, child in node.items():
 *             self._dfs(child, prefix + char, limit, heap)
 *
 *     def decay(self, decay_factor):
 *         self._decay(self.root, decay_factor)
 *
 *     def _decay(self, node, decay_factor):
 *         if node.is_word:
 *             # NOTE: Decay is a float division.
 *             node.frequency /= decay_factor
 *         for child in node.values():
 *             self._decay(child, decay_factor)
 *
 *
 * class TypeHead:
 *     def __init__(self):
 *         self.trie = Trie()
 *         self.decay_factor = 1.0 # Make sure you take the decay as a float.
 *
 *     def incrementSearchTermFrequency(self, word, increment):
 *         self.trie.insert(word, increment)
 *
 *     def findTopXSuggestion(self, prefix, limit):
 *         result = self.trie.search(prefix, limit)
 *         answer = [word for _, word in result]
 *
 *         # Append empty strings
 *         for _ in range(limit - len(answer)):
 *             answer.append("")
 *
 *         # Lexicographically sorted answer.
 *         return sorted(answer)
 *
 *     def dayPasses(self, decay_factor):
 *         self.decay_factor = decay_factor * 1.0
 *         self.trie.decay(decay_factor)*/