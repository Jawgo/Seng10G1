import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;

import org.junit.Test;

public class AnalysisUnitTest {
	private static String BASEDIR = "C:\\Users\\Josh\\Desktop\\TestForSeng";
	
	@Test(expected = NullPointerException.class)
	public void nullPointerPath() {
		AnalysisTool aT = new AnalysisTool("C://asdasda", "String");
		aT.getFile(aT.getPathName());
	}
	
	@Test
	public void testPathConstructor() {
		AnalysisTool aT = new AnalysisTool(BASEDIR, "String");
		assertEquals(BASEDIR, aT.getPathName());
	}
	
	@Test
	public void  nullFromNonExistentFile() {
		AnalysisTool aTool = new AnalysisTool(BASEDIR, "String");
		char[] charTool = aTool.fileToChar("lol.txt");
		assertEquals(charTool, null);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTooManyArguments() {
		AnalysisTool.main(new String[]{BASEDIR,"java.lang.String","String"});
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testTooLittleArguments() {
		AnalysisTool.main(new String[]{BASEDIR});
	}
}
