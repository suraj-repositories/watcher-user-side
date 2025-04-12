package com.oranbyte.watcher.services;

import java.util.Map;

public interface JsonService {

	Map<String, Object> parse(String jsonString);
}
