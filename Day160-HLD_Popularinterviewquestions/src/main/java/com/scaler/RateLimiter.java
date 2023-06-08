package com.scaler;

import java.util.ArrayList;
import java.util.HashMap;

public class RateLimiter {
}

class Solution {
    public ArrayList<Integer> solve(ArrayList<Integer> A, ArrayList<Integer> B) {
        int n = A.size();
        ArrayList<Integer> ans = new ArrayList<Integer>();
        HashMap<Integer, ArrayList<Integer>> clientWindow = new HashMap<Integer, ArrayList<Integer>>();
        for (int i = 0; i < n; i++) {
            if (!clientWindow.containsKey(A.get(i))) {
                clientWindow.put(A.get(i), new ArrayList<Integer>());
            }
            while (clientWindow.get(A.get(i)).size() > 0 && clientWindow.get(A.get(i)).get(0) <= B.get(i) - 10) {
                clientWindow.get(A.get(i)).remove(0);
            }
            if (clientWindow.get(A.get(i)).size() < 3) {
                clientWindow.get(A.get(i)).add(B.get(i));
                ans.add(1);
            } else {
                ans.add(0);
            }
        }
        return ans;
    }
}

/**
 * https://www.scaler.com/academy/mentee-dashboard/class/56988/assignment/problems/39590/hints?navref=cl_pb_nv_tb*/
/**
 * Q1. Rate Limiter
 * Solved
 * feature icon
 * Using hints is now penalty free
 * Use Hint
 * Problem Description
 * Design a rate limiter system. A rate limiter limits the number of client requests allowed to be sent over a specified period.
 * <p>
 * You are given N client requests where A[i] gives the client ID and B[i] gives the timestamp t for the i-th request. The rate limiter allows 3 requests from a client in every 10 sec interval. In other words, there can be at most 3 successful requests from each client in any time interval [t, t+10).
 * <p>
 * All requests will come in chronological order. Several requests may arrive at the same timestamp.
 * <p>
 * The answer to the i-th request should be 1 if the request is successful and 0 if the request fails.
 * <p>
 * <p>
 * <p>
 * Problem Constraints
 * 1 <= N <= 105
 * <p>
 * 1 <= A[i] <= 109
 * <p>
 * 1 <= B[i] <= 105
 * <p>
 * <p>
 * <p>
 * Input Format
 * First argument A is an array of integer denoting the client ID for each request.
 * <p>
 * Second argument B is an array of integer denoting the timestamp for each request.
 * <p>
 * <p>
 * <p>
 * Output Format
 * Return an array of integer denoting the answer to each request.
 * <p>
 * <p>
 * <p>
 * Example Input
 * Input 1:
 * A = [1, 1, 2, 1, 1, 1]
 * B = [1, 2, 2, 9, 10, 11]
 * Input 2:
 * A = [2, 2, 2, 2]
 * B = [3, 3, 8, 12]
 * <p>
 * <p>
 * Example Output
 * Output 1:
 * [1, 1, 1, 1, 0, 1]
 * Output 2:
 * [1, 1, 1, 0]
 * <p>
 * <p>
 * Example Explanation
 * For Input 1:
 * The reuqest made by client 1 at time 10 sec fails as there
 * already exist successful request made by the client at time 1,
 * 2 and 9 sec.
 * All other requests result in success.
 * For Input 2:
 * The first three requests made by client 3 are successful. The
 * fourth request fails as there already exist 3 successful request
 * made by the client in the last 10 sec.
 * <p>
 * <p>
 * <p>
 * Expected Output
 * We can try to keep track of all the recent request
 * made by each of the client.
 * Solution Approach
 * We will use sliding window technique here.
 * For every client we will maintain a window of the requests made
 * by them in last 10 sec. The window has a capacity of 3.
 * When a request comes, we first clear the requests made by the client
 * that are not made in the last 10 secs. Then we check the count of
 * requests in the window, if the count is less than 3 then the current
 * request is successful.
 * <p>
 * Time Complexity : O(N)
 * Space Complexity : O(N)
 */

/**
 * We can try to keep track of all the recent request
 * made by each of the client.*/


/**
 * Solution Approach
 * We will use sliding window technique here.
 * For every client we will maintain a window of the requests made
 * by them in last 10 sec. The window has a capacity of 3.
 * When a request comes, we first clear the requests made by the client
 * that are not made in the last 10 secs. Then we check the count of
 * requests in the window, if the count is less than 3 then the current
 * request is successful.
 *
 * Time Complexity : O(N)
 * Space Complexity : O(N)*/