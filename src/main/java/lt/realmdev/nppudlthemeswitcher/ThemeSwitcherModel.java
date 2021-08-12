
package lt.realmdev.nppudlthemeswitcher;

import java.io.File;
import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

public class ThemeSwitcherModel implements ComboBoxModel<ThemeItem> {

	private final ArrayList<ThemeItem> items;
	private ThemeItem item;
	private final ArrayList<ListDataListener> listeners;

	public ThemeSwitcherModel(File themesPath, File stylerTheme) {
		items = new ArrayList<>();
		item = null;
		listeners = new ArrayList<>();
		if (themesPath != null && themesPath.isDirectory()) {
			ThemeItem cItem;
			for (File file : themesPath.listFiles((File dir, String name) -> name.endsWith(".xml"))) {
				String[] name = file.getName().split("\\.");
				items.add(cItem = new ThemeItem(file, name[0]));
				if (stylerTheme != null && cItem.getPath().equals(stylerTheme)) item = cItem;
			}
			if (items.size() > 0 && item == null) item = items.get(0);
		}
	}
	
	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem == null) {
			item = null;
		}
		if (anItem instanceof ThemeItem && items.contains((ThemeItem) anItem)) {
			item = (ThemeItem) anItem;
		}
	}

	@Override
	public ThemeItem getSelectedItem() {
		return item;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public ThemeItem getElementAt(int index) {
		return items.get(index);
	}

	@Override
	public void addListDataListener(ListDataListener l) {
		listeners.add(l);
	}

	@Override
	public void removeListDataListener(ListDataListener l) {
		listeners.remove(l);
	}
	
}
