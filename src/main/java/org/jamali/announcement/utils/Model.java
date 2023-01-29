package org.jamali.announcement.utils;

public class Model {
	private String code;
	private String title;

	public Model(String row) {
		String[] model = row.split("\\|\\|");
		if (model.length == 2) {
			this.setCode(model[0]);
			this.setTitle(model[1]);
		}
	}

	public Model(String code, String title) {
		this.setCode(code);
		this.setTitle(title);

	}

	public Model() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public String getTitle() {
		return title;
	}

	public void setCode(String code) {
		this.code = code.replaceAll("\\n|\\r", " ");
	}

	public void setTitle(String title) {
		this.title = title.replaceAll("\\n|\\r", " ");
	}

	@Override
	public String toString() {
		return this.code + "||" + this.title;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Model other = (Model) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	
}
