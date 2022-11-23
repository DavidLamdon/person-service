package telran.java2022.person.dao;

import java.util.stream.Stream;

import org.springframework.data.repository.CrudRepository;

import telran.java2022.person.model.Person;


public interface PersonRepository extends CrudRepository<Person, Integer> {
	//Stream<Person> findByAddressCityIgnoreCase(String city);
}
