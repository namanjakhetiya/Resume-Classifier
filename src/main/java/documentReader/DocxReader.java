package documentReader;

import java.io.File;
import java.io.FileInputStream;

import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class DocxReader implements DocumentReader {

	@Override
	public String getDocumentText(File file) {
		String text = "";
		try {
			FileInputStream fis = new FileInputStream(file);
			XWPFDocument document = new XWPFDocument(fis);
			XWPFWordExtractor wordExtractor = new XWPFWordExtractor(document);
			text = wordExtractor.getText();
			wordExtractor.close();
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return text;
	}

	@Override
	public String getSupportedExtension() {
		return "docx";
	}
}
