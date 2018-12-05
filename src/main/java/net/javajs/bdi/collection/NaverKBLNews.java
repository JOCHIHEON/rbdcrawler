package net.javajs.bdi.collection;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("naverkblnews")
public class NaverKBLNews {
	private String uri;
	private String aid;
	private String oid; //신문사 코드
	private String title;
	private String text;
	private String company;
	private String date;
}
