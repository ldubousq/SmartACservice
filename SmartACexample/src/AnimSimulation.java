import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;



public class AnimSimulation {
	static StepResult  res = new StepResult();
	static Simulator  sim = new Simulator();
	@Rule 
	public TestName name = new TestName();


	@BeforeClass
	public static void Anim1() {

		sim.init(19,21,res);
		sim.startAC();
		assertTrue("heating condition init", res.checkStepResult_heating());
		assertTrue("cooling conditon init", res.checkStepResult_cooling());
		assertTrue("state consitency init", res.checkStepResult_ACstateConsistency());
		assertTrue("req temp consistency init", res.checkStepResult_requiredTemperatureConsistency());		


		for (int i=1; i<=20 ; i++) {
			sim.step(res);
			res.printStep();

			assertTrue("heating condition step " + i, res.checkStepResult_heating());
			assertTrue("cooling conditon step " + i, res.checkStepResult_cooling());
			assertTrue("state consitency step " + i, res.checkStepResult_ACstateConsistency());
			assertTrue("req temp consistency step " + i, res.checkStepResult_requiredTemperatureConsistency());				
		}

		System.out.println( "====================== ");
		sim.env.change_entropy(-0.05);
		for (int i=1; i<=100 ; i++) {
			sim.step(res);
			res.printStep();
			assertTrue("heating condition step " + i, res.checkStepResult_heating());
			assertTrue("cooling conditon step " + i, res.checkStepResult_cooling());
			assertTrue("state consitency step " + i, res.checkStepResult_ACstateConsistency());
			assertTrue("req temp consistency step " + i, res.checkStepResult_requiredTemperatureConsistency());			
		}

		sim.stopAC();
	}


	@Test
	public void Anim2_Delay0_temperatureEquals() {
		//  room temp, expected temp 
		sim.init(19,21,res);

		boolean res = sim.startSmartAC(0, 19);
		sim.stopSmartAC();
		assertTrue(name.getMethodName(),res);
	}

	@Test
	public void Anim2_Delay0_temperatureMinus1() {
		//  room temp, expected temp 
		sim.init(19,21,res);

		boolean res = sim.startSmartAC(0, 18);
		sim.stopSmartAC();
		assertTrue(name.getMethodName(),res);
	}

	@Test
	public void Anim2_Delay0_temperaturePlus1() {
		//  room temp, expected temp 
		sim.init(19,21,res);

		boolean res = sim.startSmartAC(0, 20);
		sim.stopSmartAC();
		assertTrue(name.getMethodName(),res);
	}

	@Test
	public void Anim2_Delay0_temperatureMinus2() {
		//  room temp, expected temp 
		sim.init(21,19,res);
		boolean res = sim.startSmartAC(0, 19);
		sim.stopSmartAC();
		assertFalse(name.getMethodName(),res);
	}

	@Test
	public void Anim2_Delay0_temperaturePlus2() {
		//  room temp, expected temp 
		sim.init(21,19,res);
		boolean res = sim.startSmartAC(0, 23);
		sim.stopSmartAC();
		assertFalse(name.getMethodName(),res);
	}

	@Test
	public void Anim2_PlayWithDelay2() {
		boolean verdict;
		//  room temp, expected temp 
		sim.init(21,19,res);
		for (int d=1; d<=10; d++){
			verdict = sim.startSmartAC(d, 23);
			sim.stopSmartAC();
			assertFalse(name.getMethodName() + d,verdict);
		}
	}

	@Test
	public void Anim2_PlayWithDelay3() {
		boolean verdict;
		int expectedTemp = 19;
		int currentTemp = 21;

		System.out.println("");
		System.out.println("**********************************");
		System.out.println("** Time should be enough, and needs to cool the room");
		System.out.println("expected temp "+ expectedTemp + "  current temp " + currentTemp);

		//  room temp, expected temp 
		sim.init(currentTemp,expectedTemp,res);
		int d = 11;
		verdict = sim.startSmartAC(23, expectedTemp);
		for (int i=1; i<=d ; i++) {
			sim.step(res);
			res.printStep();

		}
		sim.stopSmartAC();
		double tmp = sim.env.get_temprature();

		// if the AC told goal was reachable, it should have been reached
		if (verdict) {
				assertTrue("expectedTemp=" + expectedTemp + " real on after stepping= "+tmp, tmp >= (expectedTemp-1) && tmp <= (expectedTemp+1));
		}
		else { // The AC told the goal was not reachable
			assertTrue(name.getMethodName() + d, !(tmp >= (expectedTemp-1) && tmp <= (expectedTemp+1)));
		}


	}
}

