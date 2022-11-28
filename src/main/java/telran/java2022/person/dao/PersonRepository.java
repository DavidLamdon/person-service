package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.model.Employee;
import telran.java2022.person.model.Person;


public interface PersonRepository extends CrudRepository<Person, Integer> {
	//Stream<Person> findByAddressCityIgnoreCase(String city);
	@Query("select p from Person p  where p.name=?1")
	Stream<Person> findByName(String name);
	
	@Query("select p from Person p where p.address.city=:city")
	Stream<Person> findByAddressCity(@Param("city") String city);

	
	Stream<Person> findByBirthDateBetween(LocalDate from, LocalDate to); //соглашение об именовании методов
	
	@Query("select new telran.java2022.person.dto.CityPopulationDto(p.address.city, count(p)) from Person p group by p.address.city order by count(p) desc")
	List<CityPopulationDto> getCitiesPopulation();
	
	@Query("select e from Employee e join Person p on e.id=p.id")
	Stream<Employee> findAllEmployeeBySalary(@Param("min")int min, @Param("max") int max);
	
	@Query("select c from Child c join Person p on c.id=p.id")
	Stream<Person> findAllChild();
}
