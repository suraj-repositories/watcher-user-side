package com.oranbyte.watcher;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.oranbyte.watcher.constants.AppConstant;
import com.oranbyte.watcher.ui.BaseUI;
import com.oranbyte.watcher.ui.MainFrame;

public class Watcher {

	private static final LocalTime START_TIME = LocalTime.of(AppConstant.START_HOUR, AppConstant.START_MINUTE);
	private static final LocalTime END_TIME = LocalTime.of(AppConstant.END_HOUR, AppConstant.END_MINUTE);

	private static final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	private static final AtomicBoolean isScheduled = new AtomicBoolean(false);

	public static void main(String[] args) {
		scheduleInitialLaunch();
	}

	private static void scheduleInitialLaunch() {
		Runnable task = () -> {
			LocalTime now = LocalTime.now();
			if (now.isAfter(START_TIME) && now.isBefore(END_TIME)) {
				SwingUtilities.invokeLater(Watcher::showMainFrame);
			} else if (now.isBefore(START_TIME)) {
				long delay = java.time.Duration.between(now, START_TIME).toMillis();
				scheduler.schedule(() -> SwingUtilities.invokeLater(Watcher::showMainFrame), delay,
						TimeUnit.MILLISECONDS);
			} else {
				scheduleShutdown();
			}
		};

		scheduler.execute(task);
	}

	private static void showMainFrame() {
		new BaseUI().enableNimbusUI();
		MainFrame mainFrame = new MainFrame();

		mainFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if (!isScheduled.getAndSet(true)) {
					if (LocalTime.now().isBefore(END_TIME)) {
						scheduler.schedule(() -> {
							isScheduled.set(false);
							SwingUtilities.invokeLater(Watcher::showMainFrame);
						}, AppConstant.APP_LOOP_VALUE, AppConstant.APP_LOOP_UNIT);
					} else {
						scheduleShutdown();
					}
				}
			}
		});

		mainFrame.setVisible(true);
	}

	private static void scheduleShutdown() {
		scheduler.shutdown();
		System.exit(0);
	}
}
