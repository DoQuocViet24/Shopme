package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.product.Product;

public class ProductCsvExporter extends AbstractExporter{
	 public void export(List<Product> listProducts,HttpServletResponse response) throws IOException {
	    	super.setResponseHeader(response,"text/csv", "csv","product_");
	    	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
	    	String[] csvHearder = {"Product ID","Product Name","Brand","Category","Cost","Price","Discount"};
	    	String[] fieldMapping = {"id","name","brand","category","cost","price","discountPercent"};
	    	
	    	csvWriter.writeHeader(csvHearder);
	    	
	    	for(Product product : listProducts) {
	    		csvWriter.write(product, fieldMapping);
	    	}
	    	csvWriter.close();
	    }

}
