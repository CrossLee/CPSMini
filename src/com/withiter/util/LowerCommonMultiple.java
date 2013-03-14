/*
 * Created on 2013-2-9
 *
 * LowerCommonMultiple.java
 *
 * Copyright (C) 2013 by Withiter Software & Technology Services (Shanghai) Limited.
 * All rights reserved. Withiter Software & Technology Services (Shanghai) Limited 
 * claims copyright in this computer program as an unpublished work. Claim of copyright 
 * does not imply waiver of other rights.
 *
 * NOTICE OF PROPRIETARY RIGHTS
 *
 * This program is a confidential trade secret and the property of 
 * Withiter Software & Technology Services (Shanghai) Limited. Use, examination, 
 * reproduction, disassembly, decompiling, transfer and/or disclosure to others of 
 * all or any part of this software program are strictly prohibited except by express 
 * written agreement with Withiter Software & Technology Services (Shanghai) Limited.
 */
/*
 * ---------------------------------------------------------------------------------
 * Modification History
 * Date               Author                     Version     Description
 * 2013-2-9       cross                    1.0         New
 * ---------------------------------------------------------------------------------
 */
/**
 * 
 */
package com.withiter.util;

/**
 * @author cross
 * 
 */
public class LowerCommonMultiple {

	/**
	 * @param args
	 */

	private int A, B;

	public int get(int a, int b) {
		for (int i = Math.max(a, b);; i++) {
			A = a;
			B = b;
			if (accept(i)) {
				return i;
			}
		}
	}

	private boolean accept(int n) {
		return n % A == 0 && n % B == 0;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LowerCommonMultiple lcm = new LowerCommonMultiple();
		int test = lcm.get(4, 6);
		System.out.println(test);
	}

}
