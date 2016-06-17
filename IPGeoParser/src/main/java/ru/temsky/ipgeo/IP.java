package ru.temsky.ipgeo;

public class IP {

	private String address = "";
	private String country = "";
	private String city = "";
	private String provider = "";
	private String subnet = "";
	private String cidr = "";

	public IP() {
	}

	public IP(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getSubnet() {
		return subnet;
	}

	public void setSubnet(String subnet) {
		this.subnet = subnet;
	}

	public String getCidr() {
		return cidr;
	}

	public void setCidr(String cidr) {
		this.cidr = cidr;
	}

	@Override
	public String toString() {
		return "Ip [address=" + address + ", country=" + country + ", city=" + city + ", provider=" + provider + "]";
	}
}
