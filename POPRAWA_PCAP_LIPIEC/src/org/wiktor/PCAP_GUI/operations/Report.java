package org.wiktor.PCAP_GUI.operations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Report {

	public String correctString(int patternLength, String initialString) {
		String final_string = "| " + initialString;
		if (final_string.length() < patternLength) {
			int difference = (patternLength - final_string.length());
			for (int i = 0; i < difference; i++) {
				final_string += " ";
			}
			final_string += " ";
		}
		if (final_string.length() >= patternLength) {
			final_string += "|";
		}
		return final_string;
	}

	public void createReport(String inputFileName, String outputFileName, int inputPacketSize, int outputPacketSize, String delay,  int bufferSize, int empty, int notEnough, int good, String emptyTime)
			throws IOException {
		String report_name = "PCAPReport_";
		DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String date_file_name = dtf1.format(now);
		String date_report = dtf2.format(now);
		report_name += date_file_name;
		File dir = new File("PCAP_REPORTS");
		dir.mkdir();
		File file = new File("PCAP_REPORTS/" + report_name + ".txt");
		FileWriter wf = new FileWriter(file);

		String pattern = "--------------------------------------------------------------------------";
		int pt_len = pattern.length();

		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write("|                        >>> SUMMARY REPORT <<<                            |");
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write("|                           - GENERAL INFO -                               |");
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Report Generation Time:          " + date_report));
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Input file name:                 " + inputFileName));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Output file name:                " + outputFileName));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, " "));
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Input PCAP Packets Size:         " + inputPacketSize + " KB"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Output PCAP Packets Size:        " + outputPacketSize + " KB"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, " "));
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write("|                           - BUFFER STATS -                               |");
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Delay time (defined by user):    " + delay + " s"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "End size:                        " + bufferSize + "KB"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Empty:                           " + empty + " times"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Not enough data:                 " + notEnough + " times"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Succesful withdraw:              " + good + " times"));
		wf.write(System.lineSeparator());
		int all_size = empty + notEnough + good;
		wf.write(correctString(pt_len, "Summary (withdraw):              " + all_size + " times"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, "Time of empty:                   " + emptyTime + " s"));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, " "));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, " "));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, " "));
		wf.write(System.lineSeparator());
		wf.write(correctString(pt_len, " "));
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.write(System.lineSeparator());
		wf.write("| Report Created with >PCAPStatManager< developed by Wiktor Krzy¿anowski   |");
		wf.write(System.lineSeparator());
		wf.write("|--------------------------------------------------------------------------|");
		wf.flush();
		wf.close();
		
		file.setReadOnly();

	}

}
