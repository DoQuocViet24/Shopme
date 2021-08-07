package com.shopme.admin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.shopme.common.entity.User;

public class AbstractExporter {
	 public void setResponseHeader(HttpServletResponse response, String contentType,
			     String extention, String prefix) throws IOException{
	    	DateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
	    	String timestamp = dateFormater.format(new Date());
	    	String fileName = prefix + timestamp + "." + extention;
	    	response.setContentType(contentType);
	    	String headerKey = "Content-Disposition";
	    	String headerValue = "attacment; filename="+fileName;
	    	response.setHeader(headerKey, headerValue);
	 }
}
