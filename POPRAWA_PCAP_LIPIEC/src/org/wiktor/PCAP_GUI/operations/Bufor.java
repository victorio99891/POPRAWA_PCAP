package org.wiktor.PCAP_GUI.operations;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class Bufor {

	private int size;
	private int empty_count;
	private int not_enough_data_count;
	private int good_tries_count;
	private long empty_time;
	private ArrayList<Long> empty_time_buffer_timings = new ArrayList<Long>();

	private int temporary_buffer_size_flag;

	public void clearBufor() {
		this.size = 0;
		this.empty_count = 0;
		this.not_enough_data_count = 0;
		this.good_tries_count = 0;
		this.empty_time = 0;
		this.empty_time_buffer_timings.clear();
	}

	public void saveFileReport(int input_packet_size, int output_packet_size, long delay) {

		System.out.println("--------------------SUMMIT OF PROGRAM ACTION------------------");

		// System.out.println("Testowa:\n" + control_array + "\nTestowa Size: " +
		// control_array.size());

		System.out.println("Full size input: " + input_packet_size);
		System.out.println("Full size output: " + output_packet_size);
		System.out.println("Buffer size: " + this.getSize());
		System.out.println("Empty count: " + this.getEmpty_count());
		System.out.println("Not enough data count: " + this.getNot_enough_data_count());
		System.out.println("Good tries data count: " + this.getGood_tries_count());
		int all_counts = this.getEmpty_count() + this.getNot_enough_data_count() + this.getGood_tries_count();
		System.out.println("Allcounts: " + all_counts);
		System.out.println("Time of empty buffer: " + this.changeNanosToSeconds(this.getEmpty_time()) + " s");
		System.out.println("Czas opóznienia: " + delay + "ns = " + this.changeNanosToSeconds(delay) + "s");
	}

	/*BUFOR CONSTRUCTOR*/
	public Bufor(int init_size) {
		this.size = init_size;
	}

	/*SECONDS TO NANOS*/
	public long changeSecondsToNanos(double seconds) {
		return (long) (seconds * 1000000000);
	}

	/*NANOS TO SECONDS*/
	public String changeNanosToSeconds(long nanos) {
		DecimalFormat df = new DecimalFormat("#.######");
		df.setRoundingMode(RoundingMode.FLOOR);
		double seconds = (double) nanos / 1000000000;
		return df.format(seconds);
	}

	/*CALCULATE TEMPORARY EMPTY TIME*/
	public void checkBufferStatus(long element) {
		long time_difference = 0;
		if (this.getSize() == 0) {
			empty_time_buffer_timings.add(element);
		} else if (this.getSize() != 0 && !empty_time_buffer_timings.isEmpty()) {
			time_difference = empty_time_buffer_timings.get(empty_time_buffer_timings.size() - 1).longValue()
					- empty_time_buffer_timings.get(0).longValue();
			this.setEmpty_time(this.getEmpty_time() + time_difference);
			empty_time_buffer_timings.removeAll(empty_time_buffer_timings);
		}

	}

	/*CALCULATE TEMPORARY EMPTY TIME AFTER ONE OF LIST END*/
	public void POST_checkBufferStatus(long element, int iterator, ArrayList<Long> list) {
		long time_difference = 0;

		empty_time_buffer_timings.add(element);

		if (iterator == list.size() - 1) {
			time_difference = empty_time_buffer_timings.get(empty_time_buffer_timings.size() - 1).longValue()
					- empty_time_buffer_timings.get(0).longValue();
			this.setEmpty_time(this.getEmpty_time() + time_difference);
			empty_time_buffer_timings.removeAll(empty_time_buffer_timings);
		}

	}

	public void addData(int packetLength) {
		this.setSize(this.getSize() + packetLength);
	}

	public void subtractData(int packetLength) {
		if (this.getSize() > 0) {
			this.setSize(this.getSize() - packetLength);
			if (this.getSize() < 0) {
				this.setSize(0);
				this.setNot_enough_data_count(this.getNot_enough_data_count() + 1);
			} else if (this.getSize() > 0) {
				this.setGood_tries_count(this.getGood_tries_count() + 1);
			}
		} else if (this.getSize() == 0) {
			this.setEmpty_count(this.getEmpty_count() + 1);
		}
	}

	public ArrayList<Long> getCurrent_list() {
		return empty_time_buffer_timings;
	}

	public void setCurrent_list(ArrayList<Long> current_list) {
		this.empty_time_buffer_timings = current_list;
	}

	public int getEmpty_count() {
		return empty_count;
	}

	public void setEmpty_count(int empty_count) {
		this.empty_count = empty_count;
	}

	public int getNot_enough_data_count() {
		return not_enough_data_count;
	}

	public void setNot_enough_data_count(int not_enough_data_count) {
		this.not_enough_data_count = not_enough_data_count;
	}

	public int getGood_tries_count() {
		return good_tries_count;
	}

	public void setGood_tries_count(int good_tries_count) {
		this.good_tries_count = good_tries_count;
	}

	public long getEmpty_time() {
		return empty_time;
	}

	public void setEmpty_time(long empty_time) {
		this.empty_time = empty_time;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getTemporary_buffer_size_flag() {
		return temporary_buffer_size_flag;
	}

	public void setTemporary_buffer_size_flag(int temporary_buffer_size_flag) {
		this.temporary_buffer_size_flag = temporary_buffer_size_flag;
	}

}
