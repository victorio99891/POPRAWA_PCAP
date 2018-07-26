package org.wiktor.PCAP_GUI.operations;

public class InputPacket {

	final int ETH_HDR_LEN = 14;
	final int IPv4_HDR_LEN = 20;
	final int TCP_HDR_LEN = 32;

	private long input_time;
	private int tcp_payload_data;

	public InputPacket(Long packet_time, int tcp_payload_data) {
		this.input_time = packet_time;
		this.tcp_payload_data = tcp_payload_data;
	}

	@Override
	public String toString() {
		return "InputPacket [packet_time=" + input_time + ", tcp_payload_data=" + tcp_payload_data + "]";
	}

	public long getInputTime() {
		return input_time;
	}

	public void setInputTime(long packet_time) {
		this.input_time = packet_time;
	}

	public int getTcp_payload_data() {
		return tcp_payload_data;
	}

	public void setTcp_payload_data(int tcp_payload_data) {
		this.tcp_payload_data = tcp_payload_data;
	}

}