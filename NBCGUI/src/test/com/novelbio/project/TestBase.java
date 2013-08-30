package com.novelbio.project;

public class TestBase {
	public static void main(String[] args) {
		String cols = "100,100,60,60,60,60,60,60,80,80";
		String align = null;
		int colsLength = cols.split(",").length;
		for (int i = 0; i < colsLength; i++) {
			if (align == null) {
				align = "c";
			} else {
				align = align + ",c";
			}
		}
		System.out.println(align);
	}
}
