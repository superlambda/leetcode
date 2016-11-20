package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import com.wuerth.phoenix.util.PDate;

/**
 * 
 * @author pcnsh197
 *
 */
public class CASAPeriodBean {

	//Format YYYYMM
	private int		yearAndPeriod;

	private PDate	currentPeriodFromDate;

	private PDate	currentPeriodToDate;
	
	private PDate   onPeriodAgoToDate;

	// private CompanyYear previousYear;
	// private CompanyPeriod currentPeriodPreviousYear;

	private PDate	currentPeriodPreviousYearFromDate;

	private PDate	currentPeriodPreviousYearToDate;

	// Turnover 12 periods rolling
	private PDate	elevenPeriodAgoFromDate;

	private PDate	twelvePeriodAgoFromDate;
	private PDate	twelvePeriodAgoToDate;
	
	private PDate   thirteenPeriodAgoToDate;

	private PDate	twentythreePeriodAgoFromDate;
	
	private PDate  twentyfourPeriodAgoToDate;

	public int getYearAndPeriod() {
		return yearAndPeriod;
	}

	public void setYearAndPeriod(int yearAndPeriod) {
		this.yearAndPeriod = yearAndPeriod;
	}

	public PDate getCurrentPeriodFromDate() {
		return currentPeriodFromDate;
	}

	public void setCurrentPeriodFromDate(PDate currentPeriodFromDate) {
		this.currentPeriodFromDate = currentPeriodFromDate;
	}

	public PDate getCurrentPeriodToDate() {
		return currentPeriodToDate;
	}

	public void setCurrentPeriodToDate(PDate currentPeriodToDate) {
		this.currentPeriodToDate = currentPeriodToDate;
	}

	public PDate getOnPeriodAgoToDate() {
		return onPeriodAgoToDate;
	}

	public void setOnPeriodAgoToDate(PDate onPeriodAgoToDate) {
		this.onPeriodAgoToDate = onPeriodAgoToDate;
	}

	public PDate getCurrentPeriodPreviousYearFromDate() {
		return currentPeriodPreviousYearFromDate;
	}

	public void setCurrentPeriodPreviousYearFromDate(
			PDate currentPeriodPreviousYearFromDate) {
		this.currentPeriodPreviousYearFromDate = currentPeriodPreviousYearFromDate;
	}

	public PDate getCurrentPeriodPreviousYearToDate() {
		return currentPeriodPreviousYearToDate;
	}

	public void setCurrentPeriodPreviousYearToDate(
			PDate currentPeriodPreviousYearToDate) {
		this.currentPeriodPreviousYearToDate = currentPeriodPreviousYearToDate;
	}

	public PDate getElevenPeriodAgoFromDate() {
		return elevenPeriodAgoFromDate;
	}

	public void setElevenPeriodAgoFromDate(PDate elevenPeriodAgoFromDate) {
		this.elevenPeriodAgoFromDate = elevenPeriodAgoFromDate;
	}
	
	public PDate getTwelvePeriodAgoFromDate() {
		return twelvePeriodAgoFromDate;
	}

	public void setTwelvePeriodAgoFromDate(PDate twelvePeriodAgoFromDate) {
		this.twelvePeriodAgoFromDate = twelvePeriodAgoFromDate;
	}

	public PDate getTwelvePeriodAgoToDate() {
		return twelvePeriodAgoToDate;
	}

	public void setTwelvePeriodAgoToDate(PDate twelvePeriodAgoToDate) {
		this.twelvePeriodAgoToDate = twelvePeriodAgoToDate;
	}

	public PDate getTwentythreePeriodAgoFromDate() {
		return twentythreePeriodAgoFromDate;
	}

	public void setTwentythreePeriodAgoFromDate(PDate twentythreePeriodAgoFromDate) {
		this.twentythreePeriodAgoFromDate = twentythreePeriodAgoFromDate;
	}

	public PDate getThirteenPeriodAgoToDate() {
		return thirteenPeriodAgoToDate;
	}

	public void setThirteenPeriodAgoToDate(PDate thirteenPeriodAgoToDate) {
		this.thirteenPeriodAgoToDate = thirteenPeriodAgoToDate;
	}

	public PDate getTwentyfourPeriodAgoToDate() {
		return twentyfourPeriodAgoToDate;
	}

	public void setTwentyfourPeriodAgoToDate(PDate twentyfourPeriodAgoToDate) {
		this.twentyfourPeriodAgoToDate = twentyfourPeriodAgoToDate;
	}
	
	
	
}
