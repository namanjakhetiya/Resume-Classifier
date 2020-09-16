package ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Function;

import app.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import rules.AllRules;
import rules.Rule;

@SuppressWarnings("restriction")
public class Controller implements SkillSet {

	private final DirectoryChooser chooser = new DirectoryChooser();
	private Optional<File> file = Optional.empty();
	private ArrayList<SkillSetView> skillSetViews = new ArrayList<>();

	@FXML
	private Label selectUserInputAlertLabel;

	@FXML
	VBox mainVBox;

	@FXML
	TextField directoryTextField;

	@FXML
	Button execute;

	@FXML
	Label executionMsg;

	public void initialize() {
		addSkillSet();
	}

	public void browseProfileDirectory(ActionEvent actionEvent) {
		chooser.setTitle("Choose Profiles Folder location");
		File chosenFile = chooser.showDialog(((Node) actionEvent.getTarget()).getScene().getWindow());
		file = Optional.ofNullable(chosenFile);
		directoryTextField.setText(
				file.map((Function<File, Object>) File::getAbsolutePath).orElse(" No Directory Selected.").toString());
		if (file.isPresent() && !file.get().equals(" No Directory Selected.")) {
			selectUserInputAlertLabel.setText("");
		}
	}

	public void executeRule() {
		if (!file.isPresent()) {
			selectUserInputAlertLabel.setText("Please select directory");
		} else {
			if (skillSetViews.get(0).areSkillsPresent()) {
				new Application().parseProfiles(file.get(), getSelectionRule());
				executionMsg.setText("      Execution Completed.");
			} else {
				selectUserInputAlertLabel.setText("Please add skill details and then click on Execute");
			}
		}
	}

	private Rule getSelectionRule() {
		ArrayList<Rule> combinedSkillSetRules = new ArrayList<>();
		for (SkillSetView skillSetView : skillSetViews) {
			combinedSkillSetRules.add(skillSetView.createRule());
		}
		return new AllRules(combinedSkillSetRules);
	}

	@Override
	public void addSkillSet() {
		SkillSetView skillSetView = new SkillSetView(this);
		mainVBox.getChildren().add(skillSetView);
		skillSetViews.add(skillSetView);
	}

	@Override
	public void removeSkillSet(SkillSetView skillSetView) {
		if (skillSetViews.size() != 1) {
			mainVBox.getChildren().remove(skillSetView);
			skillSetViews.remove(skillSetView);
		}
	}
}
