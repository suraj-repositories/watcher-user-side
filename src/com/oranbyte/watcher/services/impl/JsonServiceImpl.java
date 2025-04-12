package com.oranbyte.watcher.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oranbyte.watcher.services.JsonService;

public class JsonServiceImpl implements JsonService {

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> parse(String jsonString) {
		Object parsedObject = parseInternal(jsonString);
		System.out.println(jsonString);
		if (parsedObject instanceof Map) {
			return (Map<String, Object>) parsedObject;
		} else {
			throw new IllegalArgumentException("Input JSON string does not represent a top-level JSON object.");
		}
	}

	private Object parseInternal(String jsonString) {
		jsonString = jsonString.trim();
		if (jsonString.startsWith("{") && jsonString.endsWith("}")) {
			return parseObject(jsonString);
		} else if (jsonString.startsWith("[") && jsonString.endsWith("]")) {
			return parseArray(jsonString);
		} else {
			return parseValue(jsonString);
		}
	}

	private Map<String, Object> parseObject(String jsonObjectString) {
		Map<String, Object> map = new HashMap<>();
		jsonObjectString = jsonObjectString.substring(1, jsonObjectString.length() - 1).trim();
		if (jsonObjectString.isEmpty()) {
			return map;
		}
		String[] keyValuePairs = splitTopLevel(jsonObjectString, ',');
		for (String pair : keyValuePairs) {
			String[] parts = splitTopLevel(pair, ':');
			if (parts.length == 2) {
				String key = parts[0].trim();
				String value = parts[1].trim();
				if (key.startsWith("\"") && key.endsWith("\"")) {
					key = key.substring(1, key.length() - 1);
				}
				map.put(key, this.parseInternal(value));
			}
		}
		return map;
	}

	private List<Object> parseArray(String jsonArrayString) {
		List<Object> list = new ArrayList<>();
		jsonArrayString = jsonArrayString.substring(1, jsonArrayString.length() - 1).trim();
		if (jsonArrayString.isEmpty()) {
			return list;
		}
		String[] elements = this.splitTopLevel(jsonArrayString, ',');
		for (String element : elements) {
			list.add(this.parseInternal(element.trim())); // Recursively parse each element
		}
		return list;
	}

	private static Object parseValue(String value) {
		value = value.trim();
		if (value.startsWith("\"") && value.endsWith("\"")) {
			return value.substring(1, value.length() - 1);
		} else if (value.equalsIgnoreCase("true")) {
			return true;
		} else if (value.equalsIgnoreCase("false")) {
			return false;
		} else if (value.equalsIgnoreCase("null")) {
			return null;
		} else {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e1) {
				try {
					return Double.parseDouble(value);
				} catch (NumberFormatException e2) {
					return value;
				}
			}
		}
	}

	private String[] splitTopLevel(String s, char delimiter) {
		List<String> parts = new ArrayList<>();
		int balance = 0;
		boolean inQuotes = false;
		StringBuilder currentPart = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == '\"') {
				int backslashCount = 0;
				int j = i - 1;
				while (j >= 0 && s.charAt(j) == '\\') {
					backslashCount++;
					j--;
				}
				if (backslashCount % 2 == 0) {
					inQuotes = !inQuotes;
				}
			}

			if (!inQuotes) {
				if (c == '{' || c == '[') {
					balance++;
				} else if (c == '}' || c == ']') {
					balance--;
				} else if (c == delimiter && balance == 0) {
					parts.add(currentPart.toString().trim());
					currentPart.setLength(0);
					continue;
				}
			}
			currentPart.append(c);
		}

		parts.add(currentPart.toString().trim());
		return parts.toArray(new String[0]);
	}

}