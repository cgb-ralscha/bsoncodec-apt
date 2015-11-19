package ch.rasc.bsoncodec.test.embedded;

import java.util.Date;

import ch.rasc.bsoncodec.annotation.BsonDocument;

@BsonDocument
public class Phase {
	private Date date;
	private String person;

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getPerson() {
		return this.person;
	}

	public void setPerson(String person) {
		this.person = person;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (this.date == null ? 0 : this.date.hashCode());
		result = prime * result + (this.person == null ? 0 : this.person.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Phase other = (Phase) obj;
		if (this.date == null) {
			if (other.date != null) {
				return false;
			}
		}
		else if (!this.date.equals(other.date)) {
			return false;
		}
		if (this.person == null) {
			if (other.person != null) {
				return false;
			}
		}
		else if (!this.person.equals(other.person)) {
			return false;
		}
		return true;
	}

}
