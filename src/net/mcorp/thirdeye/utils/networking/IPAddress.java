package net.mcorp.thirdeye.utils.networking;

import java.text.ParseException;

public class IPAddress {
	
	private int[] segments = new int[4];
	public int[] segments() { return this.segments; };
	
	public String get() {
		String res = ""; 
		for(int seg : segments) { 
			res += seg+".";
		}
		return res.substring(0,res.length()-1);
	}
	
	public IPAddress(String address) throws ParseException {
		String[] segs = address.split(".");
		if(segs.length != 4)
			throw new ParseException("Unable to parse ip address as it contains more or less than 4 segments.", 0);
		int[] segs_ = new int[4];
		for(int i = 0; i < segs_.length; i++) {
			segs_[i] = Integer.parseInt(segs[i],10);
			if(segs_[i] > 255 || segs_[i] < 0)
				throw new ParseException("Unable to parse ip address as a segment is either greater than 255 or less than 0. Segment: #"+i,i);
		}
		this.segments = segs_;
	}
	
	public IPAddress(int seg1, int seg2, int seg3, int seg4) {
		this.segments = new int[] {seg1, seg2, seg3, seg4};
		for(int temp : this.segments) {
			if(temp > 255 || temp < 0)
				throw new RuntimeException("Unable to parse ip address as a segment is either greater than 255 or less than 0. Segment Value: "+temp);
		}
	}
	
	public String toString() {
		return this.get();
	}
	
}
