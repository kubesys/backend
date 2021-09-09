package com.github.kubesys;

public class SplitTest {

	public static void main(String[] args) {
		String s = "a.b.c";
		for (String ss : s.split("\\.")) {
			System.out.println(ss);
		}
		System.out.println(System.getProperty("user.dir"));
	}

}
