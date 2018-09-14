package com.blackjade.crm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.blackjade.crm.model.Customer;

public interface CustomerDao {
	
	public List<Customer> selectCustomerByUserNameAndPassword(@Param(value = "username") String username ,@Param(value = "password") String password);
	
	public List<Customer> selectCustomerByEmailAndPassword(@Param(value = "email") String email ,@Param(value = "password") String password);
	
	public int insertCustomer(Customer customer);
	
	public int countUsername(@Param(value = "username") String username);
	
	public int countEmail(@Param(value = "email") String email);
	
	public int updateCustomerEmailVerify(@Param(value = "email") String email);
	
	public Customer selectCustomerByUserName(@Param(value = "username") String username);
	
	public int updatePasswordByEmail(Customer customer);
	
	public Customer selectCustomerByIdAndPassword(@Param(value = "id") int id ,@Param(value = "password") String password);
	
	public int modifyPasswordById(@Param(value = "id") int id ,@Param(value = "password") String password);
	
	public int updateCustomerDetails(Customer customer);
	
	public Customer selectCustomerById(@Param(value = "id") int id);
	
	public Customer scanPersonalInformation(@Param(value = "id") int id);
}
