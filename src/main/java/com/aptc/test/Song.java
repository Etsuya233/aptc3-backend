package com.aptc.test;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Song {
	@JsonProperty("id")
	public String gid;
	@JsonProperty("title_localized")
	public TitleLocalized titleLocalized;

	public String sname;

}
