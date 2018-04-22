package com.cs.util.xsutil.common.util;

import java.util.UUID;

public class UUIDGenerator {

	/**
	 * 产生一个32位的UUID
	 * 
	 * @return
	 */
	public static String generate() {
		return UUID.randomUUID().toString().replaceAll("-", "").toLowerCase();
	}

}
