import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;

/**
 * This tool takes a path directory and Java type and parses all the
 * files with .java extensions. The amount of declarations
 * and references withing all the files is printed to the console
 * @author Josh, Logan, Madhu
 *
 */
public class AnalysisTool {

	private String pathname;
	private String type;
	private int dCount;
	private int rCount;
	
	public static void main(String[] args) {
		if(args.length != 2) {
			throw new IllegalArgumentException();
		}
		
		AnalysisTool toolA = new AnalysisTool(args[0],args[1]);
		toolA.getFile(toolA.getPathName());
		System.out.println(toolA.printResult());
	}
	
	public String getPathName() {
		return pathname;
	}
	
	public String getType(){
		return type;
	}
	public int getDCount() {
		return dCount;
	}
	public int getRCount() {
		return rCount;
	}
	
	/**
	 * Constructor for the Class, used to set variables
	 * @param path directory to be searched
	 * @param type to search for during parsing
	 */
	public AnalysisTool(String path, String type) {
		dCount = 0;
		rCount = 0;
		pathname = path;
		this.type = type;
	}
	/**
	 * The results of the AnalysisTool
	 * @return String containing the results
	 */
	public String printResult() {
		return type+". Declarations found: "+dCount+"; references found: "+rCount+".";
	}
	
	/**
	 * Parses a file using ASTParser
	 * @param fileName of the file to be parsed
	 */
	public void parseTool(String fileName) {
	
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setSource(fileToChar(fileName));
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setEnvironment(null, null, null, true);
		parser.setUnitName("lol");
		final CompilationUnit cu = (CompilationUnit)parser.createAST(null);
		cu.accept(new ASTVisitor() {			
			
			/**
			 * Gets the name of the node and compares to
			 * the type to look for, if the same
			 * increases dCount by 1
			 * @param node of the type TypeDeclaration
			 * @return true to visit the children nodes
			 */
			public boolean visit(TypeDeclaration node) {
				String nodeName = node.getName().getFullyQualifiedName();
				if(nodeName.equals(getType())) dCount++;
				return true;
			}
			
			/**
			 * Used for Enum declarations
			 * Gets the name of the node and compares to
			 * the type to look for, if the same
			 * increases dCount by 1
			 * @param node of the type EnumDeclaration
			 * @return true to visit the children nodes
			 */
			public boolean visit(EnumDeclaration node) {
				ITypeBinding typeBind = node.resolveBinding();
				
				String typeNode = typeBind.getQualifiedName();
				if(typeNode.equals(getType())) dCount++;
				
				return true;
			}
			
			/**
			 * Used for Annotation declarations
			 * Gets the name of the node and compares to
			 * the type to look for, if the same
			 * increases dCount by 1
			 * @param node of the type AnnotationTypeDeclaration
			 * @return true to visit the children nodes
			 */
			public boolean visit(AnnotationTypeDeclaration node) {
				ITypeBinding typeBind = node.resolveBinding();
				String typeNode = typeBind.getQualifiedName();				
				if(typeNode.equals(getType())) dCount++;
				return true;
			}
			
			/**
			 * Used for referential count
			 * @param node of type QualififedName
			 * @return false to not visit children
			 */
			public boolean visit(QualifiedName node) {
				if(node.getFullyQualifiedName().equals(getType())) rCount++;
				return false;
			}
			
			/**
			 * @param node of type VariableDeclarationFragment
			 * Resolves the bindings so we can acquire the node's full name
			 * @return false, don't visit children
			 */
			public boolean visit(VariableDeclarationFragment node) {
				IVariableBinding rNode = node.resolveBinding();
				String typeNode = rNode.getType().getQualifiedName();
				if(typeNode.equals(getType())) rCount++;
				return false;
			}
			
		});
	}
	/**
	 * Converts the file name to a char array
	 * @param file to be converted
	 * @return	char[] of the file name
	 */
	public char[] fileToChar(String file) {
	    // char array to store the file contents in
	    char[] contents = null;
	    try {
	        BufferedReader br = new BufferedReader(new FileReader(file));
	            StringBuffer sb = new StringBuffer();
	        String line = "";
	        while((line = br.readLine()) != null) {
	            // append the content and the lost new line.
	            sb.append(line + "\n");
	        }
	        contents = new char[sb.length()];
	        sb.getChars(0, sb.length()-1, contents, 0);
	 
	        assert(contents.length > 0);
	    }
	    catch(IOException e) {
	            System.out.println(e.getMessage());
	    }
	    return contents;
	}
	
	/**
	 * Retrieves all the .java files within the given directory
	 * We retrieved a portion of this code from the following link
	 * https://stackoverflow.com/questions/1844688/how-to-read-all-files-in-a-folder-from-java
	 * @param path directory to look for files
	 */
	public void getFile(String path) {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		    		if(file.getName().matches(".*\\.java")) {
		    			parseTool(path+"\\"+file.getName());
		    		}
		    }
		}
	}

}
