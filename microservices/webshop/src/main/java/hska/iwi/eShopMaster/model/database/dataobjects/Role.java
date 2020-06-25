package hska.iwi.eShopMaster.model.database.dataobjects;


import javax.persistence.*;
import java.util.UUID;

public class Role implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private UUID id;
	private String typ;
	private int level;

	public Role() {
	}

	public Role(String typ, int level) {
		this.typ = typ;
		this.level = level;
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTyp() {
		return this.typ;
	}

	public void setTyp(String typ) {
		this.typ = typ;
	}

	public int getLevel() {
		return this.level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

}
