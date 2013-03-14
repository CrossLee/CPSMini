package com.withiter.dto;

public class OnebyoneDTO {

	private ResolutionDTO dimension;
	private String resource;
	private String direction;
	private String type;
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ResolutionDTO getDimension() {
		return dimension;
	}

	public void setDimension(ResolutionDTO dimension) {
		this.dimension = dimension;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public OnebyoneDTO(ResolutionDTO dimension, String resource, String direction) {
		super();
		this.dimension = dimension;
		this.resource = resource;
		this.direction = direction;
	}

	public OnebyoneDTO() {
		super();
	}
}
