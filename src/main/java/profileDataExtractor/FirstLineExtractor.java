package profileDataExtractor;

public class FirstLineExtractor implements DataExtractor {
    @Override
    public String getDataType() {
        return "Resume Head";
    }

    @Override
    public String getData(String documentText) {
        return documentText.split("\n")[0].trim();
    }
}