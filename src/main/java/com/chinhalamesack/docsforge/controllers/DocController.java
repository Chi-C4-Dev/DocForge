package com.chinhalamesack.docsforge.controllers;


import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.chinhalamesack.docsforge.entities.DocEntity;
import com.chinhalamesack.docsforge.repositories.DocRepo;
import com.chinhalamesack.docsforge.services.PdfGeneratorService;
import com.itextpdf.io.exceptions.IOException;

import jakarta.servlet.http.HttpServletResponse;



@Controller

public class DocController {

	@Autowired
	DocRepo docRepo;

	 PdfGeneratorService pdfGenerator;
	
	@Autowired
	public DocController(PdfGeneratorService pdfGenerator) {
		this.pdfGenerator = pdfGenerator;
	}
	
	@GetMapping("/new")
	public ModelAndView newDoc() {
		ModelAndView mv = new ModelAndView("index");
		mv.addObject("doc", new DocEntity());
		
		return mv;  
		}
	
	@PostMapping("/new")
	public String newDoc(@ModelAttribute DocEntity doc, RedirectAttributes attributes) {
		DocEntity existent = docRepo.findByName(doc.getName());
		if(existent!= null) {
			attributes.addFlashAttribute("mensagemError", "Já existe um documento com este nome.");
			return "redirect:/new";
		}  
		 
		if(doc.getName() == null || doc.getName().equals("") ) {
			attributes.addFlashAttribute("mensagemError", "O documento deve ter um nome");
			return "redirect:/new";
		}
		 
		 
		 
	
		var currentDate = LocalDateTime.now();
		doc.setCreatedAt(currentDate);
		
		docRepo.save(doc);
		
		return "redirect:/docs";
	}     
	     
	@GetMapping("/docs")
	public ModelAndView listDocs() {
		ModelAndView mv = new ModelAndView("list");
		List<DocEntity> docs = docRepo.findAll();
		mv.addObject("doc", docs);
		return mv;
	}
	
	@GetMapping("viewer/{id}")
	public ModelAndView visualizeDoc(@PathVariable("id") long id) {
		ModelAndView mv = new ModelAndView("viewer");
		DocEntity doc = docRepo.findById(id);
		mv.addObject("doc", doc);
		 
		return mv;
	}    
	  
	@GetMapping("edit/{id}")
	public ModelAndView editDoc(@PathVariable("id") long id) {
		ModelAndView mv = new ModelAndView("edit");
		DocEntity doc = docRepo.findById(id);
		mv.addObject("doc", doc);
		
		return mv;
	} 
	
	
	@PostMapping("edit/{id}")
	public String edit(@ModelAttribute DocEntity doc, RedirectAttributes attributes) {
		//DocEntity existent = docRepo.findByName(doc.getName());
		
		
		if(doc.getName() == null || doc.getName().equals("")) {
			attributes.addFlashAttribute("mensagemError", "O documento deve ter um nome");
			return "redirect:/new";
		} 
	    
		var currentDate = LocalDateTime.now();
		doc.setCreatedAt(currentDate);
		 
		docRepo.save(doc);
		
		return "redirect:/docs";
	} 
	  
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable("id") long id) {
		 DocEntity doc = docRepo.findById(id);
		 
		 if(doc != null) {
			 docRepo.delete(doc);
		 } 
		  
		 return "redirect:/docs"; 
	}
	
	@GetMapping("pdf/{id}")
	public ResponseEntity<?> gerarPdf(@PathVariable("id") long id, HttpServletResponse response, RedirectAttributes attributes) throws java.io.IOException{
		try {
			pdfGenerator.export(response, id);
			return ResponseEntity.ok().build();
		} catch (IOException e) {
			
			return ResponseEntity.status(500).body("erro ao gerar o pdf: " + e.getMessage());
		}
	}
	
	

}
 