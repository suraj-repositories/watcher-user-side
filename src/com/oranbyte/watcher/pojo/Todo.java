package com.oranbyte.watcher.pojo;

public class Todo {

	private Integer id;

	private String title;

	private String description;

	private String status;

	private String createdAt = "";

	private String updatedAt = "";

	public Todo() {
	}

	public Todo(Integer id, String title, String description, String status, String createdAt, String updatedAt) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.status = status;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "Todo [id=" + id + ", title=" + title + ", description=" + description + ", status=" + status
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}

}
