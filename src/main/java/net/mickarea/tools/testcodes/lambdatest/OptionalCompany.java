/******************************************************************************************************

This file "OptionalCompany.java" is part of project "pdc-common-tools" , which is belong to Michael Pang (It's Me).
In my license, all codes can be shared free of charge. 
However, if it is used for commercial purposes, I need to be notified.
Here is my email "pangdongcan@live.com"

Copyright (c) 2023 Michael Pang.

*******************************************************************************************************/
package net.mickarea.tools.testcodes.lambdatest;

import java.util.Optional;

import net.mickarea.tools.utils.Stdout;

/**
 * >> 一个测试用的组织类 传回 Optional 类型的结果
 * @author Michael Pang (Dongcan Pang)
 * @version 1.0
 * @since 2023年4月18日-2023年4月19日
 */
public class OptionalCompany {

	private String id;
	private String name;
	private String tel;
	private OptionalAddress address;
	
	/**
	 * >> 构造函数
	 */
	public OptionalCompany() {
	}
	public OptionalCompany(String id, String name, String tel, String address) {
		this.id = id;
		this.name = name;
		this.tel = tel;
		this.address = new OptionalAddress(address);
	}
	public Optional<String> getId() {
		return Optional.ofNullable(id);
	}
	public void setId(String id) {
		this.id = id;
	}
	public Optional<String> getName() {
		return Optional.ofNullable(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public Optional<String> getTel() {
		return Optional.ofNullable(tel);
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public Optional<OptionalAddress> getAddress() {
		return Optional.ofNullable(address);
	}
	public void setAddress(OptionalAddress address) {
		this.address = address;
	}
	@Override
	public String toString() {
		
		return Stdout.fplToAnyWhere("ID:%s, NAME:%s, TEL:%s, ADDR:%s", this.getId().orElse(""),
				                                                       this.getName().orElse(""),
				                                                       this.getTel().orElse(""),
				                                                       this.getAddress().orElse(new OptionalAddress()));
	}
}

class OptionalAddress {
	private String country;
	private String province;
	private String city;
	private String county;
	private String town;
	private String others;
	/**
	 * >> 构造函数
	 */
	public OptionalAddress() {
	}
	public OptionalAddress(String fullAddress) {
		//全地址解析
		if(fullAddress!=null) {
			int start = 0;
			int countryIndex = fullAddress.indexOf("国", start);
			String country = countryIndex<0?null:fullAddress.substring(start, countryIndex+1);
			start = countryIndex<0?start:countryIndex+1;
			
			int provinceIndex = fullAddress.indexOf("省", start);
			String province = provinceIndex<0?null:fullAddress.substring(start, provinceIndex+1);
			start = provinceIndex<0?start:provinceIndex+1;
			
			int cityIndex = fullAddress.indexOf("市", start);
			String city = cityIndex<0?null:fullAddress.substring(start, cityIndex+1);
			start = cityIndex<0?start:cityIndex+1;
			
			int countyIndex = fullAddress.indexOf("区", start)<0?fullAddress.indexOf("县", start):fullAddress.indexOf("区", start);
			String county = countyIndex<0?null:fullAddress.substring(start, countyIndex+1);
			start = countyIndex<0?start:countyIndex+1;
			
			String others = fullAddress.substring(start);
			
			this.country = country;
			this.province = province;
			this.city = city;
			this.county = county;
			this.others = others;
		}
	}
	public Optional<String> getCountry() {
		return Optional.ofNullable(this.country);
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Optional<String> getProvince() {
		return Optional.ofNullable(this.province);
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public Optional<String> getCity() {
		return Optional.ofNullable(this.city);
	}
	public void setCity(String city) {
		this.city = city;
	}
	public Optional<String> getCounty() {
		return Optional.ofNullable(this.county);
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public Optional<String> getTown() {
		return Optional.ofNullable(this.town);
	}
	public void setTown(String town) {
		this.town = town;
	}
	public Optional<String> getOthers() {
		return Optional.ofNullable(this.others);
	}
	public void setOthers(String others) {
		this.others = others;
	}
	@Override
	public String toString() {
		//return this.country+"||"+this.province+"||"+this.city+"||"+this.county+"||"+this.town+"||"+this.others;
		return Stdout.fplToAnyWhere("%s||%s||%s||%s||%s||%s", this.getCountry().orElse(""),
				                                              this.getProvince().orElse(""),
				                                              this.getCity().orElse(""),
				                                              this.getCounty().orElse(""),
				                                              this.getTown().orElse(""),
				                                              this.getOthers().orElse(""));
	}
}