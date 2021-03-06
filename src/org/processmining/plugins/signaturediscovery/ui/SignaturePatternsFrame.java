package org.processmining.plugins.signaturediscovery.ui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.processmining.plugins.signaturediscovery.DiscoverSignatures;
import org.processmining.plugins.signaturediscovery.metrics.Metrics;
import org.processmining.plugins.signaturediscovery.swingx.ScrollableGridLayout;
import org.processmining.plugins.signaturediscovery.util.Logger;

import com.fluxicon.slickerbox.factory.SlickerFactory;

/**
 * @author R.P. Jagadeesh Chandra 'JC' Bose
 * @date 14 July 2010 
 * @since 01 July 2010
 * @version 1.0
 * @email j.c.b.rantham.prabhakara@tue.nl
 * @copyright R.P. Jagadeesh Chandra 'JC' Bose
 * 			  Architecture of Information Systems Group (AIS) 
 * 			  Department of Mathematics and Computer Science
 * 			  University of Technology, Eindhoven, The Netherlands
 */

@SuppressWarnings("serial")
public class SignaturePatternsFrame extends JInternalFrame {
	DiscoverSignatures discoverSignatures;
	JPanel signaturePatternsPanel;
	JPanel parameterSettingsPanel;
	JPanel ruleDisplayPanel;
	JPanel rulesHeaderPanel;
	JPanel controlPanel;
	JPanel overallPerformancePanel;
	List<RuleComponent> ruleComponentList;
	JScrollPane signaturePatternsScrollPane;
	
	public SignaturePatternsFrame(DiscoverSignatures discoverSignatures){
		this.discoverSignatures = discoverSignatures;
		signaturePatternsPanel =  SlickerFactory.instance().createRoundedPanel();
		signaturePatternsPanel.setBackground(Color.white);
		ScrollableGridLayout signaturePatternsPanelLayout = new ScrollableGridLayout(signaturePatternsPanel, 1, 3, 0, 0);
//		signaturePatternsPanelLayout.setRowFixed(0, true);
		signaturePatternsPanelLayout.setRowFixed(1, true);
//		signaturePatternsPanelLayout.setColumnFixed(1, true);
		signaturePatternsPanel.setLayout(signaturePatternsPanelLayout);
		
		buildRuleDisplayPanel();
//		buildParameterSettingsPanel();
		JScrollPane ruleDisplayScrollPane = new JScrollPane(ruleDisplayPanel);
		signaturePatternsPanelLayout.setPosition(ruleDisplayScrollPane, 0, 0);
		signaturePatternsPanel.add(ruleDisplayScrollPane);
		
		buildControlPanel();
		signaturePatternsPanelLayout.setPosition(controlPanel, 0, 1);
		signaturePatternsPanel.add(controlPanel);
		
		buildOverallPerformancePanel();
		signaturePatternsPanelLayout.setPosition(overallPerformancePanel, 0, 2);
		signaturePatternsPanel.add(overallPerformancePanel);
		
//		signaturePatternsPanelLayout.setPosition(parameterSettingsPanel, 1, 0);
//		signaturePatternsPanel.add(parameterSettingsPanel);
		
		signaturePatternsScrollPane = new JScrollPane(signaturePatternsPanel);
		getContentPane().add(signaturePatternsScrollPane);
		getContentPane().validate();
		getContentPane().repaint();
		pack();
		this.show();
	}
	
	private void buildRuleDisplayPanel(){
		Logger.printCall("Entering buildRuleDisplayPanel");
		Color backgroundColor;
		if(ruleDisplayPanel == null)
			ruleDisplayPanel = SlickerFactory.instance().createRoundedPanel();
		else
			ruleDisplayPanel.removeAll();
		
		ruleDisplayPanel.setBackground(Color.white);
		backgroundColor = ruleDisplayPanel.getBackground();
		ruleComponentList = new ArrayList<RuleComponent>();
		RuleComponent ruleComponent;
		if(!discoverSignatures.hasSignatures()){
			ruleDisplayPanel.add(new JLabel("No Signatures Found"));
			return;
		}
		Map<String, Metrics> ruleMetricsMap = discoverSignatures.getFinalRuleListMetricsMap();
		Map<String, String> encodedDecodedRuleMap = discoverSignatures.getEncodedDecodedRuleMap();
		
		int noRules = ruleMetricsMap.size();

		ScrollableGridLayout ruleDisplayPanelLayout = new ScrollableGridLayout(ruleDisplayPanel, 1, noRules+2, 0, 0);
		for(int i = 0; i < noRules+1; i++)
			ruleDisplayPanelLayout.setRowFixed(i, true);
		ruleDisplayPanel.setLayout(ruleDisplayPanelLayout);
		
		buildRulesHeaderPanel();
		ruleDisplayPanelLayout.setPosition(rulesHeaderPanel, 0, 0);
		ruleDisplayPanel.add(rulesHeaderPanel);
		
		int index = 1;
		for(String rule : ruleMetricsMap.keySet()){
			if(encodedDecodedRuleMap.containsKey(rule)){
				ruleComponent = new RuleComponent(backgroundColor, rule, encodedDecodedRuleMap.get(rule), ruleMetricsMap.get(rule));
				ruleComponentList.add(ruleComponent);
				ruleDisplayPanelLayout.setPosition(ruleComponent, 0, index);
				ruleDisplayPanel.add(ruleComponent);
				index++;
			}else{
				System.out.println("ERROR: NO Decoded Rule");
			}
		}
	
		Logger.printReturn("Exiting buildRuleDisplayPanel");
	}
	
	private void buildControlPanel(){
		controlPanel = SlickerFactory.instance().createRoundedPanel();
		controlPanel.setBackground(Color.white);
		controlPanel.setBorder(BorderFactory.createEtchedBorder());
		ScrollableGridLayout controlPanelLayout = new ScrollableGridLayout(controlPanel, 4, 1, 0, 0);
//		controlPanelLayout.setColumnFixed(0, true);
		controlPanelLayout.setColumnFixed(1, true);
		controlPanelLayout.setColumnFixed(2, true);
		controlPanelLayout.setColumnFixed(3, true);
		
		controlPanel.setLayout(controlPanelLayout);
		
		final JCheckBox showOnlyPresenceConstraintsCheckBox = SlickerFactory.instance().createCheckBox("Show only presence constranits   ", false);
		
		JButton selectAllButton = SlickerFactory.instance().createButton("Select All");
		JButton invertSelectionButton = SlickerFactory.instance().createButton("Invert Selection");
		JButton exportSignaturePatternsButton = SlickerFactory.instance().createButton("Export Patterns");
		
		
		controlPanelLayout.setPosition(showOnlyPresenceConstraintsCheckBox, 0, 0);
		controlPanel.add(showOnlyPresenceConstraintsCheckBox);

		controlPanelLayout.setPosition(selectAllButton, 1, 0);
		controlPanel.add(selectAllButton);
		
		controlPanelLayout.setPosition(invertSelectionButton, 2, 0);
		controlPanel.add(invertSelectionButton);
		
		controlPanelLayout.setPosition(exportSignaturePatternsButton, 3, 0);
		controlPanel.add(exportSignaturePatternsButton);
		
		showOnlyPresenceConstraintsCheckBox.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(RuleComponent r : ruleComponentList)
					r.showPresenceConstraintsOnly(showOnlyPresenceConstraintsCheckBox.isSelected());
			}
		});
		
		selectAllButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(RuleComponent r : ruleComponentList)
					r.setSelected(true);
			}
		});
		
		invertSelectionButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				for(RuleComponent r : ruleComponentList)
					r.setSelected(!r.isSelected());
			}
		});
	}
	
	private void buildOverallPerformancePanel(){
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		overallPerformancePanel = SlickerFactory.instance().createRoundedPanel();
		overallPerformancePanel.setBackground(Color.white);
		ScrollableGridLayout overallPerformancePanelLayout = new ScrollableGridLayout(overallPerformancePanel, 2, 6, 0, 0);
		overallPerformancePanelLayout.setRowFixed(0, true);
		overallPerformancePanelLayout.setRowFixed(1, true);
		overallPerformancePanelLayout.setRowFixed(2, true);
		
		overallPerformancePanel.setLayout(overallPerformancePanelLayout);
		
		JLabel featureLabel = SlickerFactory.instance().createLabel("Feature ");
		JLabel featureValueLabel = SlickerFactory.instance().createLabel(discoverSignatures.getMaxFeature().toString());
		
		JLabel optionsLabel = SlickerFactory.instance().createLabel("Options ");
		JLabel optionsValueLabel = SlickerFactory.instance().createLabel(discoverSignatures.getMaxOptionsString());
		
		JLabel f1ScoreLabel = SlickerFactory.instance().createLabel("F1 Score ");
		JLabel f1ScoreValueLabel = SlickerFactory.instance().createLabel(decimalFormat.format(discoverSignatures.getMaxF1Score()));
		
		JLabel precisionLabel = SlickerFactory.instance().createLabel("Precision ");
		JLabel precisionValueLabel = SlickerFactory.instance().createLabel(decimalFormat.format(discoverSignatures.getPrecisionPerMaxF1Score()));
				
		JLabel recallLabel = SlickerFactory.instance().createLabel("Recall ");
		JLabel recallValueLabel = SlickerFactory.instance().createLabel(decimalFormat.format(discoverSignatures.getRecallPerMaxF1Score()));
		
		JLabel accuracyLabel = SlickerFactory.instance().createLabel("Accuracy ");
		JLabel accuracyValueLabel = SlickerFactory.instance().createLabel(decimalFormat.format(discoverSignatures.getAccuracyPerMaxF1Score()));	
		
		overallPerformancePanelLayout.setPosition(featureLabel, 0, 0);
		overallPerformancePanel.add(featureLabel);
		overallPerformancePanelLayout.setPosition(featureValueLabel, 1, 0);
		overallPerformancePanel.add(featureValueLabel);
		
		overallPerformancePanelLayout.setPosition(optionsLabel, 0, 1);
		overallPerformancePanel.add(optionsLabel);
		overallPerformancePanelLayout.setPosition(optionsValueLabel, 1, 1);
		overallPerformancePanel.add(optionsValueLabel);
		
		overallPerformancePanelLayout.setPosition(f1ScoreLabel, 0, 2);
		overallPerformancePanel.add(f1ScoreLabel);
		overallPerformancePanelLayout.setPosition(f1ScoreValueLabel, 1, 2);
		overallPerformancePanel.add(f1ScoreValueLabel);
		
		overallPerformancePanelLayout.setPosition(precisionLabel, 0, 3);
		overallPerformancePanel.add(precisionLabel);
		overallPerformancePanelLayout.setPosition(precisionValueLabel, 1, 3);
		overallPerformancePanel.add(precisionValueLabel);
		
		overallPerformancePanelLayout.setPosition(recallLabel, 0, 4);
		overallPerformancePanel.add(recallLabel);
		overallPerformancePanelLayout.setPosition(recallValueLabel, 1, 4);
		overallPerformancePanel.add(recallValueLabel);		
		
		overallPerformancePanelLayout.setPosition(accuracyLabel, 0, 5);
		overallPerformancePanel.add(accuracyLabel);
		overallPerformancePanelLayout.setPosition(accuracyValueLabel, 1, 5);
		overallPerformancePanel.add(accuracyValueLabel);		
		
	}
	
	private void buildRulesHeaderPanel(){
		rulesHeaderPanel = SlickerFactory.instance().createRoundedPanel();
		rulesHeaderPanel.setBackground(Color.white);
		rulesHeaderPanel.setLayout(new BoxLayout(rulesHeaderPanel, BoxLayout.X_AXIS));
		
		JLabel selectRuleLabel = new JLabel("Select");
		JLabel ruleLabel = new JLabel("Rule");
		JLabel classLabel = new JLabel("Class");
		JLabel metricsLabel = new JLabel("TP   FP   TN   FN   TPR     TNR     Precision   F1Score   ");
		
		rulesHeaderPanel.add(selectRuleLabel);
		rulesHeaderPanel.add(Box.createHorizontalStrut(15));
		
		rulesHeaderPanel.add(ruleLabel);
		rulesHeaderPanel.add(Box.createGlue());
		rulesHeaderPanel.add(classLabel);
		rulesHeaderPanel.add(Box.createHorizontalStrut(15));
		rulesHeaderPanel.add(metricsLabel);
		rulesHeaderPanel.add(Box.createHorizontalStrut(15));
	}

	@SuppressWarnings("unused")
	private void buildParameterSettingsPanel(){
		parameterSettingsPanel = new JPanel();
		ScrollableGridLayout parameterSettingsPanelLayout = new ScrollableGridLayout(parameterSettingsPanel, 2, 3, 0, 0);
		parameterSettingsPanelLayout.setRowFixed(0, true);
		parameterSettingsPanel.setLayout(parameterSettingsPanelLayout);
		
		JCheckBox showSignaturePatternsForSpecificClassCheckBox = new JCheckBox("Show signature patterns only for class   ");
		JComboBox signaturePatternsForClassComboBox = new JComboBox(discoverSignatures.getSignatureDiscoveryInput().getGenerateSignaturesForClassLabelSet().toArray());
		
		parameterSettingsPanelLayout.setPosition(showSignaturePatternsForSpecificClassCheckBox, 0, 0);
		parameterSettingsPanel.add(showSignaturePatternsForSpecificClassCheckBox);
		parameterSettingsPanelLayout.setPosition(signaturePatternsForClassComboBox, 1, 0);
		parameterSettingsPanel.add(signaturePatternsForClassComboBox);
	}
}
