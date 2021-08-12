
package lt.realmdev.nppudlthemeswitcher;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

// spell-checker: ignore appdata

public class NppUDLThemeSwitcher {
	
	private final JFrame mainFrame;
	private final JLabel udlChooserLabel, themeChooserLabel;
	private final JComboBox<UDLItem> udlChooser;
	private final JComboBox<ThemeItem> themeChooser;
	
	public NppUDLThemeSwitcher() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {}
		
		mainFrame = new JFrame("Notepad++ UDL Theme Switcher");
		mainFrame.setIconImages(Arrays.asList(new Image[] {
			mainFrame.getToolkit().createImage(this.getClass().getResource("/icon_16x16.png")),
			mainFrame.getToolkit().createImage(this.getClass().getResource("/icon_32x32.png")),
			mainFrame.getToolkit().createImage(this.getClass().getResource("/icon_48x48.png")),
			mainFrame.getToolkit().createImage(this.getClass().getResource("/icon_64x64.png")),
			mainFrame.getToolkit().createImage(this.getClass().getResource("/icon_128x128.png"))
		}));
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.getContentPane().setLayout(new FlowLayout(FlowLayout.LEFT));
		
		File configPath = getConfigPath();
		NPPConfig config = new NPPConfig(new File(configPath, "config.xml"));
		
		udlChooserLabel = new JLabel("User Defined Language");
		udlChooser = new JComboBox<>(new UDLSwitcherModel(new File[] {
			new File(configPath, "userDefineLang.xml"),
			new File(configPath, "userDefineLangs")
		}));
		
		themeChooserLabel = new JLabel("Theme");
		themeChooser = new JComboBox<>(new ThemeSwitcherModel(new File(configPath, "themes"), config.getStylerTheme()));
		
		JButton switchButton = new JButton("Switch");
		switchButton.addActionListener(this::switchTheme);
		
		mainFrame.getContentPane().add(udlChooserLabel);
		mainFrame.getContentPane().add(udlChooser);
		mainFrame.getContentPane().add(themeChooserLabel);
		mainFrame.getContentPane().add(themeChooser);
		mainFrame.getContentPane().add(switchButton);
		mainFrame.pack();
	}
	
	private File getConfigPath() {
		String appdata = System.getenv("APPDATA");
		return (appdata != null) ? new File(appdata, "Notepad++") : new File(".");
	}
	
	private void switchTheme(ActionEvent e) {
		UDLItem udl = (UDLItem) udlChooser.getSelectedItem();
		ThemeItem theme = (ThemeItem) themeChooser.getSelectedItem();
		if (udl != null && theme != null) {
			udl.applyTheme(theme);
		}
		JOptionPane.showMessageDialog(mainFrame, "Complete", "Switcher", JOptionPane.INFORMATION_MESSAGE);
	}
	
	private void start() {
		Dimension ss = mainFrame.getToolkit().getScreenSize();
		Insets si = mainFrame.getToolkit().getScreenInsets(mainFrame.getGraphicsConfiguration());
		ss.width -= si.left + si.right;
		ss.height -= si.top + si.bottom;
		Dimension fs = mainFrame.getSize();
		
		mainFrame.setLocation(si.left + Math.max((ss.width - fs.width) / 2, 0), si.top + Math.max((ss.height - fs.height) / 2, 0));
		mainFrame.setVisible(true);
	}
	
	public static void main(String[] args) {
		NppUDLThemeSwitcher app = new NppUDLThemeSwitcher();
		app.start();
	}
	
}
