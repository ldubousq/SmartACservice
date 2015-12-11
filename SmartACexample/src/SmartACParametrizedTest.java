import static org.junit.Assert.*;


import java.util.Arrays;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class SmartACParametrizedTest {




	private int d; 
	 StepResult  res = new StepResult();
	 Simulator  sim = new Simulator();
	
	private int inputNumber1; 

	@Rule 
	public TestName name = new TestName();


	@Before
	public  void Anim1() {

		sim.init(19,21,res);
		sim.startAC();
		assertTrue("heating condition init", res.checkStepResult_heating());
		assertTrue("cooling conditon init", res.checkStepResult_cooling());
		assertTrue("state consitency init", res.checkStepResult_ACstateConsistency());
		assertTrue("req temp consistency init", res.checkStepResult_requiredTemperatureConsistency());		


		for (int i=1; i<=20 ; i++) {
			sim.step(res);
			//res.printStep();

			assertTrue("heating condition step " + i, res.checkStepResult_heating());
			assertTrue("cooling conditon step " + i, res.checkStepResult_cooling());
			assertTrue("state consitency step " + i, res.checkStepResult_ACstateConsistency());
			assertTrue("req temp consistency step " + i, res.checkStepResult_requiredTemperatureConsistency());				
		}

		System.out.println( "====================== ");
		sim.env.change_entropy(-0.05);
		for (int i=1; i<=100 ; i++) {
			sim.step(res);
			//res.printStep();
			assertTrue("heating condition step " + i, res.checkStepResult_heating());
			assertTrue("cooling conditon step " + i, res.checkStepResult_cooling());
			assertTrue("state consitency step " + i, res.checkStepResult_ACstateConsistency());
			assertTrue("req temp consistency step " + i, res.checkStepResult_requiredTemperatureConsistency());			
		}

		sim.stopAC();
	}


	@Parameterized.Parameters(name="[jeu de test#{index} delai = {0}]")
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][] {
			{1}, {2}, {5}, {6}, {10}, {11}, {12}, {15}	});
	}

	public SmartACParametrizedTest(int input1){
		this.inputNumber1 = input1;
	}


	public void ParametrizedTest(int d){

		boolean verdict;
		int expectedTemp = 19;
		int currentTemp = 21;

		System.out.println("");
		System.out.println("*****************************************************************");
		System.out.println("** expected temp "+ expectedTemp + "  current temp " + currentTemp);

		//  room temp, expected temp 
		sim.init(currentTemp,expectedTemp,res);
		verdict = sim.startSmartAC(d, expectedTemp);
		System.out.println("** System answer is "+ verdict + " with timer " + d);
		for (int i=1; i<=d ; i++) {
			sim.step(res);
			res.printStep();

		}
		sim.stopSmartAC();
		double tmp = sim.env.get_temprature();
		System.out.println("** Final temp is "+ tmp);
 
		// if the AC told goal was reachable, it should have been reached
		if (verdict) {
			assertTrue("expectedTemp=" + expectedTemp + " real on after stepping= "+tmp, tmp >= (expectedTemp-0.5) && tmp <= (expectedTemp+0.5));
		}
		else { // The AC told the goal was not reachable
			assertTrue("expectedTemp=" + expectedTemp + " real on after stepping= "+tmp, !(tmp >= (expectedTemp-0.5) && tmp <= (expectedTemp+0.5)));
		}
	}

	@Test
	public void test1() {
		// given

		// when
		ParametrizedTest(this.inputNumber1);
	}

}