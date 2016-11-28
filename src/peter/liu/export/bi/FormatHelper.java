package com.wuerth.phoenix.basic.etnax.utilities.sap.export.bi;

import java.text.DecimalFormat;

/**
 * Love you NZ
 * @author liuyingjie
 *
 */
public class FormatHelper {
   
	private static DecimalFormat weightFormat =null;
	private static DecimalFormat priceFormat =null;
	
	private FormatHelper(){
		
	}
	public static DecimalFormat getWeightFormat(){
		if(weightFormat==null){
			weightFormat=new DecimalFormat();
			weightFormat.setMaximumFractionDigits(3);
			weightFormat.setMinimumFractionDigits(3);
			weightFormat.setGroupingUsed(false);
		}
		return weightFormat;
	}
	
	public static DecimalFormat getPriceFormat(){
		if(priceFormat==null){
			priceFormat=new DecimalFormat();
			priceFormat.setMaximumFractionDigits(2);
			priceFormat.setMinimumFractionDigits(2);
			priceFormat.setGroupingUsed(false);
		}
		return priceFormat;
	}
			
}
