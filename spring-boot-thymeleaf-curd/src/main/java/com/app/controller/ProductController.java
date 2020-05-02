package com.app.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.app.entity.Product;
import com.app.exception.RecordNotFoundException;
import com.app.repo.ProductRepository;

@Controller
public class ProductController {

	@Autowired
	ProductRepository productRepository;

	/*---Add or Update product---*/
	@PostMapping(value = "/product")
	public String save(@ModelAttribute Product product, RedirectAttributes attributes) {
		product = productRepository.save(product);
		attributes.addFlashAttribute("success", "Product Saved by id : " + product.getId());
		return "redirect:/";
	}

	/*---default url and Get a product by id---*/
	@GetMapping(value = { "/", "/product/{id}" })
	public String get(Model model, @PathVariable("id") Optional<Long> id) throws RecordNotFoundException {
		if (id.isPresent()) {
			Optional<Product> product = productRepository.findById(id.get());
			if (product.isPresent()) {
				model.addAttribute("product", product);
			} else {
				throw new RecordNotFoundException("No product record exist for given id : " + id.get());
			}
		} else {
			model.addAttribute("product", new Product());
		}
		return "index";
	}

	/*---get all products---*/
	@ModelAttribute("products")
	public List<Product> list(Model model) {
		List<Product> products = productRepository.findAll();
		return products;
	}

	/*---Delete a product by id---*/
	@GetMapping("/product/delete/{id}")
	public String delete(@PathVariable("id") Optional<Long> id, RedirectAttributes attributes) {
		productRepository.deleteById(id.get());
		attributes.addFlashAttribute("success", "Product record deleted by given id : " + id.get());
		return "redirect:/";
	}

}
