package com.chinhalamesack.docsforge.services;

import java.io.IOException;
 


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chinhalamesack.docsforge.entities.DocEntity;
import com.chinhalamesack.docsforge.repositories.DocRepo;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;

import jakarta.servlet.http.HttpServletResponse;


@Service
public class PdfGeneratorService {
	

	private final DocRepo docRepo;
	
	@Autowired
	public PdfGeneratorService(DocRepo docRepo) {
		this.docRepo = docRepo;
	}
 
	public void export(HttpServletResponse response, Long id) throws  IOException {
		
		DocEntity doc = docRepo.findById(id).orElseThrow(() ->
		new IllegalArgumentException("Documento não encontrdo"));
		
		ConverterProperties properties = new ConverterProperties();
		response.setContentType("aplication/pdf");
		response.setHeader("Content-Disposition", "attachment; filename="+doc.getName()+".pdf");
		
		HtmlConverter.convertToPdf(doc.getText(),response.getOutputStream(), properties);
		
		
		
		
		
		
		
		
	}
}
