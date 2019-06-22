
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.*;
import java.util.*;
import java.util.Timer;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;

public class Test extends GUISettings implements ActionListener {
	
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws FileNotFoundException {
		File newFile = new File(".");
		int count = 0;
		for (File file : newFile.listFiles()) {
			String fileName = file.getName();
			if (fileName.contains("src")) {
				for (File f : file.listFiles()) {
					String name = f.getName();
					if (name.endsWith(".java")) {
						Scanner scanner = new Scanner(f);
						while (scanner.hasNextLine()) {
							String line = scanner.nextLine();
							if (!line.isEmpty()) {
								count++;
							}
						}
						scanner.close();
					}
				}
			}
		}
		System.out.println(count);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
	}
}
