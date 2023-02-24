package com.openkart.controller;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.openkart.service.SellerService;
import com.openkart.dto.ProductRequest;
import com.openkart.dto.SellerRequest;
import com.openkart.exception.AlreadyExistsException;
import com.openkart.exception.NoSuchElementException;
import com.openkart.model.*;
import com.openkart.repository.SellerRepository;

@RestController
@CrossOrigin("http://localhost:4200")

// Controller
public class SellerController {
	
	@Autowired //Autowiring Seller Service
	private SellerService sellerService;
   

	//to add Seller
	@PostMapping("/addSeller")
	
	public Seller addSeller(@RequestBody @Valid SellerRequest sellerRequest) 
	{
		
		//Exception :if SellerId Already Exists;
	     if(sellerService.getSellerById(sellerRequest.getSellerId()).isPresent())
			throw new AlreadyExistsException("Seller",sellerRequest.getSellerId());
		
	  
		
	     //if seller ID or seller Email does not Exist in database
		return sellerService.addSeller(sellerRequest);
	}
	
	//to add product in database.
	@PostMapping("/addProduct")

	public Product addProduct(@RequestBody  @Valid ProductRequest productRequest) 
	{
		
		//Exception :if ProductId Already Exists;
		
	     if(sellerService.getProductById(productRequest.getProductId()).isPresent())
			throw new AlreadyExistsException("Product",productRequest.getProductId());
	     
	     //Exception : if Seller Id does not exists;
	     
	     if(!sellerService.getSellerById(productRequest.getSellerId()).isPresent())
	    	 throw new NoSuchElementException("Seller",productRequest.getSellerId());
		
		//if product id and sellerId Exist in database
		return sellerService.addProduct(productRequest);
	}
		
	//to delete product by Id
	@DeleteMapping( "deleteProductById/{id}" )
	public ResponseEntity<Map<String,Boolean>> deleteProductById(@PathVariable("id") int id)
	{
		int id1=(int)id;
	
//		Exception :if ProductId does not Exists;
	     if(!sellerService.getProductById(id1).isPresent())
			throw new NoSuchElementException("Product",id1);
		
	     //if product already Exist in database
		
		sellerService.deleteProductById(id1);
		 Map<String,Boolean> response= new HashMap<>();
		 response.put("deleted",Boolean.TRUE);
		 return ResponseEntity.ok(response);
	}
	
  //Update Product by seller using seller id and product id
	@PutMapping( "/updateProduct/{id}" )
	
	public Optional<Product> updateProductById( @PathVariable("id") int id
			 , @RequestBody @Valid ProductRequest product)
	{
		//Exception :if ProductId Does not Exists;
	     if(!sellerService.getProductById(id).isPresent())
			throw new NoSuchElementException("Product",id);
	     
	  
	     
	   //if product Id and Seller ID Exist and Products belong to the seller only.
	    
		       Optional<Product> updatedProduct=sellerService.updateProductById(id, product);
		       return updatedProduct;
	          
	   
	}

    //to get product by seller Id
	@RequestMapping(value = "/getProductBySellerId/{id}", method = RequestMethod.GET, produces = "application/json")
	
	public java.util.List<Product> getProductBySellerId(@PathVariable("id") int sellerId)
	{
		//Exception :if SellerId does not Exists;
	     if(!sellerService.getSellerById(sellerId).isPresent())
			throw new NoSuchElementException("Seller",sellerId);
	
	     // if Seller Id Exist in database.
		return sellerService.getProductBySellerId(sellerId);
	}
	
	//update seller rest api
	@PutMapping("/seller/{id}")
	
	public Optional<Seller> updateSeller(@PathVariable("id") int sellerId,@RequestBody @Valid SellerRequest sellerRequest)
	{
		
		   //Exception :if Seller id does not exist;
	     if(!sellerService.getSellerById(sellerId).isPresent())
			throw new NoSuchElementException("Seller",sellerId);
	     
	     Optional<Seller> updatedSeller=sellerService.updateSellerById(sellerId,sellerRequest);
	     
	       return updatedSeller;
	}
	
	

}
