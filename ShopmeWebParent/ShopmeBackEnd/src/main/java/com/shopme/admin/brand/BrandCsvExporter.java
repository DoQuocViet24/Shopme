package com.shopme.admin.brand;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.AbstractExporter;
import com.shopme.common.entity.Brand;

public class BrandCsvExporter extends AbstractExporter{
	 public void export(List<Brand> listBrands,HttpServletResponse response) throws IOException {
	    	super.setResponseHeader(response,"text/csv", "csv","brand_");
	    	ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
	    	String[] csvHearder = {"Brand ID","Category Name","Categories"};
	    	String[] fieldMapping = {"id","name","categories"};
	    	
	    	csvWriter.writeHeader(csvHearder);
	    	
	    	for(Brand brand : listBrands) {
	    		csvWriter.write(brand, fieldMapping);
	    	}
	    	csvWriter.close();
	    }

}
