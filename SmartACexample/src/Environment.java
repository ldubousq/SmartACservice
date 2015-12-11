
public class Environment {
	double temperature;
	double increment = 0.3;
	double naturalEntropy= 0.1;
	
	Environment(){
		temperature = 18;
	}

	public  double get_temprature(){
		return temperature;
	}

	public  double set_temperature(double t){
		temperature = t;
		return temperature;
	}

	double change_increment(double inc){
		increment = inc;
		return increment;
	}
	
	double change_entropy(double ne){
		naturalEntropy = ne;
		return naturalEntropy;
	}

	double adjustTemperature(int p){
		temperature -= naturalEntropy;
		if (p == 1) temperature += increment;
		if (p ==-1) temperature -= increment;
		return temperature;
	}
	
	
	

}
