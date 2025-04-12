package com.oranbyte.watcher.services;

import javax.swing.JFrame;

import com.oranbyte.watcher.pojo.Todo;

public interface TodoService {

	Todo save(Todo todo);

	Todo getLastTodo();

	void setJFrame(JFrame jframe);

}
