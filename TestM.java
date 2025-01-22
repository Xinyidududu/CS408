import org.junit.*;
import static org.junit.Assert.*;

import java.io.PrintStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;

public class TestM {

	private M testObj;
	private final PrintStream originalOut = System.out;
	private ByteArrayOutputStream outContent;

	@Before
	public void setUp() {
		testObj = new M();
		outContent = new ByteArrayOutputStream();
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void tearDown() {
		System.setOut(originalOut);  // 恢复原始 System.out
	}

	@Test
	//for testing 123481011, 12358911, 123678911(node coverage but not edge coverage)
	public void nodeButNoEdge() {
		testObj.m("", 0);
		assertTrue(outContent.toString().contains("zero"));
		outContent.reset();
		testObj.m("a", 1);
		assertTrue(outContent.toString().contains("a"));
		outContent.reset();
		testObj.m("ab", 2);
		assertTrue(outContent.toString().contains("b"));
	}

	//for testing 1358911,13678911,123481011 and 1378911(edge but not edge-pair)
	@Test
	public void edgeButNoEdgePair(){
		outContent.reset();
		testObj.m("", 0);
		assertTrue(outContent.toString().contains("zero"));
		outContent.reset();
		testObj.m("a", 1);
		assertTrue(outContent.toString().contains("a"));
		outContent.reset();
		testObj.m("ab", 2);
		assertTrue(outContent.toString().contains("b"));
		outContent.reset();
		testObj.m("abc", 3);
		assertTrue(outContent.toString().contains("b"));
	}

	//for {[1,2,3,4,8,10,11],[1,2,3,5,8,9,11],
	// [1,2,3,6,7,8,9,11],[1,2,3,7,8,9,11],[1,3,4,8,10,11],
	// [1,3,5,8,9,11],[1,3,6,7,8,9,11], [1,3,7,8,9,11]}
	@Test
	public void edgePairNotPrime(){
		outContent.reset();
		testObj.m("", 0);
		assertTrue(outContent.toString().contains("zero"));
		outContent.reset();
		testObj.m("a", 1);
		assertTrue(outContent.toString().contains("a"));
		outContent.reset();
		testObj.m("ab", 2);
		assertTrue(outContent.toString().contains("b"));
		testObj.m("abc", 3);
		assertTrue(outContent.toString().contains("b"));
		outContent.reset();
		testObj.m("", 0);
		assertTrue(outContent.toString().contains("zero"));
		outContent.reset();
		testObj.m("a", 1);
		assertTrue(outContent.toString().contains("a"));
		outContent.reset();
		testObj.m("ab", 2);
		assertTrue(outContent.toString().contains("b"));
		outContent.reset();
		testObj.m("abc", 3);
		assertTrue(outContent.toString().contains("b"));
	}

	//for {[1,2,3,4,8,10,11],[1,2,3,5,8,9,11],
	// [1,2,3,6,7,8,9,11],[1,2,3,7,8,9,11],[1,3,4,8,10,11],
	// [1,3,5,8,9,11],[1,3,6,7,8,9,11], [1,3,7,8,9,11]}
	@Test
	public void prime(){
		outContent.reset();
		testObj.m("", 0);
		assertTrue(outContent.toString().contains("zero"));
		outContent.reset();
		testObj.m("a", 1);
		assertTrue(outContent.toString().contains("a"));
		outContent.reset();
		testObj.m("ab", 2);
		assertTrue(outContent.toString().contains("b"));
		testObj.m("abc", 3);
		assertTrue(outContent.toString().contains("b"));
		outContent.reset();
		testObj.m("", 0);
		assertTrue(outContent.toString().contains("zero"));
		outContent.reset();
		testObj.m("a", 1);
		assertTrue(outContent.toString().contains("a"));
		outContent.reset();
		testObj.m("ab", 2);
		assertTrue(outContent.toString().contains("b"));
		outContent.reset();
		testObj.m("abc", 3);
		assertTrue(outContent.toString().contains("b"));
	}

	
}
class M {
	public static void main(String [] argv){
		M obj = new M();
		if (argv.length > 0)
			obj.m(argv[0], argv.length);
	}
	
	public void m(String arg, int i) {
		int q = 1;
		A o = null;
		Impossible nothing = new Impossible();
		if (i == 0)
			q = 4;
		q++;
		switch (arg.length()) {
			case 0: q /= 2; break;
			case 1: o = new A(); new B(); q = 25; break;
			case 2: o = new A(); q = q * 100;
			default: o = new B(); break; 
		}
		if (arg.length() > 0) {
			o.m();
		} else {
			System.out.println("zero");
		}
		nothing.happened();
	}
}

class A {
	public void m() { 
		System.out.println("a");
	}
}

class B extends A {
	public void m() { 
		System.out.println("b");
	}
}

class Impossible{
	public void happened() {
		// "2b||!2b?", whatever the answer nothing happens here
	}
}
