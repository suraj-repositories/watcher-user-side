package com.oranbyte.watcher.services.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.oranbyte.watcher.constants.AppConstant;
import com.oranbyte.watcher.pojo.Todo;
import com.oranbyte.watcher.services.JsonService;
import com.oranbyte.watcher.services.TodoService;

public class TodoServiceImpl implements TodoService {

	private JsonService jsonService;
	private JFrame jframe;

	public TodoServiceImpl() {
		this.jsonService = new JsonServiceImpl();
	}

	@SuppressWarnings("unchecked")
	@Override
	public Todo save(Todo todo) {
		String apiUrl = AppConstant.API_URL + "todos/save.php";

		String jsonData = String.format("""
				{
				    "id": "%s",
				    "title": "%s",
				    "description": "%s",
				    "status": "%s"
				}
				""", Objects.isNull(todo.getId()) ? "" : todo.getId(), todo.getTitle(), todo.getDescription(),
				todo.getStatus());

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
				.header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(jsonData)).build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

			Map<String, Object> parsedMap = jsonService.parse(response.body());

			System.out.println(response.body());
			if (parsedMap.containsKey("status")) {
				if (parsedMap.get("status").equals("error")) {

					JOptionPane.showMessageDialog(jframe, parsedMap.get("message"), "Error", JOptionPane.ERROR_MESSAGE);
					return null;
				}
			}

			Object object = parsedMap.get("todo");
			Map<String, Object> map = (Map<String, Object>) object;
			return this.mapToPojo(map);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private Todo mapToPojo(Map<String, Object> map) {
		try {

			Integer id = map.get("id") != null ? Integer.parseInt(map.get("id").toString()) : null;
			String title = map.get("title") != null ? map.get("title").toString() : null;
			String description = map.get("description") != null ? map.get("description").toString() : null;
			String status = map.get("status") != null ? map.get("status").toString() : null;
			String createdAt = map.get("created_at") != null ? map.get("created_at").toString() : null;
			String updatedAt = map.get("updated_at") != null ? map.get("updated_at").toString() : null;

			return new Todo(id, title, description, status, createdAt, updatedAt);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Todo getLastTodo() {
		String apiUrl = AppConstant.API_URL + "todos/get_last.php";

		HttpClient client = HttpClient.newHttpClient();

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl))
				.header("Content-Type", "application/json").GET().build();

		try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			Map<String, Object> map = jsonService.parse(response.body());

			if (Objects.nonNull(map)) {
				Map<String, Object> object = (Map<String, Object>) map.get("todo");
				return this.mapToPojo(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setJFrame(JFrame jframe) {
		this.jframe = jframe;
	}

}
