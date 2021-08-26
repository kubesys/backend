/**
 * Copyrigt (2019, ) Institute of Software, Chinese Academy of Sciences
 */
package com.github.kubesys.webfrk;

import java.util.Base64;

/**
 * @author wuheng@otcaix.iscas.ac.cn
 * @author chace
 * @since 2019.10.29
 *
 */

public class Base64Test  {

	public static void main(String[] args) {
		String admin = "admin";
		System.out.println(Base64.getEncoder().encodeToString(admin.getBytes()));
	}
	
}
