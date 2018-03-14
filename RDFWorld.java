import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.VCARD;

public class RDFWorld {
	
	private Model model = ModelFactory.createDefaultModel();
	private String personURI    = "http://somewhere/JohnSmith";
	private String givenName    = "John";
	private String familyName   = "Smith";
	private String fullName     = givenName + " " + familyName;
	
	private File file = new File("D:\\javaworkplace\\JenaTest\\src\\test.rdf");
	private PrintWriter writer;
	private InputStream input;
	
	public RDFWorld() throws FileNotFoundException {
		
		StmtIterator iter = model.listStatements();
		writer = new PrintWriter(new FileOutputStream(file));
		
		Resource johnSmith
		  = model.createResource(personURI)
		         .addProperty(VCARD.FN, fullName)
		         .addProperty(VCARD.N,
		                      model.createResource()
		                           .addProperty(VCARD.Given, givenName)
		                           .addProperty(VCARD.Family, familyName));
		
		while(iter.hasNext()) {
			Statement statement = iter.nextStatement();
			Resource subject = statement.getSubject();
			Property predicate = statement.getPredicate();
			RDFNode object = statement.getObject();
			
			System.out.print(subject.toString());
			System.out.print(" " + predicate.toString() + " ");
			if(object instanceof Resource) {
				System.out.print(object.toString());
			} else {
				//object is literal
				System.out.println(" \""+ object.toString() + "\"");
			}
			
			System.out.println(" .");
		}
		
//		model.write(writer);
//		writer.flush();
//		writer.close();
		
	}
	
	public void prefixes() {
		String nsA = "http://somewhere/else#";
		 String nsB = "http://nowhere/else#";
		 Resource root = model.createResource( nsA + "root" );
		 Property P = model.createProperty( nsA + "P" );
		 Property Q = model.createProperty( nsB + "Q" );
		 Resource x = model.createResource( nsA + "x" );
		 Resource y = model.createResource( nsA + "y" );
		 Resource z = model.createResource( nsA + "z" );
		 model.add( root, P, x ).add( root, P, y ).add( y, Q, z );
		 System.out.println( "# -- no special prefixes defined" );
		 model.write( System.out );
		 System.out.println( "# -- nsA defined" );
		 model.setNsPrefix( "nsA", nsA );
		 model.write( System.out );
		 System.out.println( "# -- nsA and cat defined" );
		 model.setNsPrefix( "cat", nsB );
		 model.write( System.out );
	}
	
	
	public static void main(String args[]) throws FileNotFoundException {
		new RDFWorld().prefixes();
	}

}
