package net.javajs.bdi.collection;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document("record")
public class Record {
	private ObjectId _id;
	private String date;
	private String homeName;
	private String awayName;
	private Integer homeScore;
	private Integer awayScore;
	private Boolean isTransfer;
}
