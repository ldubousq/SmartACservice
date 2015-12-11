
public class Simulator {
	Environment env  = new Environment();
	AirConditioner AC= new AirConditioner(env);
	SmartACService smartAC = new SmartACService(AC,env);
	


	public void init (double Troom, int Treq, StepResult res){
		//env. initialization
		env.set_temperature(Troom);
		
		//AC initialization
		AC.requiredTemperature = Treq;
		
		//smartACinitialization
		

		res.currentTemperature = env.get_temprature();
		res.modeAC = AC.getMode();
		res.stateAC = AC.getState();
		res.requiredTemperature = AC.requiredTemperature;
	}
	
	public void startAC() {AC.on();}
	public void stopAC() {AC.off();}
	public boolean startSmartAC(int delay, int tmp) { return smartAC.on(delay,tmp);}
	public boolean stopSmartAC() {return smartAC.off();}
	
	public void step (StepResult res){
        int p=0;
        String H = "";
        
        // Smart AC execute one step
        H = smartAC.step();
        
        // AC execute one step
		p = AC.adjust();
		
		//Environment is executing one step
		env.adjustTemperature(p);

		//simulator is collecting resulting values
		res.currentTemperature = env.get_temprature();
		res.modeAC = AC.getMode();
		res.stateAC = AC.getState();
		res.requiredTemperature = AC.requiredTemperature;
		res.history = H;
	}
}
