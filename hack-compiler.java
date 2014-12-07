import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class parser {
	static ArrayList<String> outC = new ArrayList<String>();
	static ArrayList<String> fileContentsC = new ArrayList<String>();
	static int indent = 0;
	
	public static void main(String[] args) {
		//Setup variables
		String file = "", directory = "";
		ArrayList<String> out = new ArrayList<String>();
		
		//Setup variables for testing purposes
		//directory = "/Users/proales/Dropbox/Top Folder/nand2tetris/projects/10/";
		directory = "/home/proales/Desktop/nand2tetris/projects/10/";
		//file = "ArrayTest/";
		//file = "ExpressionlessSquare/";
		file = "Square/";

		//Prompt and Get User Input
		System.out.println("Working Directory = "+System.getProperty("user.dir"));
		System.out.println("Type in the file location and name or directory location:");
		Scanner scan = new Scanner(System.in);
		String userInput = scan.nextLine();
		if (userInput.equals(""))
			userInput = directory+file;
		
		//Create the output directory
		directory = userInput.substring(0, userInput.lastIndexOf("/"));
		File tempFile = new File(directory+"/out/");
		tempFile.mkdirs();

		//Load Files
		File temp = new File(userInput);
		if(temp.isDirectory()){
			//If directory was indicated then load each file
			File[] listOfFiles = temp.listFiles();
			for (int i = 0; i < listOfFiles.length; i++) {
				File fileTemp = listOfFiles[i];
				if (fileTemp.isFile() && fileTemp.getName().endsWith(".jack")) {
					//Process and prep file name
					ArrayList<String> fileContents = loadFile(fileTemp.getPath());
					fileContents = tokenize(fileContents);
					String fileNameOut = directory+"/out/"+fileTemp.getName().substring(0, fileTemp.getName().indexOf("."))+"T.xml";
					writeFile(fileNameOut, fileContents);
					out = parse(fileContents);
					fileNameOut = directory+"/out/"+fileTemp.getName().substring(0, fileTemp.getName().indexOf("."))+".xml";
					writeFile(fileNameOut, out);
				} 
			}
		} else {
			//If file was indicated then throw error and quit
			System.out.println("Error loading directory.");
			System.exit(1);
		}
	}
	
	public static ArrayList<String> tokenize(ArrayList<String> fileContents){
		ArrayList<String> out = new ArrayList<String>();

		//Remove White Space and Full Line Comments
		for(int x = 0; x<fileContents.size(); x++){
			if (fileContents.get(x).equals("") || fileContents.get(x).startsWith("//")){
				fileContents.remove(x);
				x--;
			}
		}

		//Remove partial line comments
		for(int x = 0; x<fileContents.size(); x++){
			if (fileContents.get(x).contains("//")){
				fileContents.set(x, fileContents.get(x).substring(0, fileContents.get(x).indexOf("/")));
			}
		}
		
		//Remove leading spaces or tabs
		for(int x = 0; x<fileContents.size(); x++){
			fileContents.set(x, fileContents.get(x).trim());
		}
		
		//Dump the array list to a flat string
		String fileString = "";
		for (String s : fileContents)
			fileString += s; 
			
		//Remove multi-line comments
		while(fileString.contains("/*")){
			int firstIndex = fileString.indexOf("/*");
			int secondIndex = fileString.indexOf("*/");
			fileString = fileString.substring(0, firstIndex)+fileString.substring(secondIndex+2);
		}
		
		//Header
		out.add("<tokens>");

		//Loop through and process every line 
		while(fileString.length() > 0){
			fileString = fileString.trim();
			
			if (fileString.startsWith(" ")){ 
				fileString.trim();
				
			} else if (fileString.startsWith("class")) {
				out.add("<keyword> class </keyword>");
				fileString = fileString.substring(fileString.indexOf("class")+5);
				
			} else if (fileString.startsWith("constructor")) {
				out.add("<keyword> constructor </keyword>");
				fileString = fileString.substring(fileString.indexOf("constructor")+11);
				
			} else if (fileString.startsWith("function")) {
				out.add("<keyword> function </keyword>");
				fileString = fileString.substring(fileString.indexOf("function")+8);
				
			} else if (fileString.startsWith("method")) {
				out.add("<keyword> method </keyword>");
				fileString = fileString.substring(fileString.indexOf("method")+6);
				
			} else if (fileString.startsWith("field")) {
				out.add("<keyword> field </keyword>");
				fileString = fileString.substring(fileString.indexOf("field")+5);
				
			} else if (fileString.startsWith("static")) {
				out.add("<keyword> static </keyword>");
				fileString = fileString.substring(fileString.indexOf("static")+6);
				
			} else if (fileString.startsWith("var")) {
				out.add("<keyword> var </keyword>");
				fileString = fileString.substring(fileString.indexOf("var")+3);
				
			} else if (fileString.startsWith("int")) {
				out.add("<keyword> int </keyword>");
				fileString = fileString.substring(fileString.indexOf("int")+3);
				
			} else if (fileString.startsWith("char")) {
				out.add("<keyword> char </keyword>");
				fileString = fileString.substring(fileString.indexOf("char")+4);
				
			} else if (fileString.startsWith("boolean")) {
				out.add("<keyword> boolean </keyword>");
				fileString = fileString.substring(fileString.indexOf("boolean")+7);
				
			} else if (fileString.startsWith("void")) {
				out.add("<keyword> void </keyword>");
				fileString = fileString.substring(fileString.indexOf("void")+4);
				
			} else if (fileString.startsWith("true")) {
				out.add("<keyword> true </keyword>");
				fileString = fileString.substring(fileString.indexOf("true")+4);
				
			} else if (fileString.startsWith("false")) {
				out.add("<keyword> false </keyword>");
				fileString = fileString.substring(fileString.indexOf("false")+5);
				
			} else if (fileString.startsWith("null")) {
				out.add("<keyword> null </keyword>");
				fileString = fileString.substring(fileString.indexOf("null")+4);
				
			} else if (fileString.startsWith("this")) {
				out.add("<keyword> this </keyword>");
				fileString = fileString.substring(fileString.indexOf("this")+4);
				
			} else if (fileString.startsWith("let")) {
				out.add("<keyword> let </keyword>");
				fileString = fileString.substring(fileString.indexOf("let")+3);
				
			} else if (fileString.startsWith("do")) {
				out.add("<keyword> do </keyword>");
				fileString = fileString.substring(fileString.indexOf("do")+2);
				
			} else if (fileString.startsWith("if")) {
				out.add("<keyword> if </keyword>");
				fileString = fileString.substring(fileString.indexOf("if")+2);
				
			} else if (fileString.startsWith("else")) {
				out.add("<keyword> else </keyword>");
				fileString = fileString.substring(fileString.indexOf("else")+4);
				
			} else if (fileString.startsWith("while")) {
				out.add("<keyword> while </keyword>");
				fileString = fileString.substring(fileString.indexOf("while")+5);
				
			} else if (fileString.startsWith("return")) {
				out.add("<keyword> return </keyword>");
				fileString = fileString.substring(fileString.indexOf("return")+6);
				
			} else if (fileString.startsWith("{")) {
				out.add("<symbol> { </symbol>");
				fileString = fileString.substring(fileString.indexOf("{")+1);
				
			} else if (fileString.startsWith("}")) {
				out.add("<symbol> } </symbol>");
				fileString = fileString.substring(fileString.indexOf("}")+1);
				
			} else if (fileString.startsWith("(")) {
				out.add("<symbol> ( </symbol>");
				fileString = fileString.substring(fileString.indexOf("(")+1);
				
			} else if (fileString.startsWith(")")) {
				out.add("<symbol> ) </symbol>");
				fileString = fileString.substring(fileString.indexOf(")")+1);
				
			} else if (fileString.startsWith("[")) {
				out.add("<symbol> [ </symbol>");
				fileString = fileString.substring(fileString.indexOf("[")+1);
				
			} else if (fileString.startsWith("]")) {
				out.add("<symbol> ] </symbol>");
				fileString = fileString.substring(fileString.indexOf("]")+1);
				
			} else if (fileString.startsWith(".")) {
				out.add("<symbol> . </symbol>");
				fileString = fileString.substring(fileString.indexOf(".")+1);
				
			} else if (fileString.startsWith(",")) {
				out.add("<symbol> , </symbol>");
				fileString = fileString.substring(fileString.indexOf(",")+1);
				
			} else if (fileString.startsWith(";")) {
				out.add("<symbol> ; </symbol>");
				fileString = fileString.substring(fileString.indexOf(";")+1);
				
			} else if (fileString.startsWith("+")) {
				out.add("<symbol> + </symbol>");
				fileString = fileString.substring(fileString.indexOf("+")+1);
				
			} else if (fileString.startsWith("-")) {
				out.add("<symbol> - </symbol>");
				fileString = fileString.substring(fileString.indexOf("-")+1);
				
			} else if (fileString.startsWith("*")) {
				out.add("<symbol> * </symbol>");
				fileString = fileString.substring(fileString.indexOf("*")+1);
				
			} else if (fileString.startsWith("/")) {
				out.add("<symbol> / </symbol>");
				fileString = fileString.substring(fileString.indexOf("/")+1);
				
			} else if (fileString.startsWith("&")) {
				out.add("<symbol> &amp; </symbol>");
				fileString = fileString.substring(fileString.indexOf("&")+1);
				
			} else if (fileString.startsWith("|")) {
				out.add("<symbol> | </symbol>");
				fileString = fileString.substring(fileString.indexOf("|")+1);
				
			} else if (fileString.startsWith("<")) {
				out.add("<symbol> &lt; </symbol>");
				fileString = fileString.substring(fileString.indexOf("<")+1);
				
			} else if (fileString.startsWith(">")) {
				out.add("<symbol> &gt; </symbol>");
				fileString = fileString.substring(fileString.indexOf(">")+1);
				
			} else if (fileString.startsWith("=")) {
				out.add("<symbol> = </symbol>");
				fileString = fileString.substring(fileString.indexOf("=")+1);
				
			} else if (fileString.startsWith("-")) {
				out.add("<symbol> - </symbol>");
				fileString = fileString.substring(fileString.indexOf("-")+1);
				
			} else if (fileString.startsWith("~")) {
				out.add("<symbol> ~ </symbol>");
				fileString = fileString.substring(fileString.indexOf("~")+1);
				
			} else if (fileString.startsWith("\"")) {
				String constant = "";
				int firstIndex = fileString.indexOf("\"");
				int secondIndex = fileString.indexOf("\"",1);
				constant = fileString.substring(firstIndex+1,secondIndex-1);
				fileString = fileString.substring(secondIndex+1);
				out.add("<stringConstant> "+constant+" </stringConstant>");
				
			} else if (	fileString.startsWith("0") ||  
						fileString.startsWith("1") ||
						fileString.startsWith("2") ||
						fileString.startsWith("3") ||
						fileString.startsWith("4") ||
						fileString.startsWith("5") ||
						fileString.startsWith("6") ||
						fileString.startsWith("7") ||
						fileString.startsWith("8") ||
						fileString.startsWith("9") ) {
				//Then process integer constant
				String constantString = "";
				
				while(fileString.substring(0,1).matches("[0-9]")){
					constantString = constantString + fileString.charAt(0);
					fileString = fileString.substring(1);
				}
				
				int constant = Integer.parseInt(constantString);
				out.add("<integerConstant> "+constant+" </integerConstant>");

			} else if (fileString.substring(0,1).matches("[a-zA-Z_]")) {
				//loop through collecting letters, digits and underscore, not starting with digit
				String identifier = "";
				
				while(fileString.substring(0,1).matches("[a-zA-Z0-9_]")){
					identifier = identifier + fileString.charAt(0);
					fileString = fileString.substring(1);
				}
				
				out.add("<identifier> "+identifier+" </identifier>");
				
			} else {	
				System.out.println("Operator Line Processing Error. Clearing string.");
				System.out.println("String was: "+fileString.substring(0, 20));
				fileString = "";
			}
		}		
		
		//Footer
		out.add("</tokens>");
		
		return out;
	}
	
	public static void addC(String input){
		String pad = "";
		for(int x = 0; x<indent; x++){
			pad = pad+" ";
		}
		outC.add(pad+input);
	}
	
	public static void removeC(){
		fileContentsC.remove(0);
	}
	
	public static void addCRemoveC(String input){
		addC(input);
		removeC();
	}
	
	public static void pushC(){
		addCRemoveC(fileContentsC.get(0));
		//removeC();
	}
	
	public static String token1(){
		return fileContentsC.get(0).substring(0, fileContentsC.get(0).indexOf(">"));
	}
	
	public static String token2(){
		//System.out.println("Token2 check: "+fileContentsC.get(0).substring(fileContentsC.get(0).indexOf(">")+2, fileContentsC.get(0).indexOf("</")-1));
		return fileContentsC.get(0).substring(fileContentsC.get(0).indexOf(">")+2, fileContentsC.get(0).indexOf("</")-1);
	}
	
	public static String token1ahead(){
		return fileContentsC.get(1).substring(1, fileContentsC.get(1).indexOf(">"));
	}
	
	public static String token2ahead(){
		//System.out.println("Token2 check: "+fileContentsC.get(1).substring(fileContentsC.get(1).indexOf(">")+2, fileContentsC.get(1).indexOf("</")-1));
		return fileContentsC.get(1).substring(fileContentsC.get(1).indexOf(">")+2, fileContentsC.get(1).indexOf("</")-1);
	}


	public static ArrayList<String> parse(ArrayList<String> fileContents){
		outC.clear();
		fileContentsC.clear();
		fileContentsC = fileContents;
		compileClassVarDec();
		return outC;
	}

	private static void compileClassVarDec() {
		addCRemoveC("<class>");
		indent += 2;
		pushC();
		pushC();
		pushC();
		//variable dec
		while (token2().equals("var")    || 
			   token2().equals("static") ||
			   token2().equals("field")  ){
			addC("<classVarDec>");
			indent += 2;
			compileVarDec();
			indent -= 2;
			addC("</classVarDec>");
		}
		//function, constructor or method
		while(token2().equals("function") || 
		      token2().equals("constructor") || 
		      token2().equals("method")){ 
			addC("<subroutineDec>");
			indent += 2;
			compileSubroutine();
			indent -= 2;
			addC("</subroutineDec>");
		}
		pushC();
		indent -= 2;
		addCRemoveC("</class>");
	}
	
	private static void compileSubroutine() {
		
		pushC();
		pushC();
		pushC();
		pushC();
		compileParameterList();
		pushC();
		addC("<subroutineBody>");
		indent += 2;
		pushC();
		//Varaibles
		while(token2().equals("var")    || 
			  token2().equals("static") ||
			  token2().equals("field")  ){
			addC("<varDec>");
			indent += 2;
			compileVarDec();
			indent -= 2;
			addC("</varDec>");
		}
		//Satements
		//addC("<statements>");
		//indent += 2;
		compileStatements();
		//indent -= 2;
		//addC("</statements>");
		pushC();
		//Ending
		indent -= 2;
		addC("</subroutineBody>");
		
		//pushC();
	}
	
	private static void compileParameterList() {
		addC("<parameterList>");
		indent += 2;
		if(token2().equals(")")){
			//nothing
		} else {
			pushC();
			pushC();
			while(token2().equals(",")){
				pushC();
				pushC();
				pushC();
			}
		}
		indent -= 2;
		addC("</parameterList>");
	}
	
	private static void compileVarDec() {
		pushC();
		pushC();
		pushC();
		while (token2().equals(",")){
			pushC();
			pushC();
		}
		pushC();	
	}
	
	private static void compileStatements() {
		addC("<statements>");
		indent += 2;
		while(token2().equals("let")   || 
			  token2().equals("if")    ||
		      token2().equals("while") ||
		      token2().equals("do")    ||
		      token2().equals("return")  ){
			if (token2().equals("let"))
				compileLet();
			if (token2().equals("if"))
				compileIf();
			if (token2().equals("while"))
				compileWhile();
			if (token2().equals("do"))
				compileDo();
			if (token2().equals("return"))
				compileReturn();
		}
		indent -= 2;
		addC("</statements>");
	}
	
	private static void compileDo() {
		addC("<doStatement>");
		indent += 2;
		//push do
		pushC();
		//push name
		pushC();
		if (token2().equals(".")){
			pushC();
			pushC();
			pushC();
			compileExpressionList();
			pushC();
		} else {
			pushC();
			compileExpressionList();
			pushC();
		}
		//push ;
		pushC();
		indent -= 2;
		addC("</doStatement>");
	}
	
	private static void compileLet() {
		addC("<letStatement>");
		indent += 2;
		//push let
		pushC();
		//push varname
		pushC();
		if(token2().equals("[")){
			//is array
			pushC();
			compileExpression();
			pushC();
		}
		//pushes equals
		pushC();
		compileExpression();
		//push ;
		pushC();
		indent -= 2;
		addC("</letStatement>");
	}
	
	private static void compileWhile() {
		addC("<whileStatement>");
		indent += 2;
		//push while
		pushC();
		//push (
		pushC();
		compileExpression();
		pushC();
		pushC();
		compileStatements();
		pushC();
		indent -= 2;
		addC("</whileStatement>");
	}
	
	private static void compileReturn() {
		addC("<returnStatement>");
		indent += 2;
		pushC();
		if (token2().equals(";")){
			//nothing
		} else {
			compileExpression();
		}
		// push ;
		pushC();
		indent -= 2;
		addC("</returnStatement>");
	}
	
	private static void compileIf() {
		addC("<ifStatement>");
		indent += 2;
		pushC();
		pushC();
		compileExpression();
		pushC();
		pushC();
		compileStatements();
		pushC();
		if(token2().equals("else")){
			pushC();
			pushC();
			compileStatements();
			pushC();
		}
		indent -= 2;
		addC("</ifStatement>");
	}
	
	private static void compileExpressionList() {
		addC("<expressionList>");
		indent += 2;
			if(token2().equals(")")){
				//nothing
			} else {
				compileExpression();
				while(token2().equals(",")){
					pushC();
					compileExpression();
				}
			}
		indent -= 2;
		addC("</expressionList>");
	}
	
	private static void compileExpression() {
		addC("<expression>");
		indent += 2;
			compileTerm();
			while(	token2().equals("+") ||
					token2().equals("-") ||
					token2().equals("*") ||
					token2().equals("/") ||
					token2().equals("&amp;") ||
					token2().equals("|") ||
					token2().equals("&lt;") ||
					token2().equals("&gt;") ||
					token2().equals("=") ){
				pushC();
				compileTerm();
			}
		indent -= 2;
		addC("</expression>");
	}
	
	private static void compileTerm() {
		addC("<term>");
		indent += 2;
			// intconstant
				//push 1
			// stringconstant
				// " string "
			// keywordconstant
				// true false null this
			// varname
				//push 1
			//varname[array]
				//push 1 then [
			// subroutineCall
				// push 1 then period then call compileSubroutine
			// ( expression )
				//dont know how to do this one?
			// unaryOp term
				// - ~
		
			if (token2().equals("(")){
				//is an expression
				pushC();
				compileExpression();						
				pushC();
			} else if (token2().equals("\"")) {
				//is a string
				pushC();
				pushC();
				pushC();
			} else if ( token2().equals("true")  ||
						token2().equals("flase") ||
						token2().equals("null")  ||
						token2().equals("this")  ){
				//is a keyword constant
				pushC();
			} else if ( token2().equals("~")  ||
    					token2().equals("-")  ){
				//is a unary operator
				pushC();
				compileTerm();
			} else if (token2ahead().equals("[")) {
				//is an array
				pushC();
				pushC();
				compileExpression();
				pushC();
			} else if (token2ahead().equals(".")){
				//is a subroutinecall 
				//compileSubroutine();					////
				pushC();
				pushC();
				pushC();
				pushC();
				compileExpressionList();
				pushC();
			} else {
				//is a integerconstant or varname 
				pushC();
			}
			
		indent -= 2;
		addC("</term>");
	}
	
	public static ArrayList<String> loadFile(String fileName){
	    ArrayList<String> fileContents = new ArrayList<String>();
	    try{                                                        
	       File F = new File(fileName);
	       Scanner S = new Scanner(F);
	       while(S.hasNext()){                                      
	           fileContents.add(S.nextLine());
	       }
	       System.out.println("Successfully Loaded File: "+fileName);
	    } catch (Exception e){
	       System.err.println("File Read Error: " + e.getMessage());
	       System.out.println("File name input was: \""+fileName+"\""); 
	       System.exit(0);
	    }
	    return fileContents;
	}

	public static void writeFile(String fileName, ArrayList<String> input){
		try{
			FileWriter fstream = new FileWriter(fileName);
			BufferedWriter out = new BufferedWriter(fstream);
			for(String x : input){
				out.write(x);
				out.write("\n");
			}
			out.close();
			System.out.println("Successfully Saved File:  "+fileName);
		}catch (Exception e){
			System.err.println("File Write Error: " + e.getMessage());
		}
	}
}


