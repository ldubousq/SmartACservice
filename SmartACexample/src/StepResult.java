public class StepResult {


	final boolean ON = true;
	final boolean OFF = false;
	final int HEATING = 1;
	final int COOLING = -1;
	final int IDLE = 0;

	public double requiredTemperature = 20;
	public double currentTemperature = 0;
	public int modeAC = IDLE;
	public boolean stateAC= ON;
	public String history="";


	void printStep(){
		String m="";
		String s="";

		if (modeAC==HEATING) m="Heating";
		if (modeAC==COOLING) m="Cooling";
		if (modeAC==IDLE) m="Idle";

		if (stateAC) s="AC is On"; else s="AC is Off";

		System.out.println(currentTemperature + " " + s + " and in state " + m );
		System.out.println(" --- " + s + " --- " + history );


	}


	/*•	is_on and (required_temp > room_temp) => heating
		•	is_on and (required_temp < room_temp) => cooling
		•	is_on and (required_temp = room_temp) => idle
		•	is_off and not (sleeping or cooling or heating)
		•	required_temp > 17 and required_temp < 27 (assumed thanks to the appliance limitations)
	 */

	boolean checkStepResult_heating(){
		return ( !(stateAC && (requiredTemperature > (currentTemperature+2))) || (modeAC==HEATING)) ;
	}

	boolean checkStepResult_cooling(){
		return ( !(stateAC && (requiredTemperature < (currentTemperature-2))) || (modeAC==COOLING)) ;
	}

	boolean checkStepResult_ACstateConsistency(){
		return ((modeAC==HEATING) || (modeAC==COOLING) ||(modeAC==IDLE));
	}

	boolean checkStepResult_requiredTemperatureConsistency(){
		return ((requiredTemperature>=17) && (requiredTemperature<=27));
	}
	

	/* 
	 * What to check on the SmartService?
	 * if the service said it will be able to do it, then it should be on schedule (no false positive)
	 * Similarly, if it said it won't, well it should not managed it (non false negative)
	 * the service is really good, if it is just on time (= there are no more than xxx heating/cooling at the end of the timer)
	 * if it start cooling/heating, there is no idle part. 
	 */
}