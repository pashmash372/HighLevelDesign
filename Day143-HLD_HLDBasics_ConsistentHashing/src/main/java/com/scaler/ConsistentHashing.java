package com.scaler;

import java.util.*;

public class ConsistentHashing {

}

 class Solution {
    public class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    private ArrayList<Integer> answers;
    /* each pair corresponds to : ( location, serverID ) */
    private Map<Integer, String> locationToServerMapping;
    /* each pair corresponds to : ( serverID, <usernames> ) */
    private Map<String, List<Pair<String, Integer>>> serverToKeyMappings;

    private int userHash(String username, int hashKey) {
        final int p = hashKey;
        final int n = 360;
        long hashCode = 0;
        long p_pow = 1;
        for (int i = 0; i < username.length(); i++) {
            char character = username.charAt(i);
            hashCode = (hashCode + (character - 'A' + 1) * p_pow) % n;
            p_pow = (p_pow * p) % n;
        }
        return (int)(hashCode);
    }

    private int assignRequest(String keyname, int hashKey) {
        if (locationToServerMapping.size() == 0) {
            return -1;
        }
        int keyLocation = userHash(keyname, hashKey);
        Map.Entry<Integer, String> it = locationToServerMapping.entrySet().stream()
                .filter(e -> e.getKey() >= keyLocation)
                .findFirst()
                .orElse(locationToServerMapping.entrySet().stream().findFirst().get());
        /* when no server to right on the ring, assign the first server from start of the ring */
        String serverID = it.getValue();
        serverToKeyMappings.computeIfAbsent(serverID, k -> new ArrayList<>()).add(new Pair<>(keyname, hashKey));
        return keyLocation;
    }

    private void findRequestsToServe(int serverLocation, int hashKey) {
        if (serverToKeyMappings.size() == 0) {
            /* No requests to serve */
            return;
        }
        /* we need to find the requests which can now possibly be redirected to the added server */
        /* doing +1 because otherwise we'll get the location of the newly added server */
        Map.Entry<Integer, String> it = locationToServerMapping.entrySet().stream()
                .filter(e -> e.getKey() > serverLocation)
                .findFirst()
                .orElse(locationToServerMapping.entrySet().stream().findFirst().get());
        String serverID = it.getValue();
        /* these are the usernames which can possibly be redirected */
        List<Pair<String, Integer>> keynames = serverToKeyMappings.getOrDefault(serverID, new ArrayList<>());
        serverToKeyMappings.remove(serverID);
        for (Pair<String, Integer> entry : keynames) {
            /* may or may not get reassigned */
            assignRequest(entry.getKey(), entry.getValue());
        }
    }

    private int addServer(String serverID, int hashKey) {
        int firstLocation = userHash(serverID, hashKey);
        locationToServerMapping.put(firstLocation, serverID);
        findRequestsToServe(firstLocation, hashKey);
        return serverToKeyMappings.getOrDefault(serverID, new ArrayList<>()).size();
    }

    private int removeServer(String serverID, int hashKey) {
        for (Iterator<Map.Entry<Integer, String>> it = locationToServerMapping.entrySet().iterator(); it.hasNext(); ) {
            Map.Entry<Integer, String> entry = it.next();
            if (entry.getValue().equals(serverID)) {
                it.remove();
                break;
            }
        }
        List<Pair<String, Integer>> keynamesToReassign = serverToKeyMappings.getOrDefault(serverID, Collections.emptyList());
        serverToKeyMappings.remove(serverID);
        for (Pair<String, Integer> keyname : keynamesToReassign) {
            assignRequest(keyname.getKey(), keyname.getValue());
        }
        return keynamesToReassign.size();
    }

    void performOperation(String A, String B, int C) {
        String operation = A;
        if (operation.equals("ADD")) {
            String serverID = B;
            answers.add(addServer(serverID, C));
        } else if (operation.equals("REMOVE")) {
            String serverID = B;
            answers.add(removeServer(serverID, C));
        } else if (operation.equals("ASSIGN")) {
            String keyname = B;
            answers.add(assignRequest(keyname, C));
        }
    }

    public ArrayList<Integer> solve(ArrayList<String> A, ArrayList<String> B, ArrayList<Integer> C) {
        answers = new ArrayList<Integer>();
        locationToServerMapping = new TreeMap<Integer, String>();
        serverToKeyMappings = new HashMap<String, List<Pair<String, Integer>>>();
        for (int i = 0; i < A.size(); i++) {
            performOperation(A.get(i), B.get(i), C.get(i));
        }
        return answers;
    }
}


/**
 * Q1. Consistent Hashing
Solved
feature icon
Using hints is now penalty free
Use Hint
Problem Description
We have a hash ring where we have locations available from 0 degrees to 359 degrees. Implement a consistent hashing in the following way : You can add a server to the hash ring. We just need to pass the serverID and the hash functions automatically assign them one location on the hash ring. The serverID can be strings.

To add any server you give input as ADD servername . This will add that server to your hash ring. You also have the option to remove an added server which can be done by giving the input as REMOVE servername . This will remove the server from the hash ring. Lastly, to the servers added on the ring, you can assign incoming requests based on keys.

So, when you do ASSIGN keyname, you basically want to assign this request to one of the servers nearest to the name hash location in clockwise direction ( If no server found in clockwise direction, pick the nearest server from 0 degrees in clockwise direction or if there is more than one nearest server available then assign this request to the latest server added to that location). It has been guaranteed that all the key names and server names would be unique and at least one server exists for ASSIGN type requests.

You are given two string vectors A and B and an integer array C. For all valid i, A[i] tells you the type of operation of i-th query and B[i] tells you the key/server name depending on the type of query and C[i] tells you the hashKey for the i-th operation. A[i] can only take 3 values - "ADD", "REMOVE", "ASSIGN".

For "ADD" queries, B[i] is an uppercase string with 5 or more letters. They are all unique among add queries.
For "REMOVE" queries, B[i] is an uppercase string with 5 or more letters. They are all unique among remove queries.
For "ASSIGN" queries, B[i] is an uppercase string with exactly 4 letters. They are all unique among all queries.

You need to return an integer array. Let's call it ans. The value ans[i] should correspond to the output for the i-th query. The rules for the output are:
For "ADD" queries, ans[i] should be equal to the number of keys that got reassigned to the server added in the i-th query.
For "REMOVE" queries, ans[i] should be equal to the number of keys that assigned to the server getting removed (before removal).
For "ASSIGN" queries, ans[i] should be equal to the hash location of this request.

It is guaranteed that there will not be any removals of servers that are not already there. It is also guaranteed that number of queries of types "ADD" / "REMOVE" do not exceed 11 each.

Note:
While removing a server, all the keys assigned to that particular server will get reassigned according to the "ASSIGN" functionality.
If there are more than one server at a specific location, then consider the latest added server to that location to serve the upcoming requests that needs to be assigned to a server at that location.
You are required to use the following hash function to assign degrees to servers/keys:

int userHash(string username, int hashKey){
    const int p = hashKey;
    const int n = 360;
    int hashCode = 0;
    long long p_pow = 1;
    for (int i = 0; i &amp;amp;lt; username.length(); i++) {
        char character = username[i];
        hashCode = (hashCode + (character - 'A' + 1) * p_pow) % n;
        p_pow = (p_pow * p) % n;
    }
    return hashCode;
}


Problem Constraints
1 <= A.size() <= 104 + 30

1 <= B.size() <= 104 + 30

1 <= C.size() <= 104 + 30

A.size() = B.size() = C.size()



Input Format
First argument is the string array A. Second argument is string array B. Third argument is integer array C.


Output Format
Return an integer array with the corresponding values for each query according to the problem statement.


Example Input
Input 1:
A = [ADD, ASSIGN, ADD, ASSIGN, REMOVE, ASSIGN]
B = [INDIA, NWFJ, RUSSIA, OYVL, INDIA, IGAX]
C = [7, 3, 5, 13, 23, 17 ]
Input 2:

A = [ADD, ASSIGN, ASSIGN, ADD, ASSIGN, ASSIGN, REMOVE, ASSIGN]
B = [INDIA, IRYA, RGJK, RUSSIA, BGVH, SUKJ, INDIA, RBRF]
C = [11, 31, 7, 3, 5, 13, 23, 17]


Example Output
Output 1:
0 47 1 182 0 249
Output 2:

0 23 10 0 147 1 4 172


Example Explanation
Explanation 1:
"INDIA" gets mapped to hash value 31
"RUSSIA" gets mapped to hash value 203
For the 1st query, there are no keys at all so 0 keys get reassigned.
For the 2nd query, string gets hash value of 47 and gets assigned to "INDIA"
For the 3rd query, "RUSSIA" gets added and 1 key get reassigned to "RUSSIA"
For the 4th query, string gets hash value of 182 and gets assigned to "RUSSIA"
For the 5th query, "INDIA" is removed and 0 keys need to be reassigned.
For the 6th query, string gets hash value of 249 and gets assigned to "RUSSIA"
Explanation 1:

"INDIA" gets mapped to hash value 267
"RUSSIA" gets mapped to hash value 297
For the 1st query, there are no keys at all so 0 keys get reassigned.
For the 2nd query, string gets hash value of 23 and gets assigned to "INDIA"
For the 3rd query, string gets hash value of 10 and gets assigned to "INDIA"
For the 4th query, "RUSSIA" gets added and 0 key get reassigned to "RUSSIA"
For the 5th query, string gets hash value of 147 and gets assigned to "INDIA"
For the 6th query, string gets hash value of 1 and gets assigned to "INDIA"
For the 7th query, "INDIA" is removed and 4 keys need to be reassigned.
For the 8th query, string gets hash value of 172 and gets assigned to "RUSSIA"
*/


/**
* https://www.scaler.com/academy/mentee-dashboard/class/56999/assignment/problems/12359?navref=cl_tt_lst_nm
* */


/**HINT 1
 * You can make a separate function for adding servers, removing servers, and assigning keys.*/


/** Solution Approach
 * You can make a separate function for adding servers, removing servers, and assigning keys.
 *
 * Also, you can keep a map/hashmap to store locations of each server. Further, for each server,
 * you can have a list of keys currently in there.
 *
 * Since the queries of "ADD"/"REMOVE" are very less, we can comfortable use
 * map/hashmap to keep the data.*/


/***/