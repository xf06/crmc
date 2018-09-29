package com.blackjade.crm.model;

public class FeesRow {

	private int pnsgid;
	private int pnsid;
	private char side;
	private String type;
	private long fixamt;
	private double fixrate;

	public int getPnsgid() {
		return pnsgid;
	}

	public void setPnsgid(int pnsgid) {
		this.pnsgid = pnsgid;
	}

	public int getPnsid() {
		return pnsid;
	}

	public void setPnsid(int pnsid) {
		this.pnsid = pnsid;
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = side;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getFixamt() {
		return fixamt;
	}

	public void setFixamt(long fixamt) {
		this.fixamt = fixamt;
	}

	public double getFixrate() {
		return fixrate;
	}

	public void setFixrate(double fixrate) {
		this.fixrate = fixrate;
	}

}
