
package lt.realmdev.nppudlthemeswitcher;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ThemeItem {
	
	public static final String STYLE_PREPROCESSOR = "PREPROCESSOR";
	public static final String STYLE_DEFAULT = "DEFAULT";
	public static final String STYLE_INSTRUCTION_WORD = "INSTRUCTION WORD";
	public static final String STYLE_TYPE_WORD = "TYPE WORD";
	public static final String STYLE_NUMBER = "NUMBER";
	public static final String STYLE_STRING = "STRING";
	public static final String STYLE_CHARACTER = "CHARACTER";
	public static final String STYLE_OPERATOR = "OPERATOR";
	public static final String STYLE_VERBATIM = "VERBATIM";
	public static final String STYLE_REGEX = "REGEX";
	public static final String STYLE_COMMENT = "COMMENT";
	public static final String STYLE_COMMENT_LINE = "COMMENT LINE";
	public static final String STYLE_COMMENT_DOC = "COMMENT DOC";
	public static final String STYLE_COMMENT_LINE_DOC = "COMMENT LINE DOC";
	public static final String STYLE_COMMENT_DOC_KEYWORD = "COMMENT DOC KEYWORD";
	public static final String STYLE_COMMENT_DOC_KEYWORD_ERROR = "COMMENT DOC KEYWORD ERROR";
	
	private final File path;
	private final String name;
	public final Map<String, ThemeStyle> styles;
	
	public ThemeItem(File path, String name) {
		this.path = path;
		this.name = name;
		styles = new HashMap<>();
	}
	
	public void loadData() {
		if (path != null && path.isFile()) {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db;
			try {
				db = dbf.newDocumentBuilder();
				Document doc = db.parse(path);
				
				NodeList nodes = null;
				for (int i = 0, c = doc.getChildNodes().getLength(); i < c; i++) {
					if (doc.getChildNodes().item(i).getNodeName().equals("NotepadPlus")) {
						nodes = doc.getChildNodes().item(i).getChildNodes();
						break;
					}
				}
				if (nodes == null) return;
				for (int i = 0, c = nodes.getLength(); i < c; i++) {
					Node oType = nodes.item(i);
					if (oType.getNodeName().equals("LexerStyles")) {
						for (int n = 0, cn = oType.getChildNodes().getLength(); n < cn; n++) {
							Node oItem = oType.getChildNodes().item(n);
							if (oItem.getNodeName().equals("LexerType")) {
								if (oItem.getAttributes().getNamedItem("name").getNodeValue().equals("java")) {
									for (int t = 0, ct = oItem.getChildNodes().getLength(); t < ct; t++) {
										Node oStyle = oItem.getChildNodes().item(t);
										if (oStyle.getNodeName().equals("WordsStyle")) {
											ThemeStyle style = new ThemeStyle();
											style.setName(oStyle.getAttributes().getNamedItem("name").getNodeValue());
											style.setId(parseInt(oStyle.getAttributes().getNamedItem("styleID").getNodeValue()));
											style.setFgColor(oStyle.getAttributes().getNamedItem("fgColor").getNodeValue());
											style.setBgColor(oStyle.getAttributes().getNamedItem("bgColor").getNodeValue());
											style.setFontName(oStyle.getAttributes().getNamedItem("fontName").getNodeValue());
											style.setFontStyle(parseInt(oStyle.getAttributes().getNamedItem("fontStyle").getNodeValue()));
											style.setFontSize(parseInt(oStyle.getAttributes().getNamedItem("fontSize").getNodeValue()));
											styles.put(style.getName(), style);
										}
									}
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
	
	private int parseInt(String val) {
		if (val != null && val.length() > 0) {
			try { return Integer.parseInt(val); } catch (NumberFormatException ex) {}
		}
		return 0;
	}
	
	public File getPath() {
		return path;
	}

	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return name;
	}
	
}
