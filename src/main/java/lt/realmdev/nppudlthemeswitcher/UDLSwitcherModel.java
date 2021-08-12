
package lt.realmdev.nppudlthemeswitcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UDLSwitcherModel implements ComboBoxModel<UDLItem> {
	
	private final ArrayList<UDLItem> items;
	private UDLItem item;
	private final ArrayList<ListDataListener> listeners;

	public UDLSwitcherModel(File[] udlSources) {
		items = new ArrayList<>();
		item = null;
		listeners = new ArrayList<>();
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		for (File udlSource : udlSources) {
			File[] udlFiles;
			if (udlSource.isFile()) {
				udlFiles = new File[] {udlSource};
			} else {
				File[] list = udlSource.listFiles((parent, name) -> (new File(parent, name)).isFile());
				udlFiles = (list != null) ? list : new File[0];
			}
			for (File udlFile : udlFiles) {
				try {
					Document doc = dbf.newDocumentBuilder().parse(udlFile);
					
					NodeList nodes = null;
					for (int i = 0, c = doc.getChildNodes().getLength(); i < c; i++) {
						if (doc.getChildNodes().item(i).getNodeName().equals("NotepadPlus")) {
							nodes = doc.getChildNodes().item(i).getChildNodes();
							break;
						}
					}
					if (nodes == null) return;
					for (int i = 0, c = nodes.getLength(); i < c; i++) {
						if (nodes.item(i).getNodeName().equals("UserLang")) {
							String name = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
							items.add(new UDLItem(udlFile, name));
						}
						if (items.size() > 0) item = items.get(0);
					}
				} catch (ParserConfigurationException | SAXException | IOException ex) {
					Logger.getLogger(UDLSwitcherModel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
		
	}
	
	@Override
	public void setSelectedItem(Object anItem) {
		if (anItem == null) {
			item = null;
		}
		if (anItem instanceof UDLItem && items.contains((UDLItem) anItem)) {
			item = (UDLItem) anItem;
		}
	}

	@Override
	public UDLItem getSelectedItem() {
		return item;
	}

	@Override
	public int getSize() {
		return items.size();
	}

	@Override
	public UDLItem getElementAt(int index) {
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
