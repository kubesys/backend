package com.github.kubesys;

import java.text.SimpleDateFormat;
import java.util.Base64;

public class APIMarkdownTest {
	public static void main(String[] args) {
//		APIMarkdown api = new APIMarkdown(ApplicationServer.class);
//		System.out.println(api.getContent());
		System.out.println(Base64.getEncoder().encodeToString("admin".getBytes()));
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateStr = dateformat.format(System.currentTimeMillis());
		System.out.println(dateStr);
	}
}
