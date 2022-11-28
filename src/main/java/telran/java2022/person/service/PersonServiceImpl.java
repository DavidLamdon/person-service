package telran.java2022.person.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepository;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.ChildDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.EmployeeDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.PersonNotFoundExeption;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Child;
import telran.java2022.person.model.Employee;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService, CommandLineRunner {
	final PersonRepository personRepository;
	final ModelMapper modelMapper;

	@Override
	@Transactional
	public Boolean addPerson(PersonDto personDto) {
		if(personRepository.existsById(personDto.getId())) {
			return false;
		}
		personRepository.save(modelMapper.map(personDto, getModelClass(personDto)));
		return true;
	}


	@Override
	public PersonDto findPersonById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto removePerson(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		personRepository.deleteById(id);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonName(Integer id, String name) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		person.setName(name);
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional
	public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
		Person person = personRepository.findById(id).orElseThrow(PersonNotFoundExeption::new);
		person.getAddress().setBuilding(addressDto.getBuilding());
		person.getAddress().setCity(addressDto.getCity());
		person.getAddress().setStreet(addressDto.getStreet());
		return modelMapper.map(person, PersonDto.class);
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsByCity(String city) {
		return personRepository.findByAddressCity(city)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersosnsByName(String name) {
		return personRepository.findByName(name)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
		LocalDate from = LocalDate.now().minusYears(maxAge);
		LocalDate to = LocalDate.now().minusYears(minAge);
		return personRepository.findByBirthDateBetween(from, to)
				.map(p -> modelMapper.map(p, PersonDto.class))
				.collect(Collectors.toList());
	}

	@Override
	@Transactional(readOnly = true)
	public Iterable<CityPopulationDto> getCitiesPopulation() {
//		Map<String, Long> population = StreamSupport.stream(personRepository.findAll().spliterator(), false)
//				.collect(Collectors.groupingBy(p -> p.getAddress().getCity(), Collectors.counting()));
//		return population.entrySet().stream()
//				.map(e -> new CityPopulationDto(e.getKey(), e.getValue()))
//				.collect(Collectors.toList());
		return personRepository.getCitiesPopulation();
	}

	@Override
	public void run(String... args) throws Exception {
		Person person = new Person(1000, "John", LocalDate.of(1985, 4, 11), new Address("Tel Aviv", "Ben Gvirol", 87));
		Child child = new Child(2000, "Moshe", LocalDate.of(2018, 7, 5), new Address("Ashkelon", "Bar Kohva", 21), "Shalom");
		Employee employee = new Employee(3000, "Sara", LocalDate.of(1995, 11, 23), new Address("Rehovot", "Herzl", 7),"Motorola", 20000);
		personRepository.save(person);
		personRepository.save(child);
		personRepository.save(employee);
	}
	
	private Class<? extends Person> getModelClass(PersonDto personDto) {
		if(personDto instanceof EmployeeDto) {
			return Employee.class;
		}
		if(personDto instanceof ChildDto) {
			return Child.class;
		}
		return Person.class;
		
	}


	@Override
	public Iterable<PersonDto> findEmployeeBySalary(int min, int max) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Iterable<PersonDto> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
}
