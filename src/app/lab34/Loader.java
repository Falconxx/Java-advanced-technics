package app.lab34;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.List;
import javax.swing.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class Loader {
	String path;
	List<JButton> list = new ArrayList<JButton>();
	FilenameFilter ff = new FilenameFilter() {
		@Override
		public boolean accept(File dir, String name) {
			return dir.getName().endsWith(name);

		}
	};

	void read(String path) throws IOException {
		File directory = new File(path);
		if (!directory.isDirectory()) {
			System.out.println(path + " is not directory");
		} else {
			printContentOfFilesInDirectory(directory);
		}
	}

	private void printContentOfFilesInDirectory(File directory) throws IOException {
		File[] files = directory.listFiles();
		list.clear();
		for (File file : files) {
			if (file.isFile()) {
				if (ff.accept(file, "jpg")) {
					WeakReference weakReference = new WeakReference(new ImageIcon(file.getPath()));
					ImageIcon icon = (ImageIcon) weakReference.get();
					WeakReference wr = new WeakReference(new JButton());
					JButton jb = (JButton) wr.get();
					java.awt.Image image = icon.getImage(); // transform it
					java.awt.Image newimg = image.getScaledInstance(120, 120, java.awt.Image.SCALE_SMOOTH);
					icon = new ImageIcon(newimg);
					jb.setIcon(icon);
					jb.setSize(100, 100);
					list.add(jb);

				}
			} else if (file.isDirectory()) {
				printContentOfFilesInDirectory(file);
			}
		}
	}

	private ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource("java.jpg");
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<JButton> getList() {
		return list;
	}

	public void setList(List<JButton> list) {
		this.list = list;
	}

}
