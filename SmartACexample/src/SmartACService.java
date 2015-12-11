import java.util.Observable;

public class SmartACService  {
	final boolean ON = true;
	final boolean OFF = false;

	AirConditioner AC;
	Environment env;
	boolean State;
	int delayBeforeACStarting;
	int memorizedTimer = -1;
	int state;

	int numberOfRecordH = 0;
	int numberOfRecordC = 0;
	// indicate respectively how many records there are.
	// a record is the last temp. We only keep the last 10 records.
	History historyHeating = new History();
	History historyCooling =  new History();

	double requiredTemperature;


	SmartACService(AirConditioner a, Environment e){
		AC = a;
		env = e;
		State = OFF;
		requiredTemperature = 19;
	}
	
	boolean get_State(){return State;}


	boolean on(int timer, int requiredTemp){
		// res return the feasibility of the timer w.r.t. what we know about the history.
		boolean res = false;
		double roomTemp = env.get_temprature();

		State = ON;
		AC.setTemperature(requiredTemp);
		requiredTemperature = requiredTemp;

		if (timer==0) {
			memorizedTimer = 0;
			res=((requiredTemp<=(roomTemp+0.5)) && (requiredTemp>=(roomTemp-0.5)));
			//System.out.println("===> smart service ON, with timer 0, temperature OK or not: " + res);
		}
		else {
			memorizedTimer=timer;
			double timeRequired=computeExepctedTimeRequired(requiredTemp);
			res= (timer >= timeRequired); //  is time enough?
			System.out.println("===> smart service ON, with timer " + timer + ", temperature reachable: " + res+ ", time required: " + timeRequired);

			if (!res) { //time is not enough, but let's do as much as possible
				delayBeforeACStarting=0;
			} else{ //compute the delay
				delayBeforeACStarting= (int) (timer - timeRequired);
			}
		}
		

		step();
		return res;
	}

	String step(){
		if (memorizedTimer >=0) memorizedTimer--;
		if (State= ON) {
			if (delayBeforeACStarting > 0){
				delayBeforeACStarting = delayBeforeACStarting -1;
				
				//in case of suddenly changing conditions
				double timeRequired=computeExepctedTimeRequired((int)requiredTemperature);
				if (memorizedTimer>0 && memorizedTimer<= timeRequired) {delayBeforeACStarting = 0;}
			}
			if  (delayBeforeACStarting == 0) {
				AC.on();
				delayBeforeACStarting = -1;
			}
		}
		if (AC.isACCooling() || AC.isACHeating()) {
			recordHistory();
		}

		return " " + State + "  HH=" + historyHeating.printHistory() + "  HC=" + historyCooling.printHistory() ;
	}

	double computeExepctedTimeRequired(int requiredTemperature){
		double delta;
		double nb = 0;

		// delta of temperature to reach
		delta = Math.floor(env.get_temprature()) - requiredTemperature;

		if (env.get_temprature() > requiredTemperature){ 
			//needs to cool, but it is enough to reach t+0.4
			delta = Math.floor(env.get_temprature()) + 0.5 - requiredTemperature -1;
			nb = delta / historyCooling.averageSlope();
		} else if (env.get_temprature() < requiredTemperature){ // needs to heat
			delta = requiredTemperature - (Math.floor(env.get_temprature()) + 0.5) - 1;
			nb = Math.abs(delta) /historyHeating.averageSlope();
		} else {  //noting to do (!!! needs to take the entropy into account)
			nb=0;
		}
		return nb;
	}

	boolean recordHistory(){
		double t = env.get_temprature();

		if (AC.isACCooling()) {
			historyCooling.newEntry(t);
			numberOfRecordC++;
		}
		if (AC.isACHeating()) {
			historyHeating.newEntry(t);
			numberOfRecordH++;
		}
		return true;
	}

	public boolean off() {
		State=OFF;
		AC.off();
		return State;
	}

	public String toString(){
		return " " + State + "  HH=" + historyHeating.printHistory() + "  HC=" + historyCooling.printHistory() ;
	}
}
