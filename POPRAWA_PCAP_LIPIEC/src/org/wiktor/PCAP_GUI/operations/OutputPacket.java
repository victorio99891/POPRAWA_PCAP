package org.wiktor.PCAP_GUI.operations;

import java.util.ArrayList;

public class OutputPacket {

	/* HEADERS */
	final int ETH_HDR_LEN = 14;
	final int IPv4_HDR_LEN = 20;
	final int UDP_HDR_LEN = 8;
	final int RTP_HDR_LEN = 12;

	/* MP2T SIZE - whole packet size = 7 * 188 = 1316 bytes */
	final int MP2T_SIZE = 188;
	final int DATA_SIZE = 1316;

	private Long inputTime;
	private ArrayList<String> eventArray = new ArrayList<String>();
	private ArrayList<Long> dts_timings = new ArrayList<Long>();

	public OutputPacket() {

	}

	public OutputPacket(Long inputTime, ArrayList<String> eventArray, ArrayList<Long> pes_timings) {
		this.inputTime = inputTime;
		this.eventArray = eventArray;
		this.dts_timings = pes_timings;
	}

	@Override
	public String toString() {
		return "OutputPacket [inputTime=" + inputTime + ", eventArray=" + eventArray + ", dts_timings=" + dts_timings
				+ "]";
	}

	public int getDATA_SIZE() {
		return DATA_SIZE;
	}

	public Long getInputTime() {
		return inputTime;
	}

	public void setInputTime(Long inputTime) {
		this.inputTime = inputTime;
	}

	public ArrayList<String> getEventArray() {
		return eventArray;
	}

	public void setEventArray(ArrayList<String> eventArray) {
		this.eventArray = eventArray;
	}

	public ArrayList<Long> getDTS_timings() {
		return dts_timings;
	}

	public void setDTS_timings(ArrayList<Long> pes_timings) {
		this.dts_timings = pes_timings;
	}

}
