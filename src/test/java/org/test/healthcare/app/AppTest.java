package org.test.healthcare.app;

import static org.testng.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.testng.annotations.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {

	/**
	 * Rigorous Test :-)
	 */
	@Test
	public void shouldAnswerWithTrue() {
		assertTrue(true);
	}

	public static void main(String[] args) {
		// nums();
		reversePattern();
	}

	public static void nums() {
		String str = "aaassssddffffff";
		HashMap<Character, Integer> map = new HashMap<Character, Integer>();
		for (char c : str.toCharArray()) {
			System.out.println("char: " + c);
			if (map.containsKey(c)) {
				int count = map.get(c) + 1;
				map.replace(c, count);
				System.out.println("count:" + count);
			} else {
				map.put(c, 1);
			}
			System.out.println("Map: " + map.get(c));
		}
		System.out.println("Map Size:" + map.size());
		StringBuilder sb = new StringBuilder();
		Iterator<Map.Entry<Character, Integer>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Character, Integer> entry = it.next();
			String s = entry.getKey().toString() + entry.getValue();
			System.out.println("Entry: " + s);
			sb.append(s);
		}
		System.out.println("Result: " + sb.toString());
		System.out.println(9 / 0.0);
	}

	public static void reversePattern() {
		String str = "5Java123pop234sele900";
		char[] ch = str.toCharArray();

		Pattern pattern = Pattern.compile("\\d", Pattern.CASE_INSENSITIVE);
		Matcher matcher = null;

		StringBuilder sb1 = null;
		StringBuilder sb2 = null;
		// HashMap<Integer, String> map = new HashMap<Integer, String>();
		ArrayList<String> list = new ArrayList<String>();
		boolean flag = false;
		for (char c : ch) {
			System.out.println("For char: " + c);
			matcher = pattern.matcher(c + "");
			// System.out.println("Pattern found?: " + matcher.find());
			if (matcher.find()) {
				// add to list if there was any char found and now we are in digit
				if (!flag && sb2 != null) {
					list.add(sb2.toString());
					sb2 = null;
				}
				if (sb1 == null) {
					sb1 = new StringBuilder();
				}
				sb1.append(c);
				flag = true;
				System.out.println("sb1: " + sb1);
			} else {
				// add to list if there was any digit found and now we are in char
				if (flag && sb1 != null) {
					list.add(sb1.toString());
					sb1 = null;
				}
				if (sb2 == null) {
					sb2 = new StringBuilder();
				}
				sb2.append(c);
				flag = false;
				System.out.println("sb2: " + sb2);
			}
		}
		// add the last remaining digit or char seq to the last
		if (sb1 != null) {
			list.add(sb1.toString());
		}
		if (sb2 != null) {
			list.add(sb2.toString());
		}
		System.out.println("List has: " + list.toString());

		StringBuilder result = new StringBuilder();
		for (String data : list) {
			System.out.println("list data: " + data);
			matcher = pattern.matcher(data);
			if (matcher.find()) { // its a digit
				result.append(data);
			} else {
				StringBuilder sb = new StringBuilder(data);
				result.append(sb.reverse().toString());
			}
		}

		System.out.println("Result: " + result.toString());
	}
}
