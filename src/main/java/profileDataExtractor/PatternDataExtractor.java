package profileDataExtractor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract class PatternDataExtractor implements DataExtractor {

    abstract String getPattern();

    @Override
    public String getData(String documentText) {
        Pattern pattern = Pattern.compile(getPattern());
        Matcher matcher = pattern.matcher(documentText);
        if (matcher.find()) {
            matcher.reset();
            String data = "";
            while (matcher.find()) {
                data += matcher.group() + " ";
            }
            return data;
        } else return "Unable to find data.";
    }
}
