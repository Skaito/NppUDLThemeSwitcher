
package lt.realmdev.nppudlthemeswitcher;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author Marius Tomas <marius@realmdev.lt>
 */
public class NPPConfig {
	
	private File stylerTheme = null;
	
	public NPPConfig(File configFile) {
		if (configFile != null && configFile.isFile()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				Document doc = db.parse(configFile);
				
				NodeList nodes = doc.getFirstChild().getChildNodes();
				for (int i = 0, c = nodes.getLength(); i < c; i++) {
					if (nodes.item(i).getNodeName().equals("GUIConfigs")) {
						for (int n = 0, cn = nodes.item(i).getChildNodes().getLength(); n < cn; n++) {
							if (nodes.item(i).getChildNodes().item(n).getNodeName().equals("GUIConfig")) {
								Node node = nodes.item(i).getChildNodes().item(n);
								if (node.getAttributes().getNamedItem("name").getNodeValue().equals("stylerTheme")) {
									stylerTheme = new File(node.getAttributes().getNamedItem("path").getNodeValue());
								}
							}
						}
						break;
					}
				}
			} catch (ParserConfigurationException | SAXException | IOException ex) {
				Logger.getLogger(NPPConfig.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	public File getStylerTheme() {
		return stylerTheme;
	}
	
}
