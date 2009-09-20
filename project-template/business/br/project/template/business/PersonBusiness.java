package br.project.template.business;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Out;

import br.project.template.entity.Person;

@SuppressWarnings("serial")
@Stateless
@Name("personBusiness")
public class PersonBusiness implements IPersonBusiness {

	@In(value="person")
	private Person ok;
	
	@PersistenceContext
	private EntityManager em;

	public String add(Person person) {
		em.persist(person);
		return "home.xhtml";
	}

}