package ui;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import rules.AllRules;
import rules.AnyRule;
import rules.Rule;

@SuppressWarnings("restriction")
public class SkillSetView extends VBox implements Skill {
    private ArrayList<SkillView> skillViews = new ArrayList<>();
    private ChoiceBox<String> andOrConditionChoiceBox;

    @FXML
    private Label removeSkillAlertLabel;

    SkillSetView(SkillSet skillSet) {
        super();
        this.fillWidthProperty().setValue(true);
        this.setSpacing(10);

        HBox hBox = new HBox();
        hBox.setSpacing(17);

        createSelectResumeLabel(hBox);
        createAndOrConditionChoiceBox(hBox);
        createAddNewSkillSetButton(hBox, skillSet);
        createRemoveSkillSetButton(hBox, skillSet);
        createRemoveSkillAlertLabel(hBox);

        this.getChildren().add(hBox);
    }

    private void createRemoveSkillAlertLabel(HBox hBox) {
        removeSkillAlertLabel = new Label();
        removeSkillAlertLabel.setTextFill(Color.web("#DC143C"));
        hBox.getChildren().add(removeSkillAlertLabel);
    }

    private void createSelectResumeLabel(HBox hBox) {
        Label label = new Label();
        label.setText("Select Resume When :");
        hBox.getChildren().add(label);
    }

    private void createAndOrConditionChoiceBox(HBox hBox) {
        andOrConditionChoiceBox = new ChoiceBox<String>(FXCollections.observableArrayList(
                "Select a condition",
                "All of the Skills are met",
                "Any of the Skills are met"));
        andOrConditionChoiceBox.setStyle("choice-box");
        andOrConditionChoiceBox.setValue("Select a condition");
        andOrConditionChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (andOrConditionChoiceBox.getSelectionModel().getSelectedIndex() != 0 && skillViews.size() == 0) {
                    addSkill();
                }
            }
        });
        hBox.getChildren().add(andOrConditionChoiceBox);
    }

    private void createAddNewSkillSetButton(HBox hBox, SkillSet skillSet) {
        Button addNewSkillSetButton = new Button();
        addNewSkillSetButton.setText("Add New SkillSet");
        addNewSkillSetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (skillViews.size() != 0) {
                    skillSet.addSkillSet();
                }
            }
        });
        hBox.getChildren().add(addNewSkillSetButton);
    }

    private void createRemoveSkillSetButton(HBox hBox, SkillSet skillSet) {
        Button removeSkillSetButton = new Button();
        removeSkillSetButton.setText("Remove SkillSet");
        removeSkillSetButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (skillViews.size() == 0) {
                    skillSet.removeSkillSet(SkillSetView.this);
                } else {
                    removeSkillAlertLabel.setText("Remove skills first");
                }
            }
        });
        hBox.getChildren().add(removeSkillSetButton);
    }

    boolean areSkillsPresent() {
        return (skillViews.size() != 0 && skillViews.get(0).areSkillDetailsPresent());
    }

    Rule createRule() {
        ArrayList<Rule> rules = new ArrayList<>();
        for (SkillView view : skillViews) {
            rules.add(view.createRule());
        }
        if (andOrConditionChoiceBox.getSelectionModel().getSelectedIndex() == 1) {
            return new AllRules(rules);
        } else if (andOrConditionChoiceBox.getSelectionModel().getSelectedIndex() == 2) {
            return new AnyRule(rules);
        }
        return null;
    }

    @Override
    public void addSkill() {
        SkillView skillView = new SkillView(this);
        this.getChildren().add(skillView);
        skillViews.add(skillView);
    }

    @Override
    public void removeSkill(SkillView skillView) {
        this.getChildren().remove(skillView);
        skillViews.remove(skillView);
    }
}
