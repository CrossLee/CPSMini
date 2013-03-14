package com.withiter.dto;

import java.util.Map;

public class ProgramDTO {

	private int areas;
	private Map<Integer, OnebyoneDTO> onebyones;
	
	public ProgramDTO() {
		super();
	}

	public ProgramDTO(int areas, Map<Integer, OnebyoneDTO> onebyones) {
		super();
		this.areas = areas;
		this.onebyones = onebyones;
	}

	public int getAreas() {
		return areas;
	}
	public void setAreas(int areas) {
		this.areas = areas;
	}
	public Map<Integer, OnebyoneDTO> getOnebyones() {
		return onebyones;
	}
	public void setOnebyones(Map<Integer, OnebyoneDTO> onebyones) {
		this.onebyones = onebyones;
	}
	
}
