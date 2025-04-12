package com.oranbyte.watcher.ui;

import java.awt.Color;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class BaseUI {

	public void enableNimbusUI() {
		try {
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}

			UIManager.put("control", new Color(240, 240, 240));
			UIManager.put("info", new Color(255, 255, 255));
			UIManager.put("nimbusBase", new Color(230, 230, 230));
			UIManager.put("nimbusBlueGrey", new Color(200, 200, 200));
			UIManager.put("nimbusLightBackground", new Color(247, 247, 247));
			UIManager.put("text", Color.BLACK);

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

	}

}
