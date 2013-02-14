package gui;

import java.awt.EventQueue;
import java.awt.Panel;
import java.awt.Window;

import javax.swing.JFrame;
import java.awt.CardLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.plaf.FileChooserUI;
import javax.swing.JSeparator;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import java.awt.Choice;
import javax.swing.SwingConstants;

public class windowRNA {
	
	private JFrame frame;
	private JComboBox comboBox;
	private JComboBox comboBox_1;
	private JComboBox comboBox_2;
	private JComboBox comboBox_3;
	private JComboBox comboBox_4;
	private JComboBox comboBox_5;
	private JComboBox comboBox_6;
	private JLabel label_1;
	private JFormattedTextField learningRate;
	private JFormattedTextField numberOfExamples;
	private JLabel label_2;
	JFormattedTextField rateOfTrainning;
	JFormattedTextField quantityOfIterations;
	JFormattedTextField quantityFail;
	private JTextField pathFile_1;
	JButton btnDatabaseFile;
	String pathFile;
	JTextArea logArea;
	
	JComboBox numberInputs;
	JComboBox numberOutputs;
	
	JComboBox numberHidden;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					windowRNA window = new windowRNA();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public windowRNA() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	void inicializeParamaters() {
		rateOfTrainning.setText("50");
		learningRate.setText("0.01");
		numberOfExamples.setText("100");
		quantityOfIterations.setText("5000");
		quantityFail.setText("10");
		
		for(int i=1; i<50; i++){
			numberInputs.addItem(""+i);
			numberOutputs.addItem(""+i);
			numberHidden.addItem(""+i);
		}
		
		
	}
	void setShowingHiddenLayer(int quantity){
		if (quantity >= 1){
			comboBox.show(true);
		}
		if (quantity >= 2){
			comboBox_1.show(true);	
		}
		if (quantity >= 3){
			comboBox_2.show(true);
		}
		if (quantity >= 4){
			comboBox_3.show(true);
		}
		if (quantity >= 5){
			comboBox_4.show(true);
		}
		if (quantity>= 6){
			comboBox_5.show(true);
		}
	}
	
	void resetShowingHiddenLayer(){
		
		comboBox.hide();
		comboBox_1.hide();
		comboBox_2.hide();
		comboBox_3.hide();
		comboBox_4.hide();
		comboBox_5.hide();
		comboBox_6.hide();
		comboBox.setVisible(true);
		//comboBox_3.show(false);
		//comboBox_3.setVisible(true);
		//comboBox_3.show(true);
		
	}
	
	void loadNumberHiddenLayer(){
		for (int j=1; j<50; j++){
			comboBox.addItem(""+j);
			comboBox_1.addItem(""+j);
			comboBox_2.addItem(""+j);
			comboBox_3.addItem(""+j);
			comboBox_4.addItem(""+j);
			comboBox_5.addItem(""+j);
			comboBox_6.addItem(""+j);
			
		}
		
	}
	
	void setInLog(String txt){
		logArea.setText(logArea.getText()+txt+"\n");
	}
	
	void eraseLog(){
		logArea.setText("");
	}
	
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setEnabled(false);
		frame.setBounds(100, 100, 641, 499);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(169, 110, 1, 2);
		
		JLabel lblInput = new JLabel("Number of Neurons in Inputs");
		lblInput.setBounds(65, 73, 225, 23);
		
		JLabel lblArtificialNeuralNetwork = new JLabel("Artificial Neural Network  - Multi Layer Perceptron");
		lblArtificialNeuralNetwork.setBounds(117, 11, 352, 15);
		
		JLabel label = new JLabel("Number of Neurons in Outputs");
		label.setBounds(65, 108, 216, 23);
		
		JLabel lblNewLabel = new JLabel("Number of Hidden Layers");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		lblNewLabel.setBounds(33, 143, 257, 15);
		
		comboBox = new JComboBox();
		comboBox.setBounds(219, 183, 47, 24);
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(278, 183, 46, 24);
		comboBox_2 = new JComboBox();
		comboBox_2.setBounds(336, 183, 47, 24);
		comboBox_3 = new JComboBox();
		comboBox_3.setBounds(395, 183, 47, 24);
		comboBox_4 = new JComboBox();
		comboBox_4.setBounds(455, 183, 47, 24);
		comboBox_5 = new JComboBox();
		comboBox_5.setBounds(507, 183, 47, 24);
		comboBox_6 = new JComboBox();
		comboBox_6.setBounds(562, 183, 54, 24);

		final JFileChooser fc = new JFileChooser();
	
		fc.setBounds(12, 12, 615, 113);
		
		JButton btnNewButton = new JButton("set");
		btnNewButton.setBounds(377, 142, 70, 24);
		btnNewButton.addActionListener(new ActionListener() {
			
			 		        
			public void actionPerformed(ActionEvent arg0) {
				int quantity = Integer.parseInt(numberHidden.getSelectedItem().toString());
				resetShowingHiddenLayer();
				setShowingHiddenLayer(quantity);
				
				
			}
		});
		
		label_1 = new JLabel("Learning Rate");
		label_1.setBounds(171, 213, 120, 15);
		
		learningRate = new JFormattedTextField();
		learningRate.setBounds(307, 211, 106, 19);
		learningRate.setText("1");
		
		numberOfExamples = new JFormattedTextField();
		numberOfExamples.setBounds(307, 238, 106, 19);
		numberOfExamples.setText("1");
		
		label_2 = new JLabel("Number of Examples");
		label_2.setBounds(143, 240, 148, 15);
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(lblArtificialNeuralNetwork);
		frame.getContentPane().add(separator);
		frame.getContentPane().add(lblInput);
		frame.getContentPane().add(label);
		frame.getContentPane().add(lblNewLabel);
		frame.getContentPane().add(comboBox_4);
		frame.getContentPane().add(label_1);
		frame.getContentPane().add(btnNewButton);
		frame.getContentPane().add(comboBox_5);
		frame.getContentPane().add(comboBox_6);
		frame.getContentPane().add(comboBox);
		frame.getContentPane().add(label_2);
		frame.getContentPane().add(numberOfExamples);
		frame.getContentPane().add(comboBox_1);
		frame.getContentPane().add(learningRate);
		frame.getContentPane().add(comboBox_2);
		frame.getContentPane().add(comboBox_3);
		
		
		JLabel label_3 = new JLabel("Rate of Trainning (%)");
		label_3.setBounds(133, 264, 158, 15);
		frame.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("Quantity of Iterations");
		label_4.setBounds(122, 291, 169, 15);
		frame.getContentPane().add(label_4);
		
		JLabel label_5 = new JLabel("Quantity of Allow Fails");
		label_5.setBounds(133, 318, 158, 15);
		frame.getContentPane().add(label_5);
		
		rateOfTrainning = new JFormattedTextField();
		rateOfTrainning.setText("1");
		rateOfTrainning.setBounds(307, 262, 106, 19);
		frame.getContentPane().add(rateOfTrainning);
		
		quantityOfIterations = new JFormattedTextField();
		quantityOfIterations.setText("1");
		quantityOfIterations.setBounds(307, 289, 106, 19);
		frame.getContentPane().add(quantityOfIterations);
		
		quantityFail = new JFormattedTextField();
		quantityFail.setText("1");
		quantityFail.setBounds(307, 317, 106, 19);
		frame.getContentPane().add(quantityFail);
		
		JButton btnRun = new JButton("RUN");
		
		
		
		
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int var_rateTraining = Integer.parseInt(rateOfTrainning.getText());
				int var_quantityOfIterations = Integer.parseInt(quantityOfIterations.getText());
				int var_quantityFail = Integer.parseInt(quantityFail.getText());
				double var_learningRate = Double.parseDouble(learningRate.getText());
				int var_numberOfExamples = Integer.parseInt(numberOfExamples.getText());
				
				int var_numberInput = Integer.parseInt(numberInputs.getSelectedItem().toString());
				int var_numberOutput = Integer.parseInt(numberOutputs.getSelectedItem().toString());
				int var_numberHidden= Integer.parseInt(numberHidden.getSelectedItem().toString());
				
				int var_quantityNeurosPerLayer [] = new int[var_numberHidden + 2];
				var_quantityNeurosPerLayer[0] = var_numberInput;
				
				for (int i=1; i<=var_numberHidden; i++){
					int valor =0;
					if (i==1){
						valor = Integer.parseInt(comboBox.getSelectedItem().toString());
					}else if (i==2){
						valor = Integer.parseInt(comboBox_1.getSelectedItem().toString());
					}else if(i==3){
						valor = Integer.parseInt(comboBox_2.getSelectedItem().toString());
					}else if(i==4){
						valor = Integer.parseInt(comboBox_3.getSelectedItem().toString());
					}else if(i==5){
						valor = Integer.parseInt( comboBox_4.getSelectedItem().toString());
					}else if(i==6){
						valor = Integer.parseInt(comboBox_5.getSelectedItem().toString());
					}
					var_quantityNeurosPerLayer[i] = valor;
					System.out.println(valor);
				}
				
				setInLog("Fernando teste FernandO");
				
			}
		});
		btnRun.setBounds(510, 348, 117, 25);
		frame.getContentPane().add(btnRun);
		
		pathFile_1 = new JTextField();
		pathFile_1.setBounds(54, 38, 393, 23);
		frame.getContentPane().add(pathFile_1);
		pathFile_1.setColumns(10);
		
		//int returnVal = fc.showOpenDialog(frame);
		
		btnDatabaseFile = new JButton("Database File ");
		btnDatabaseFile.setBounds(459, 32, 168, 31);
		frame.getContentPane().add(btnDatabaseFile);
		
		JProgressBar progressBar = new JProgressBar();
		progressBar.setBounds(208, 443, 148, 14);
		frame.getContentPane().add(progressBar);
		
		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBounds(49, 348, 437, 85);
		frame.getContentPane().add(logArea);
		
		JButton button = new JButton("erase log");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				eraseLog();
			}
		});
		button.setBounds(510, 385, 117, 25);
		frame.getContentPane().add(button);
		
		numberInputs = new JComboBox();
		numberInputs.setBounds(302, 72, 54, 24);
		frame.getContentPane().add(numberInputs);
		
		numberOutputs = new JComboBox();
		numberOutputs.setBounds(302, 107, 54, 24);
		frame.getContentPane().add(numberOutputs);
		
		numberHidden = new JComboBox();
		numberHidden.setBounds(302, 142, 54, 24);
		frame.getContentPane().add(numberHidden);
		
		JLabel lblNumberOfNeurons = new JLabel("Number of Neurons ");
		lblNumberOfNeurons.setHorizontalAlignment(SwingConstants.LEFT);
		lblNumberOfNeurons.setBounds(22, 168, 190, 23);
		frame.getContentPane().add(lblNumberOfNeurons);
		
		JLabel lblForEachHidden = new JLabel("for each Hidden Layer");
		lblForEachHidden.setHorizontalAlignment(SwingConstants.LEFT);
		lblForEachHidden.setBounds(22, 184, 190, 23);
		frame.getContentPane().add(lblForEachHidden);
		
		/*
		 * 
		 
		 * 
		 */
		inicializeParamaters();
		resetShowingHiddenLayer();
		loadNumberHiddenLayer();
		
		
		btnDatabaseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 if (arg0.getSource() == btnDatabaseFile) {
				        int returnVal = fc.showOpenDialog(null);

				        if (returnVal == JFileChooser.APPROVE_OPTION) {
				            File file = fc.getSelectedFile();
				        	pathFile = file.getPath();
				        	pathFile_1.setText(pathFile);
				            //This is where a real application would open the file.
				            //log.append("Opening: " + file.getName() + "." + newline);
				        } else {
				            //log.append("Open command cancelled by user." + newline);
				        }
				   }
			}
		});
		
	}
}
