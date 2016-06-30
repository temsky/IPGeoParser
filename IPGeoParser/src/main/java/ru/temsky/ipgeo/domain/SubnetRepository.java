package ru.temsky.ipgeo.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubnetRepository extends CrudRepository<Subnet, String> {
	@Query(value = "select * from subnet  where name >> inet(?) limit 1", nativeQuery = true)
	Subnet findSubnet(String ip);
}
