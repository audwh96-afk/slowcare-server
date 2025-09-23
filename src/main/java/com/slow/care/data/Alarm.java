package com.slow.care.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
	private String id;
	private String name;
	private String rate;
	private String gender;
	private String address;
	private String birth;
	private String phone;
	private String avatarUrl;
	private String link;
}
