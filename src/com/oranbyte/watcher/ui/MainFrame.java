package com.oranbyte.watcher.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

import com.oranbyte.watcher.constants.AppConstant;
import com.oranbyte.watcher.pojo.Todo;
import com.oranbyte.watcher.services.TodoService;
import com.oranbyte.watcher.services.impl.TodoServiceImpl;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public MainFrame() {
		initFrame();

		TodoService todoService = new TodoServiceImpl();
		Todo todo = todoService.getLastTodo();

		JPanel mainPanel = new JPanel();
		mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

		JPanel titlePanel = new JPanel(new BorderLayout());
		JTextField titleField = new JTextField() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 76426424799016012L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (getText().isEmpty()) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setColor(Color.GRAY);
					g2.drawString("Enter title...", 10, getHeight() / 2 + getFont().getSize() / 2 - 2);
					g2.dispose();
				}
			}
		};

		titleField.setFont(new Font("SansSerif", Font.PLAIN, 14));
		titleField.setBorder(new EmptyBorder(5, 10, 5, 10));
		titlePanel.add(titleField);

		JPanel descriptionPanel = new JPanel(new BorderLayout());

		JTextArea descriptionArea = new JTextArea(3, 20) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 76426424799016012L;

			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (getText().isEmpty()) {
					Graphics2D g2 = (Graphics2D) g.create();
					g2.setColor(Color.GRAY);
					g2.drawString("Enter description...", 10, 20);
					g2.dispose();
				}
			}
		};

		descriptionArea.setLineWrap(true);
		descriptionArea.setWrapStyleWord(true);
		descriptionArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
		descriptionArea.setBorder(new EmptyBorder(5, 10, 5, 10));

		JScrollPane descriptionScroll = new JScrollPane(descriptionArea);
		descriptionScroll.setBorder(null);
		descriptionPanel.add(descriptionScroll, BorderLayout.CENTER);

		JPanel statusAndButtonPanel = new JPanel();
		statusAndButtonPanel.setLayout(new BoxLayout(statusAndButtonPanel, BoxLayout.X_AXIS));

		String[] statuses = { "In progress", "Completed", "Pending" };
		JComboBox<String> statusCombo = new JComboBox<>(statuses);
		statusCombo.setFont(new Font("SansSerif", Font.PLAIN, 14));
		statusCombo.setFocusable(false);
		statusCombo.setBorder(null);

		if (Objects.nonNull(todo) && todo.getStatus().equalsIgnoreCase("in_progress")) {
			titleField.setText(todo.getTitle() == null ? "" : todo.getTitle());
			descriptionArea.setText(todo.getDescription() == null ? "" : todo.getDescription());
			statusCombo.setSelectedItem("In progress");
		}

		JButton submitButton = new JButton("Submit");
		submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
		submitButton.setBackground(new Color(0x4CAF50));
		submitButton.setForeground(Color.WHITE);
		submitButton.setFocusPainted(false);
		submitButton.setBorder(new EmptyBorder(10, 20, 10, 20));

		statusAndButtonPanel.add(statusCombo);
		statusAndButtonPanel.add(Box.createHorizontalStrut(20));
		statusAndButtonPanel.add(submitButton);

		mainPanel.add(titlePanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(descriptionPanel);
		mainPanel.add(Box.createVerticalStrut(10));
		mainPanel.add(Box.createVerticalStrut(15));
		mainPanel.add(statusAndButtonPanel);

		add(mainPanel);

		submitButton.addActionListener((ActionEvent e) -> {
			String title = titleField.getText();
			String description = descriptionArea.getText();
			String status = (String) statusCombo.getSelectedItem();

			submitButton.setText("Saving...");
			todoService.setJFrame(this);
			Todo todoNew = todoService.save(new Todo(todo.getId(), title, description,
					status.toLowerCase().replaceAll("\\s+", "_"), null, null));

			if (Objects.nonNull(todoNew)) {
				System.out.println(todoNew.getStatus());
				if (Objects.nonNull(todoNew.getStatus()) && !todoNew.getStatus().equalsIgnoreCase("in_progress")) {
					titleField.setText("");
					descriptionArea.setText("");
					statusCombo.setSelectedItem("In progress");

					submitButton.setText("✅ saved");
					Timer timer = new Timer(2000, new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							submitButton.setText("Submit");
						}
					});
					timer.setRepeats(false);
					timer.start();

				} else {
					this.dispose();
				}

			} else {
				submitButton.setText("Submit");
			}

		});
	}

	private void initFrame() {
		setTitle(AppConstant.APP_NAME);

		setIconImage(new ImageIcon(getClass().getResource(AppConstant.APP_LOGO)).getImage());
		setSize(AppConstant.APP_WIDTH, AppConstant.APP_HEIGHT);
		this.setAlwaysOnTop(AppConstant.APP_ON_TOP);
		this.setResizable(AppConstant.APP_WINDOW_RESIZEABLE);

		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		int x = 0, y = 0;

		switch (AppConstant.APP_POSITION) {
		case 1:
			x = 10;
			y = 20;
			setLocation(x, y);
			break;
		case 2:
			x = screenSize.width - getWidth() - 10;
			y = 20;
			setLocation(x, y);
			break;
		case 3:
			x = screenSize.width - getWidth() - 10;
			y = screenSize.height - getHeight() - 60;
			setLocation(x, y);
			break;
		case 4:
			x = 10;
			y = screenSize.height - getHeight() - 60;
			setLocation(x, y);
			break;
		case 5:
			setLocationRelativeTo(null);
			break;
		default:
			x = screenSize.width - getWidth() - 10;
			y = screenSize.height - getHeight() - 60;
			setLocation(x, y);
			break;
		}

	}

}
