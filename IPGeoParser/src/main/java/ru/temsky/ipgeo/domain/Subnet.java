package ru.temsky.ipgeo.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

@Entity
@TypeDef(name = "inet", typeClass = InetType.class)
public class Subnet implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@Column(columnDefinition = "inet")
	@Type(type = "inet")
	private String name;
	private String country;
	private String city;
	private String provider;

	protected Subnet() {
	}

	public Subnet(String name, String country, String city, String provider) {
		this.name = name;
		this.country = country;
		this.city = city;
		this.provider = provider;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	@Override
	public String toString() {
		return "Subnet [name=" + name + ", country=" + country + ", city=" + city + ", provider=" + provider + "]";
	}

}
