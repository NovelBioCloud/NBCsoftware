package com.novelbio.report.Params;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

//TODO 不简洁
public enum EnumTaskClass {
	
	RowData("Raw Data Treatment:"),
	Preliminary("Preliminary Analysis:"),
	InDepth("In-Depth Analysis:");
	
	String title = null;
	EnumTaskClass(String title) {
		this.title = title;
	}
	
	public static LinkedHashMap<EnumTaskClass, List<String>> getMapClass2LsTaskId() {
		LinkedHashMap<EnumTaskClass, List<String>> mapTaskClass2lsTaskId = new LinkedHashMap<EnumTaskClass, List<String>>();
		EnumTaskClass[] enumTaskClasses = values();
		for (EnumTaskClass enumTaskClass : enumTaskClasses) {
			mapTaskClass2lsTaskId.put(enumTaskClass, new ArrayList<String>());
		}
		return mapTaskClass2lsTaskId;
	}
	
	public String getTitle() {
		return title;
	}
	
	@Override
	public String toString() {
		return title;
	}


}
