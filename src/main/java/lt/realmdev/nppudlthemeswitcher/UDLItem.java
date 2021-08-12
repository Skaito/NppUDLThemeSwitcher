
package lt.realmdev.nppudlthemeswitcher;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class UDLItem {
	
	private final File path;
	private final String name;

	public UDLItem(File path, String name) {
		this.path = path;
		this.name = name;
	}

	public void applyTheme(ThemeItem theme) {
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
					if (nodes.item(i).getNodeName().equals("UserLang")) {
						String uName = nodes.item(i).getAttributes().getNamedItem("name").getNodeValue();
						if (!uName.equals(name)) continue;
						theme.loadData();
						ThemeStyle defaultStyle = theme.styles.containsKey(ThemeItem.STYLE_DEFAULT) ? theme.styles.get(ThemeItem.STYLE_DEFAULT) : null;
						if (defaultStyle == null) Logger.getLogger(UDLItem.class.getName()).log(Level.WARNING, "Default theme style not found.");
						for (int n = 0, cn = nodes.item(i).getChildNodes().getLength(); n < cn; n++) {
							Node nBlock = nodes.item(i).getChildNodes().item(n);
							if (nBlock.getNodeName().equals("KeywordLists")) {
								for (int t = 0, ct = nBlock.getChildNodes().getLength(); t < ct; t++) {
									Node nKeywords = nBlock.getChildNodes().item(t);
									if (nKeywords.getNodeName().equals("Keywords")) {
										String sName = nKeywords.getAttributes().getNamedItem("name").getNodeValue();
										if (sName.matches("^Keywords[0-9]+$")) {
											Node cNode = nKeywords.getFirstChild();
											if (cNode != null && cNode.getNodeType() == Node.TEXT_NODE) {
												cNode.setNodeValue(cNode.getNodeValue().replaceAll("[\r\n ]+", " "));
											}
										}
									}
								}
							} else if (nBlock.getNodeName().equals("Styles")) {
								for (int t = 0, ct = nBlock.getChildNodes().getLength(); t < ct; t++) {
									Node nStyle = nBlock.getChildNodes().item(t);
									if (nStyle.getNodeName().equals("WordsStyle")) {
										String sName = nStyle.getAttributes().getNamedItem("name").getNodeValue();

										if (sName.equals("DEFAULT")) {
											applyStyleToNode(nStyle, defaultStyle);
										} else if (sName.equals("COMMENTS")) {
											lookupAndApplyStyleToNode(nStyle, theme.styles, ThemeItem.STYLE_COMMENT, defaultStyle);
										} else if (sName.equals("LINE COMMENTS")) {
											lookupAndApplyStyleToNode(nStyle, theme.styles, ThemeItem.STYLE_COMMENT_LINE, defaultStyle);
										} else if (sName.equals("NUMBERS")) {
											lookupAndApplyStyleToNode(nStyle, theme.styles, ThemeItem.STYLE_NUMBER, defaultStyle);
										} else if (sName.matches("^KEYWORDS[0-9]+$")) {
											lookupAndApplyStyleToNode(nStyle, theme.styles, ThemeItem.STYLE_INSTRUCTION_WORD, defaultStyle);
										} else if (sName.equals("OPERATORS")) {
											lookupAndApplyStyleToNode(nStyle, theme.styles, ThemeItem.STYLE_OPERATOR, defaultStyle);
										} else if (sName.matches("^FOLDER IN CODE[0-9]+$")) {
											applyStyleToNode(nStyle, defaultStyle);
										} else if (sName.equals("FOLDER IN COMMENT")) {
											applyStyleToNode(nStyle, defaultStyle);
										} else if (sName.matches("^DELIMITERS[0-9]+$")) {
											lookupAndApplyStyleToNode(nStyle, theme.styles, ThemeItem.STYLE_STRING, defaultStyle);
										} else {
											applyStyleToNode(nStyle, defaultStyle);
										}
									}
								}
							}
						}
					}
				}
				
				if (!path.isFile()) path.createNewFile();
				try (FileOutputStream fos = new FileOutputStream(path)) {
					TransformerFactory tFactory = TransformerFactory.newInstance();
					Transformer transformer = tFactory.newTransformer();

					DOMSource source = new DOMSource(doc);
					StreamResult result = new StreamResult(fos);
					transformer.transform(source, result);
				} catch (TransformerException ex) {
					Logger.getLogger(UDLItem.class.getName()).log(Level.SEVERE, null, ex);
				}
				
			} catch (ParserConfigurationException | SAXException | IOException ex) {
				Logger.getLogger(UDLItem.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
	}

	private void lookupAndApplyStyleToNode(Node styleNode, Map<String, ThemeStyle> styles, String name, ThemeStyle defaultStyle) {
		ThemeStyle style = styles.containsKey(name) ? styles.get(name) : null;
		if (style == null) {
			style = defaultStyle;
			Logger.getLogger(UDLItem.class.getName()).log(Level.WARNING, "Theme style ''{0}'' not found, using default style.", name);
		}
		applyStyleToNode(styleNode, style);
	}
	
	private void applyStyleToNode(Node styleNode, ThemeStyle style) {
		if (style == null) return;
		styleNode.getAttributes().getNamedItem("fgColor").setNodeValue(style.getFgColor());
		styleNode.getAttributes().getNamedItem("bgColor").setNodeValue(style.getBgColor());
		styleNode.getAttributes().getNamedItem("fontName").setNodeValue(style.getFontName());
		styleNode.getAttributes().getNamedItem("fontStyle").setNodeValue(String.valueOf(style.getFontStyle()));
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
