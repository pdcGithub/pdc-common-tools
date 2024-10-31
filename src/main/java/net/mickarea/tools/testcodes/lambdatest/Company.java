/******************************************************************************************************

This file "Company.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

/**
 * >> 一个测试用的组织类
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月18日-2023年4月19日
 */
public class Company {

	private String id;
	private String name;
	private String tel;
	private Address address;
	
	/**
	 * >> 构造函数
	 */
	public Company() {
	}
	public Company(String id, String name, String tel, String address) {
		this.id = id;
		this.name = name;
		this.tel = tel;
		this.address = new Address(address);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Address getAddress() {
		return address;
	}
	public void setAddress(Address address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "ID:"+this.id+", NAME:"+this.name+", TEL:"+this.tel+", ADDR:"+this.address;
	}
}

class Address {
	private String country = "";
	private String province = "";
	private String city = "";
	private String county = "";
	private String town = "";
	private String others = "";
	/**
	 * >> 构造函数
	 */
	public Address() {
	}
	public Address(String fullAddress) {
		//全地址解析
		if(fullAddress!=null) {
			int start = 0;
			int countryIndex = fullAddress.indexOf("国", start);
			String country = countryIndex<0?"":fullAddress.substring(start, countryIndex+1);
			start = countryIndex<0?start:countryIndex+1;
			
			int provinceIndex = fullAddress.indexOf("省", start);
			String province = provinceIndex<0?"":fullAddress.substring(start, provinceIndex+1);
			start = provinceIndex<0?start:provinceIndex+1;
			
			int cityIndex = fullAddress.indexOf("市", start);
			String city = cityIndex<0?"":fullAddress.substring(start, cityIndex+1);
			start = cityIndex<0?start:cityIndex+1;
			
			int countyIndex = fullAddress.indexOf("区", start)<0?fullAddress.indexOf("县", start):fullAddress.indexOf("区", start);
			String county = countyIndex<0?"":fullAddress.substring(start, countyIndex+1);
			start = countyIndex<0?start:countyIndex+1;
			
			String others = fullAddress.substring(start);
			
			this.country = country;
			this.province = province;
			this.city = city;
			this.county = county;
			this.others = others;
		}
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getTown() {
		return town;
	}
	public void setTown(String town) {
		this.town = town;
	}
	public String getOthers() {
		return others;
	}
	public void setOthers(String others) {
		this.others = others;
	}
	@Override
	public String toString() {
		return this.country+"||"+this.province+"||"+this.city+"||"+this.county+"||"+this.town+"||"+this.others;
	}
}