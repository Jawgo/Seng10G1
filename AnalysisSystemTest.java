import static org.junit.Assert.*;

import org.junit.Test;

public class AnalysisSystemTest {
	
	private static String BASEDIR = "C:\\Users\\Josh\\Desktop\\TestForSeng";
	// The files used for testing were Foo.java and SeeIfWork.java
	@Test
	public void javaLangStringTest(){
		String type = "java.lang.String";
		AnalysisTool aT = new AnalysisTool(BASEDIR, type);
		aT.getFile(aT.getPathName());
		String result = "java.lang.String. Declarations found: 0; references found: 2.";
		assertEquals(result, aT.printResult()); 
	}
	
	@Test
	public void testDeclarations() {
		String type = "DoesItWork";
		AnalysisTool aT2 = new AnalysisTool(BASEDIR, type);
		aT2.getFile(BASEDIR);
		assertEquals(1, aT2.getDCount());
	}
	

	
}
