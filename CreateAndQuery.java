import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.util.FileManager;

public class CreateAndQuery {
	
	private static final String NOBEL_FILE = "Nobeldump.nt";
	
	private static ArrayList<Object> namelist = new ArrayList<Object>();
	
	private Model model;
	private Model outputModel;
	private InputStream input;
	
	public CreateAndQuery() {
		
		model = ModelFactory.createDefaultModel();
		outputModel = ModelFactory.createDefaultModel();
		input = FileManager.get().open(NOBEL_FILE);
		
		if(input == null) {
			throw new IllegalArgumentException (
						"File: " + NOBEL_FILE + "not found");
		}
		
		model.read(input, null, "N-TRIPLES");
//		model.write(System.out);
		System.out.println("The size of model is: "+model.size());
	}
	
	//get name, gender, nationality, discipline, year
	public void createGraph() {
		ResIterator iter = model.listSubjectsWithProperty(FOAF.name);
		
		if(iter.hasNext()) {
			System.out.println("The nobel contains the name");
	
			
			while(iter.hasNext()) {
				Resource tmpResource = iter.nextResource();
				namelist.add(tmpResource);
//				tmpname.add(tmpResource.getProperty(FOAF.name).getString());
			}
//			System.out.println(namelist.size()+"\t"+tmpname.size());
			createDetailInfo();
		} else {
			System.out.println("No name found");
		}
		
	}
	
	protected void createDetailInfo() {
		int count = 0;
		
		Iterator<Object> mapiter = namelist.iterator();
		while(mapiter.hasNext() && count <10) {
			
			count ++;
			Resource tmpR = (Resource)mapiter.next();
			
			System.out.println("The gender is: "+tmpR.getProperty(FOAF.gender));
			
			Resource nobelname = outputModel.createResource(tmpR)
					.addProperty(FOAF.name, tmpR.getProperty(FOAF.name).getString())
					.addProperty(FOAF.gender, tmpR.getProperty(FOAF.name).getString())
					.addProperty();
		}
		
	}
	
	public static void main(String args[]) {
		new CreateAndQuery().createGraph();
	}
}
