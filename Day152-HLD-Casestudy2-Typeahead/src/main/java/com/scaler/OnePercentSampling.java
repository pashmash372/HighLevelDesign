package com.scaler;

public class OnePercentSampling {
}


/** Q1. 1% Sampling
 Solved
 feature icon
 Using hints is now penalty free
 Use Hint
 Your job is to write a function that does 1% sampling - this is to say the function returns true approx 1% of the time. Note that you want to keep that 1% truly random and spread out. This function could exist separately on different machines. Hence it could be initiated multiple times across machines.

 Note:- An error rate of 5% is allowed. No global variables should be used.*/


/**
 * class OnePercentFilter {
 *     Boolean filter() {
 *         // Complete the function
 *         Random random = new Random();
 *         int num = random.nextInt(100);
 *         if(num % 100 == 0){
 *             return true;
 *         }
 *         return false;
 *     }
 * };*/