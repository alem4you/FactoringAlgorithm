import java.math.BigInteger;
import java.util.ArrayList;


public class TableElement {

	BigInteger r;
	BigInteger rSquared;
	Integer[] expon = new Integer[Factoring.NUM_PRIMES];
	boolean out = false;
	
	
	public TableElement(BigInteger r, ArrayList<Integer> primes) {
		
		// PRELIMINARY OPERATIONS
		
		for(int i=0;i<Factoring.NUM_PRIMES;i++) {
			expon[i]=0;
		}
		
		// COMPUTE R, R^2
		this.r=r;
		this.rSquared=r.pow(2).mod(Factoring.BASE);
		
		// COMPUTE SCOMPOSITION
		
		BigInteger number = rSquared;
		for(int i=0;i<primes.size() && !r.equals(BigInteger.ONE); i++) {
			
			while(number.mod(new BigInteger(Integer.toString(primes.get(i))))==BigInteger.ZERO) {
				
				number=number.divide(new BigInteger(Integer.toString(primes.get(i))));
				expon[i]++;
			}
						
		}
		
		if(!number.equals(BigInteger.ONE)) {
			out=true;
		}
		
		
	}
	
	
	public boolean sameFactorBase(TableElement num) {
		
		
		for(int i=0;i<Factoring.NUM_PRIMES;i++) {
			
			if(this.expon[i]%2 != num.expon[i]%2) {
				return false;
			}
			
		}
		return true;
		
		
	}
	
	public String getString() {
		
		String back = new String("");
		
		for(int i=0;i<Factoring.NUM_PRIMES-1;i++) {
			
			back = back + expon[i]%2 + " ";
			
		}
		
		back = back + expon[Factoring.NUM_PRIMES-1];
		
		return back;
		
		
		
	}
	
	
	
}
