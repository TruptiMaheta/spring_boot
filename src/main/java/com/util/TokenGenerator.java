package com.util;

import org.springframework.stereotype.Component;

@Component
public class TokenGenerator {
	public static String generateToken() {
		StringBuffer sb = new StringBuffer("");
		String source = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
		int index = 0;
		for (int i = 1; i <= 16; i++) {
			index = (int) (Math.random() * source.length());

			sb.append(source.charAt(index));
		}

		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(generateToken());
	}
}
