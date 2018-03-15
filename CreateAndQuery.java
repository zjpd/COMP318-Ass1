import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.util.FileManager;

/**
 * Comp318 assignment 1
 * @author Jin Zhang Student ID:201219953
 *
 */
public class CreateAndQuery {
	
	static final String FILE = "Nobeldump.nt";
	static final String OUTPUT_FILE = "ConciseNobel.rdf";
	
	Model readInModel;
	Model output;
	InputStream input;
	PrintWriter writer;
	
	/**
	 * Constructor of the class.
	 * initiate the input model and output model and read the file.
	 * if the file is not exist, an error will be thrown
	 */
	public CreateAndQuery() {
		
		readInModel = ModelFactory.createDefaultModel();
		output = ModelFactory.createDefaultModel();
		input = FileManager.get().open(FILE);
		try {
			writer = new PrintWriter(OUTPUT_FILE);
		} catch (FileNotFoundException e) {
			System.out.println("No output file found");
		}
		
		if(input == null) {
			throw new IllegalArgumentException (
						"File: " + FILE + "not found");
		}
		
		readInModel.read(input, null, "N-TRIPLES");
	}
	
	/**
	 * Question 2 part A
	 * create a new graph for Nobel laureate information
	 * name, gender, nationality, discipline and year.
	 */
	public void createGraph() {
		String query_nationality = "PREFIX dbpo: <http://dbpedia.org/ontology/>"
								+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
								+ "PREFIX laureate: <http://data.nobelprize.org/terms/> "
								+ " CONSTRUCT {"
								+ " ?x dbpo:birthPlace ?nationality ."
								+ " ?x foaf:name ?name ."
								+ " ?x foaf:gender ?gender ."
								+ " ?x laureate:category ?discipline ."
								+ " ?x laureate:year ?year}"
								+ " WHERE {"
								+ "  ?x dbpo:birthPlace ?nationality ."
								+ "  ?x foaf:name ?name ."
								+ "  ?x foaf:gender ?gender ."
								+ "  ?y laureate:laureate ?x ."
								+ "  ?y laureate:category ?discipline ."
								+ "  ?y laureate:year ?year"
								+ " }";
		executeGraph(query_nationality);
	}
	
	/**
	 * In order to create a new graph of the model
	 * @param query the sparql for retrieving data
	 */
	public void executeGraph(String query) {
		
		Query execution = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(execution, readInModel);
		
		output = qe.execConstruct();
		output.write(writer, "TURTLE");
		output.close();
	}
	
	/**
	 * Question 2 query 1
	 * find all the nobel laureates from the UK.
	 */
	public void Query1() {
		String query_1 =  "PREFIX dbpo: <http://dbpedia.org/ontology/>"
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
						+ " SELECT ?x ?birth_place"
						+ " WHERE {"
						+ "  ?x dbpo:birthPlace ?birth_place ."
						+ "  FILTER (regex(str(?birth_place), \"United_Kingdom\"))"
						+ "}";
		executeQuery(query_1);
	}
	
	/**
	 * Question 2 query 2
	 * Find all the Nobel Laureates who are female and were born after 1949
	 */
	public void Query2() {
		String query_2 =  "PREFIX dbpo: <http://dbpedia.org/ontology/>"
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
						+ "PREFIX laureate: <http://data.nobelprize.org/terms/> "
						+ " SELECT ?x ?gender ?year"
						+ " WHERE {"
						+ " ?x foaf:gender ?gender ."
						+ " ?y laureate:laureate ?x ."
						+ " ?y laureate:year ?year ."
						+ " FILTER (?year>1949) ."
						+ " FILTER (regex(str(?gender), \"female\"))"
						+ " }";
		executeQuery(query_2);
	}
	
	/**
	 * Question 2 query 3
	 *  List all the Nobel Laureates ordering them by discipline for which they were awarded the prize. 
	 *  List the names in alphabetical order;
	 */
	public void Query3() {
		String query_3 = "PREFIX dbpo: <http://dbpedia.org/ontology/>"
								+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
								+ "PREFIX laureate: <http://data.nobelprize.org/terms/> "
								+ " SELECT DISTINCT ?name ?discipline"
								+ "	WHERE {"
								+ " ?x foaf:name ?name ."
								+ " ?y laureate:laureate ?x ."
								+ " ?y laureate:category ?discipline}"
								+ " ORDER BY ?discipline";
		executeQuery(query_3);
	}
	
	/**
	 * Question 2 query 4
	 * Find all the Nobel Laureates born in the US who share the award with someone else;
	 */
	public void Query4() {
		String query_4 = "PREFIX dbpo: <http://dbpedia.org/ontology/>"
						+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>"
						+ "PREFIX laureate: <http://data.nobelprize.org/terms/> "
						+ " SELECT DISTINCT ?name ?nationality ?year ?discipline"
						+ " WHERE {"
						+ " ?x foaf:name ?name ."
						+ " ?x dbpo:birthPlace ?nationality ."
						+ " ?y laureate:laureate ?x ."
						+ " ?y laureate:category ?discipline ."
						+ " ?y laureate:year ?year ."
						+ " FILTER (regex(str(?nationality), \"USA\"))}"
						+ " ORDER BY ?year";
		executeQuery(query_4);
	}
	
	/**
	 * Execute the query. use result set to hold the results
	 * @param query
	 */
	public void executeQuery(String query) {
		Query q = QueryFactory.create(query);
		QueryExecution qe = QueryExecutionFactory.create(q, readInModel);
		ResultSet results = qe.execSelect();
		ResultSetFormatter.out(System.out, results);
		qe.close();
	}
	
	public static void main(String args[]) {
		CreateAndQuery main = new CreateAndQuery();
		main.createGraph();
		main.Query1();
		main.Query2();
		main.Query3();
		main.Query4();
	}
}
