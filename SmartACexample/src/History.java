
public class History {
	
	public double[] history = new double[6];

	// intermediate function for recording an history entry
	boolean newEntry(double tmp){
		for (int i=5; i>=1; i--){
			history[i] = history[i-1];
		}
		history[0] = tmp;		
		return false;
	}

	double averageSlope(){
		double som=0;
		int nb=0;
		
		for (int i=(6-1); i>0; i--){
			if (history[i] !=0) {
				som += Math.abs(history[i]-history[i-1]);
			    nb++;
			 }
		}
		if (nb!=0) som = som/nb;
		return som;
	}
	
	String  printHistory(){
		String S;
		S = "[";
		for (int i = 0; i<6;i++){
			S = S + history[i] + ",";
		}
		S = S + "]";
		return S;
	}
}