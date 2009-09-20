package br.project.template.business;

import java.io.Serializable;

import javax.ejb.Local;

import br.project.template.entity.Person;

@Local
public interface IPersonBusiness extends Serializable {

	public abstract String add(Person person);

}