
public class AirConditioner{
	Boolean State;
	int Mode;
	int requiredTemperature;
	int observedTemperature;
	Environment env;

	final boolean ON = true;
	final boolean OFF = false;
	final int HEATING = 1;
	final int COOLING = -1;
	final int IDLE = 0;


	AirConditioner(Environment Env){
		env = Env;
		State = OFF;
		Mode = IDLE; 
	}

	private boolean set_requiredTemperature(int t){
		boolean res = true;
		if ((t<=27) && (t>17)) {
			requiredTemperature = t;
		} else
			res = false;
		return res;
	}

	public int getMode(){
		return Mode;
	}

	public boolean getState(){
		return State;
	}
	
	public boolean isACCooling(){
		return ((Mode == COOLING) && (State==ON));
	}

	public boolean isACHeating(){
		return ((Mode == HEATING) && (State==ON));
	}
	public boolean isACIdle(){
		return Mode == IDLE;
	}


	/**
	 * Turn on the device. Should give the required temperature
	 */
	public int on(){
		State = ON;
		adjust();
		return getTemperature();
	}

	public int adjust(){
		// observation
		double observedTemp =  env.get_temprature();

		// Interpretation (if) "and"
		// proposal & decision "then"
		if (State) {
			if (Mode == COOLING && observedTemp <= (requiredTemperature-0.5))	Mode = IDLE;
		    else if (Mode == HEATING && observedTemp >= (requiredTemperature + 0.5)) Mode = IDLE;	
		    else if (Mode == IDLE && observedTemp >= (requiredTemperature + 1))  Mode = COOLING;	
		    else if (Mode == IDLE && observedTemp <= (requiredTemperature - 1))  Mode = HEATING;	
		
		}
		
		return Mode;
	}

	/**
	 * Turn off the device.
	 */
	public void off() {
		State = OFF;
	}


	/**
	 * Set the temperature setting of the air-conditioner to temp celcius.
	 * @param temp temperature setting
	 */
	public void setTemperature(int temp) {
		requiredTemperature=temp;
	}

	public int getTemperature(){
		return requiredTemperature;
	}

}
