package com.openkart.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.openkart.exception.NoSuchElementException;
import com.openkart.exception.ResourceNotFoundException;
import com.openkart.helper.JwtUtil;
import com.openkart.model.BuyProduct;
import com.openkart.model.Buyer;
import com.openkart.model.JwtResponse;
import com.openkart.model.Jwtrequest;
import com.openkart.model.Product;
import com.openkart.model.Seller;
import com.openkart.service.BuyerService;
import com.openkart.service.CustomUserDetailsService;
import com.openkart.service.SellerService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@CrossOrigin("*")

// Admin Controller
public class AdminController {
	
	@Autowired  // Buyer Service
	private BuyerService buyerService;
	@Autowired // Seller Service
	private SellerService sellerService;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsDervice;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
//	@RequestMapping(value = "/token", method = RequestMethod.POST, produces = "application/json")
   @PostMapping(value="/generate-token",produces = "application/json")
	public ResponseEntity<?> generateToken(@org.springframework.web.bind.annotation.RequestBody Jwtrequest jwtRequest ) throws Exception
	{
	 
	   System.out.println(jwtRequest);
		try
		   { 
			  this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
		   } 
		catch (UsernameNotFoundException e) 
		{
			e.printStackTrace();
			throw new Exception("Username not present");
		
		}
		UserDetails userDetails = this.customUserDetailsDervice.loadUserByUsername(jwtRequest.getUsername());
		
		String token=this.jwtUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new JwtResponse(token));
		
	
	}
	
	//buyer controller // To get All Buyers
	
	@RequestMapping(value = "/getAllBuyer", method = RequestMethod.GET, produces = "application/json")
	
	public java.util.List<Buyer> getAllBuyers()
	{
		
		return buyerService.getAllBuyer();	
	}
	
	//Buyer Controller //To get Buyer by Id
	
	@RequestMapping(value = "/getBuyerById/{id}", method = RequestMethod.GET, produces = "application/json")
	
	public Optional<Buyer> getBuyerById(@PathVariable("id") int id)
	{
	  //Exception : if Buyerid is not present in DB.
		
		if(!buyerService.getBuyerbyId(id).isPresent())
		    throw new NoSuchElementException("Buyer",id);
		
		else
		  return buyerService.getBuyerbyId(id);	
	}
	
	//Buyer Controller //To delete Buyer by Id
	@DeleteMapping( "/deleteBuyerById/{id}" )
	
	public Optional<Buyer> deleteBuyerById(@PathVariable("id") int id) 
	{
        
		//Exception : if Buyerid is not present in DB.
		
		if(!buyerService.getBuyerbyId(id).isPresent())
		    throw new NoSuchElementException("Buyer",id);
		
		
		// if buyer Id is present
		Optional<Buyer> deletedBuyer = null;
		Optional<Buyer> Buyer = buyerService.getBuyerbyId(id);
		buyerService.deleteBuyerbyId(id);
		deletedBuyer = Buyer;
		return deletedBuyer;
	}
	
	//Seller controller //to get all sellers
	@RequestMapping(value = "/getAllSeller", method = RequestMethod.GET, produces = "application/json")
	
	public java.util.List<Seller> getAllSeller(){
		
		return sellerService.getAllSeller()	;
	}
	
	//Seller controller //to get seller by ID
	
	@RequestMapping(value = "/getSellerById/{id}", method = RequestMethod.GET, produces = "application/json")
	
	public Optional<Seller> getSellerById(@PathVariable("id") int id)
	{
	    //Exception : if SellerId is not present in DB.
		
		if(!sellerService.getSellerById(id).isPresent())
		    throw new NoSuchElementException("Seller",id);
		//if seller Id is present
		return sellerService.getSellerById(id);
	}
	//Seller controller //to delete seller by ID
	@DeleteMapping( "/deleteSellerrById/{id}" )

	public ResponseEntity<Map<String,Boolean>>deleteSellerById(@PathVariable("id") int id) 
	{
		    //Exception : if Sellerid is not present in DB.
				if(!sellerService.getSellerById(id).isPresent())
				    throw new NoSuchElementException("Seller",id);
				
		//if seller id is present in DB.
		
		sellerService.deleteSellerBYId(id);
		 Map<String,Boolean> response= new HashMap<>();
		 response.put("deleted",Boolean.TRUE);
		 return ResponseEntity.ok(response);
		 
		 
	}
	
	
	//Product controller //to get all products.
	@RequestMapping(value = "/getAllProductsAllSellers", method = RequestMethod.GET, produces = "application/json")
	public java.util.List<Product> getAllProducts(){
		
		return buyerService.getAllProduct();	
	}
	
	//Product controller //to get all bought products.
	@RequestMapping(value = "/getAllBuyedProduct", method = RequestMethod.GET, produces = "application/json")
	public java.util.List<BuyProduct> getAllBuyedProduct(){
		
		return buyerService.getAllBuyedProduct();
	}
	
	

	
}
