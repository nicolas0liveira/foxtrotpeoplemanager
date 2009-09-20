package br.project.template.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.validator.Email;
import org.hibernate.validator.NotNull;
import org.jboss.seam.annotations.Name;

@SuppressWarnings("serial")
@Entity
@Name("person")
public class Person implements Serializable {

	@Id
	@GeneratedValue
	private long id;

	@NotNull(message="sdsdsdsdsdsd")
	private String name;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Email(message="sodads")
	private String email;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getId() {
		return id;
	}

}