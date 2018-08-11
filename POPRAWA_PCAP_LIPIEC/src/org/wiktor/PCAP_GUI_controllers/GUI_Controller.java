package org.wiktor.PCAP_GUI_controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.wiktor.PCAP_GUI.operations.Bufor;
import org.wiktor.PCAP_GUI.operations.InputPacket;
import org.wiktor.PCAP_GUI.operations.OutputPacket;
import org.wiktor.PCAP_GUI.operations.PcapFileOperations;
import org.wiktor.PCAP_GUI.operations.Report;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class GUI_Controller {

	/*CREATE BUFOR*/
	Bufor bufor = new Bufor(0);

	/*SUPPORTING VARIABLES*/
	String input_path = null;
	String output_path = null;
	int input_packet_size;
	int output_packet_size;
	long delay;

	/*GET INPUT AND OUTPUT PCAP FILES*/
	PcapFileOperations pcapfile_input = new PcapFileOperations();
	PcapFileOperations pcapfile_output = new PcapFileOperations();

	ArrayList<Long> input_time_array;
	ArrayList<Long> dts_time_array;
	ArrayList<InputPacket> input_packet_array;
	ArrayList<OutputPacket> output_packet_array;

	@FXML
	private Pane paneBCK;

	@FXML
	private Button restartButton;

	@FXML
	private Button outputFileButton;

	@FXML
	private Button generateButton;

	@FXML
	private Label delayInputLabel;

	@FXML
	private Label raportGenerateLabel;

	@FXML
	private Button closeAppButton;

	@FXML
	private Button checkDelayButton;

	@FXML
	private Button inputFileButton;

	@FXML
	private TextField delayInput;

	@FXML
	private TextField outputFileLabel;

	@FXML
	private TextField inputFileLabel;

	@FXML
	void chooseFileToInput(ActionEvent event) {
		// System.out.println("Choose file to input!");
		try {
			input_path = pcapfile_input.openFileChooserDialog("Podaj œcie¿kê pliku WEJŒCIOWEGO: (format *.txt):");
			inputFileLabel.setText(input_path);
			System.out.println("Input file path: " + input_path);
			pcapfile_input.setFileAdress(input_path);

			input_packet_array = pcapfile_input.readOfflineFiles_INPUT();
			input_time_array = pcapfile_input.make_timings_array_in(input_packet_array);
			input_packet_size = 0;

			for (int i = 0; i < input_time_array.size(); i++) {
				input_packet_size += input_packet_array.get(i).getTcp_payload_data();
			}

			/*System.out.println("Input ARRAY: " + input_packet_array);*/
			System.out.println("Input packets size: " + input_packet_size + " KB");

		} catch (Exception e) {

		}

	}

	@FXML
	void chooseFileToOutput(ActionEvent event) {
		// System.out.println("Choose file to output!");
		try {
			output_path = pcapfile_output.openFileChooserDialog("Podaj œcie¿kê pliku WYJŒCIOWEGO: (format *.txt):");
			outputFileLabel.setText(output_path);
			System.out.println("Output file path: " + output_path);
			pcapfile_output.setFileAdress(output_path);

			output_packet_array = pcapfile_output.readOfflineFiles_OUTPUT();
			dts_time_array = pcapfile_output.make_timings_array_out(output_packet_array);
			output_packet_size = 0;

			for (int i = 0; i < output_packet_array.size(); i++) {
				output_packet_size += output_packet_array.get(i).getDATA_SIZE();
			}

			/*System.out.println("Output ARRAY: " + output_packet_array);*/
			System.out.println("Output packets size: " + output_packet_size + " KB");

		} catch (Exception e) {

		}

	}

	@FXML
	void closeApp(ActionEvent event) {
		// System.out.println("Close!");
		System.exit(0);

	}

	@FXML
	void checkCorrectInput(ActionEvent event) {

		if (!inputFileLabel.getText().isEmpty() && !outputFileLabel.getText().isEmpty()
				&& !delayInput.getText().isEmpty()) {

			if (delayInput.getText().matches("^[+]?[0-9]*\\.?[0-9]+([eE][-+]?[0-9]+)?$")) {
				delayInputLabel.setText("Good input!");
				delayInputLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");

				if (pcapfile_input.getFileName().contains(".txt") && pcapfile_output.getFileName().contains(".txt")) {
					raportGenerateLabel.setText("Everything is good! Click generate!");
					raportGenerateLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
					generateButton.setDisable(false);
					inputFileButton.setDisable(true);
					outputFileButton.setDisable(true);
					delayInput.setDisable(true);
					restartButton.setVisible(true);
				} else {
					raportGenerateLabel.setText("Wrong input/output filetype!");
					raportGenerateLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
				}
			} else {
				delayInputLabel.setText("Wrong input!");
				delayInputLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
			}

		} else if (!inputFileLabel.getText().isEmpty() && !outputFileLabel.getText().isEmpty()
				&& delayInput.getText().isEmpty()) {
			delayInputLabel.setText("Type delay time!");
			delayInputLabel.setStyle("-fx-text-fill: blue; -fx-font-weight: bold;");
		} else {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Choose *.txt input and output file to continue...");
			alert.showAndWait();
		}
	}

	@FXML
	void generateReport(ActionEvent event) {
		delay = bufor.changeSecondsToNanos(Double.valueOf(delayInput.getText()));

		System.out.println("DELAY in nanos: " + delay);

		/*System.out.println("BEFORE:\n");
		System.out.println("INPUT DATA: " + input_packet_array.size());
		System.out.println("OUTPUT DATA: " + output_packet_array.size());*/

		// DELAY START

		for (int i = 0; i < input_packet_array.size(); i++) {
			if (input_packet_array.get(i).getInputTime() < delay) {
				input_packet_array.remove(input_packet_array.get(i));
				i--;
			}
		}

		for (int i = 0; i < output_packet_array.size(); i++) {
			if (output_packet_array.get(i).getInputTime() < delay) {
				output_packet_array.remove(output_packet_array.get(i));
				i--;
			}
		}

		// DELAY STOP

		// CHECK HOW MANY DTS
		int DTS_count = 0;

		for (int i = 0; i < output_packet_array.size(); i++) {
			DTS_count = DTS_count + output_packet_array.get(i).getDTS_timings().size();
		}

		int MP2T_WITHDRAW_CUMULATIONS = 0;

		for (int i = 0; i < 1; i++) {

			InputPacket input_packet = new InputPacket();
			OutputPacket output_packet = new OutputPacket();

			if (!input_packet_array.isEmpty()) {
				input_packet = input_packet_array.get(0);
			}

			if (!output_packet_array.isEmpty()) {
				output_packet = output_packet_array.get(0);

			}

			if (!input_packet_array.isEmpty() && !output_packet_array.isEmpty()) {

				if (output_packet.getDTS_timings().isEmpty()) {
					// ITERACJA KUMULACJI!
					MP2T_WITHDRAW_CUMULATIONS = MP2T_WITHDRAW_CUMULATIONS + 7;
					output_packet_array.remove(0);
					i--;

				} else if (!output_packet_array.get(i).getDTS_timings().isEmpty()) {
					// DTS JEST

					if ((input_packet.getInputTime() < output_packet.getDTS_timings().get(0))) {
						// DODANIE DO BUFORA Z INPUT PACKET
						bufor.addData(input_packet.getTcp_payload_data());
						input_packet_array.remove(0);
						i--;

					} else if ((input_packet.getInputTime() > output_packet.getDTS_timings().get(0))) {
						// OPERACJE NA DTS

						for (int j = 0; j < 1; j++) {

							if (output_packet.getEventArray().get(j).equals("(DTS)") && (j == 0)) {

								bufor.subtractData(188 * (MP2T_WITHDRAW_CUMULATIONS));

								MP2T_WITHDRAW_CUMULATIONS = 0;
								output_packet.getEventArray().remove(0);

								bufor.checkBufferStatus(output_packet.getDTS_timings().get(0));

								output_packet.getDTS_timings().remove(0);
								j--;

							} else if (output_packet.getEventArray().get(j).equals("MP2T") && (j == 0)) {
								MP2T_WITHDRAW_CUMULATIONS++;
								output_packet.getEventArray().remove(0);
								j--;
							}

							if (output_packet.getEventArray().isEmpty()) {
								output_packet_array.remove(0);
								break;
							}

						}

						i--;

					}

				}

			}

		}

		if (!input_packet_array.isEmpty() && output_packet_array.isEmpty()) {
			for (int j = 0; j < input_packet_array.size(); j++) {
				bufor.addData(input_packet_array.get(j).getTcp_payload_data());
			}
		}

		if (input_packet_array.isEmpty() && !output_packet_array.isEmpty()) {

			for (int i = 0; i < output_packet_array.size(); i++) {
				if (output_packet_array.get(i).getDTS_timings().isEmpty()) {
					MP2T_WITHDRAW_CUMULATIONS = MP2T_WITHDRAW_CUMULATIONS + 7;

				} else if (!output_packet_array.get(i).getDTS_timings().isEmpty()) {
					for (int j = 0; j < 1; j++) {

						if (output_packet_array.get(i).getEventArray().get(j).equals("(DTS)") && (j == 0)) {

							bufor.subtractData(188 * (MP2T_WITHDRAW_CUMULATIONS));

							MP2T_WITHDRAW_CUMULATIONS = 0;
							output_packet_array.get(i).getEventArray().remove(0);

							bufor.checkBufferStatus(output_packet_array.get(i).getDTS_timings().get(0));

							output_packet_array.get(i).getDTS_timings().remove(0);
							j--;

						} else if (output_packet_array.get(i).getEventArray().get(j).equals("MP2T") && (j == 0)) {
							MP2T_WITHDRAW_CUMULATIONS++;
							output_packet_array.get(i).getEventArray().remove(0);
							j--;
						}

						if (output_packet_array.get(i).getEventArray().isEmpty()) {

							break;
						}

					}
				}
			}

		}

		try {
			new Report().createReport(pcapfile_input.getFileName(), pcapfile_output.getFileName(), input_packet_size,
					output_packet_size, bufor.changeNanosToSeconds(delay), bufor.getSize(), bufor.getEmpty_count(),
					bufor.getNot_enough_data_count(), bufor.getGood_tries_count(),
					bufor.changeNanosToSeconds(bufor.getEmpty_time()));
			raportGenerateLabel.setText("Report succesfully created on PCAP_REPORTS!");
			raportGenerateLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
		} catch (Exception e) {
			raportGenerateLabel.setText("Nieoczekiwany b³¹d!");
			System.exit(0);
		}

		/*System.out.println("\nAFTER:");
		System.out.println("INPUT DATA: " + input_packet_array.size());
		System.out.println("OUTPUT DATA: " + output_packet_array.size());
		System.out.println("DTS COUNT: " + DTS_count);*/

		try {
			Desktop.getDesktop().open(new File("PCAP_REPORTS"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		inputFileButton.setDisable(true);
		outputFileButton.setDisable(true);
		checkDelayButton.setDisable(true);
		generateButton.setDisable(true);
		restartButton.setVisible(true);
	}

	@FXML
	void clearLabel(ActionEvent event) {
		delayInputLabel.setText("");
	}

	@FXML
	void restartAll(ActionEvent event) {
		generateButton.setDisable(true);
		inputFileLabel.setText("");
		outputFileLabel.setText("");
		inputFileButton.setDisable(false);
		outputFileButton.setDisable(false);
		checkDelayButton.setDisable(false);
		delayInput.setText("");
		delayInput.setDisable(false);
		delayInputLabel.setText("");
		raportGenerateLabel.setText("");
		input_time_array.clear();
		dts_time_array.clear();
		input_packet_size = 0;
		output_packet_size = 0;
		delay = 0;
		restartButton.setVisible(false);
		bufor.clearBufor();
	}

	@FXML
	void initialize() {

	}

}
