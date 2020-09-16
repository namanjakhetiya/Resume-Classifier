package profileDataExtractor;

public class ContactExtractor extends PatternDataExtractor {

    private static final String COUNTRY_CODE = "+91";

    @Override
    String getPattern() {
        return "(\\" + COUNTRY_CODE + "?(\\s?|\\-?)\\d{10})|(\\d{10})|(\\(?\\" + COUNTRY_CODE + "?\\)?(\\-|\\s)?\\d{3}\\s?\\d{3}\\s?\\d{4})";
    }

    @Override
    public String getDataType() {
        return "Contact";
    }
}
