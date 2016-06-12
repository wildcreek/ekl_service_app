package com.leyikao.onlinelearn.serviceapp.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.StringUtils;

public class SimilarityDegree {
	public static boolean isSome(String target, String compareItem, double sentinel){
		target = formatString(target);
		compareItem = formatString(compareItem);
		
		int someCount = statSomeCount(target, compareItem);
		Double someDegree = 0d;
		if (someCount > 0){
			Double targetSomeDegree = someCount / (target.length() + 0d);
			Double compareItemSomeDegree = someCount / (compareItem.length() + 0d);
			someDegree = Math.min(targetSomeDegree, compareItemSomeDegree);
		}
		
		return someDegree > sentinel;
	}
	
	
	public static Integer statSomeCount(String target, String compareItem) {
		
		AtomicInteger someCount = new AtomicInteger(0);
		
		if (StringUtils.isEmpty(target) || StringUtils.isEmpty(compareItem)){
			return someCount.get();
		}
		
		target.chars().forEach(aChar -> {
			if (compareItem.indexOf(aChar) != -1){
				someCount.incrementAndGet();
			}
		});
		
		return someCount.get();
	}
	
	public static String formatString(String description){
		if (description == null){
			return description;
		}
		
		description = description.replaceAll("\\s+", "")
				.replaceAll("&nbsp;", "").replaceAll("</p>", "").replaceAll("<br/>", "")
				.replaceAll("<p>", "").replaceAll("<br>", "");
		do {
			int startIndex = description.indexOf("<img");
			if (startIndex > 0){
				int endIndex = description.indexOf("/>", startIndex);
				if (endIndex > 0){
					description = description.replaceAll(description.substring(startIndex, endIndex + 2), "");
				}
			}
			
		} while (description.indexOf("<img") > 0);
		
		do {
			int startIndex = description.indexOf("<span");
			if (startIndex > 0){
				int endIndex = description.indexOf("</span>", startIndex);
				if (endIndex > 0){
					description = description.substring(0, startIndex) + description.substring(endIndex + 7, description.length());
				}
			}
			
		} while (description.indexOf("<span") > 0);
		
		return description;
	}

}
