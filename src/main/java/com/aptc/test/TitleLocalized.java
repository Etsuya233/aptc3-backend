package com.aptc.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class TitleLocalized {
	@JsonProperty("en")
	public String sname;
}
