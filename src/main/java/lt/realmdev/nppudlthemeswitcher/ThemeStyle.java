
package lt.realmdev.nppudlthemeswitcher;

public class ThemeStyle {
	
	private int id;
	private String name;
	private String fgColor;
	private String bgColor;
	private String fontName;
	private int fontStyle;
	private float fontSize;

	public ThemeStyle() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFgColor() {
		return fgColor;
	}

	public void setFgColor(String fgColor) {
		this.fgColor = fgColor;
	}

	public String getBgColor() {
		return bgColor;
	}

	public void setBgColor(String bgColor) {
		this.bgColor = bgColor;
	}

	public String getFontName() {
		return fontName;
	}

	public void setFontName(String fontName) {
		this.fontName = fontName;
	}

	public int getFontStyle() {
		return fontStyle;
	}

	public void setFontStyle(int fontStyle) {
		this.fontStyle = fontStyle;
	}

	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	@Override
	public String toString() {
		return "ThemeStyle{" + "id=" + id + ", name=" + name + ", fgColor=" + fgColor + ", bgColor=" + bgColor + ", fontName=" + fontName + ", fontStyle=" + fontStyle + ", fontSize=" + fontSize + '}';
	}
	
}
