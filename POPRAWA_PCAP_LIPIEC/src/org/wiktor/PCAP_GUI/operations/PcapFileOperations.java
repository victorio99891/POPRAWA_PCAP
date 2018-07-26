package org.wiktor.PCAP_GUI.operations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PcapFileOperations {

	private String fileAdress = "";
	private String fileName = "";

	public PcapFileOperations() {

	}

	/* OPEN FILE EXPLORER*/
	public String openFileChooserDialog(String message) {
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TXT FILES (*.txt)", "txt");
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileFilter(filter);
		fc.setDialogTitle(message);
		fc.setCurrentDirectory(new File("./TXT_FILES"));
		fc.showOpenDialog(fc);
		String path = fc.getSelectedFile().getAbsolutePath();
		this.setFileName(fc.getSelectedFile().getName());

		return path;
	}

	/*READ TXT FILES AND MAKE TXTARRAY*/
	public ArrayList<InputPacket> readOfflineFiles_INPUT() {
		ArrayList<InputPacket> temporary_list = new ArrayList<InputPacket>();
		File input_file = new File(this.fileAdress);

		int lines = 0;
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(input_file));

			while (br.readLine() != null) {
				lines++;
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br = new BufferedReader(new FileReader(input_file));

			Long inputTime = 0L;
			int packet_payload = 0;

			int frame_number = 2;

			for (int i = 0; i < lines; i++) {

				String line = br.readLine();

				if (line.contains("[Time since reference or first frame:")) {

					System.out.println("Frame " + (frame_number - 1) + ":");

					String numbers = null;
					numbers = line.replaceAll("[^0-9]", "");

					inputTime = Long.valueOf(numbers);

					System.out.println("Input time: " + inputTime);

				}

				if (line.contains("[TCP Segment Len:")) {
					String numbers = null;
					numbers = line.replaceAll("[^0-9]", "");
					packet_payload = Integer.valueOf(numbers);
				}

				if (line.contains("Frame " + frame_number + ":") || i == (lines - 1)) {
					InputPacket current_packet = new InputPacket(inputTime, packet_payload);
					System.out.println(current_packet.toString());
					temporary_list.add(current_packet);
					inputTime = 0L;
					packet_payload = 0;
					frame_number++;
					System.out.println("\n===========================================================================================================================================\n");
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Ile pakietów: " + temporary_list.size());

		return temporary_list;
	}

	public ArrayList<OutputPacket> readOfflineFiles_OUTPUT() {
		ArrayList<OutputPacket> temporary_list = new ArrayList<OutputPacket>();
		File input_file = new File(this.fileAdress);

		int lines = 0;
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(input_file));

			while (br.readLine() != null) {
				lines++;
			}

			br.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			br = new BufferedReader(new FileReader(input_file));

			Long inputTime = 0L;
			ArrayList<String> eventArray = new ArrayList<String>();
			ArrayList<Long> pes_timings = new ArrayList<Long>();

			int frame_number = 2;

			for (int i = 0; i < lines; i++) {

				String line = br.readLine();

				if (line.contains("[Time since reference or first frame:")) {

					System.out.println("Frame " + (frame_number - 1) + ":");

					String numbers = null;
					numbers = line.replaceAll("[^0-9]", "");

					inputTime = Long.valueOf(numbers);

					System.out.println("Input time: " + inputTime);

				}

				if (line.contains("decode time stamp (DTS):")) {
					String numbers = null;
					numbers = line.replaceAll("[^0-9]", "");

					Long timing = Long.valueOf(numbers);

					eventArray.add("(DTS)");
					pes_timings.add(timing);

					System.out.println("(DTS) PES time: " + timing);
				}

				if (line.contains("ISO/IEC 13818-1")) {
					eventArray.add("MP2T");
					//System.out.println("MP2T");
				}

				if (line.contains("Frame " + frame_number + ":") || i == (lines - 1)) {

					@SuppressWarnings("unchecked")
					OutputPacket current_packet = new OutputPacket(inputTime, (ArrayList<String>) eventArray.clone(),
							(ArrayList<Long>) pes_timings.clone());
					System.out.println(current_packet.toString());

					temporary_list.add(current_packet);

					inputTime = 0L;
					eventArray.clear();
					pes_timings.clear();

					frame_number++;
					System.out.println("\n===========================================================================================================================================\n");
				}

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Ile pakietów: " + temporary_list.size());

		return temporary_list;
	}

	public ArrayList<Long> make_timings_array_in(ArrayList<InputPacket> input_packet_array) {
		ArrayList<Long> temporary = new ArrayList<Long>();
		for (int i = 0; i < input_packet_array.size(); i++) {
			temporary.add(input_packet_array.get(i).getInputTime());
		}
		return temporary;
	}

	public ArrayList<Long> make_timings_array_out(ArrayList<OutputPacket> output_packet_array) {
		ArrayList<Long> temporary = new ArrayList<Long>();
		for (int i = 0; i < output_packet_array.size(); i++) {
			temporary.add(output_packet_array.get(i).getInputTime());
		}
		return temporary;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileAdress() {
		return fileAdress;
	}

	public void setFileAdress(String fileAdress) {
		this.fileAdress = fileAdress;
	}
}
