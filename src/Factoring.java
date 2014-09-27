import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigInteger;
import java.util.ArrayList;


public class Factoring {


	public static final int NUM_PRIMES = 100;
	public static final int NUM_ELEMENTS_TABLE = NUM_PRIMES + 10;

	public static final BigInteger BASE = new BigInteger("91967379518938584281857");


	public static ArrayList<Integer> primeGenerator() {

		ArrayList<Integer> primes = new ArrayList<Integer>();

		int x, y = 0;
		for( x = 2; x<10E8 && primes.size()<NUM_PRIMES ; x++ )	{
			if( x % 2 != 0 || x == 2 ) {
				for( y = 2; y <= x / 2; y++ ) {
					if( x % y == 0 )
						break;	
				}

				if( y > x / 2 )	{
					primes.add(x);
				}
			}
		}

		return primes;
	}


	public static BigInteger squareRoot(BigInteger x) {
		BigInteger right = x, left = BigInteger.ZERO, mid;
		while(right.subtract(left).compareTo(BigInteger.ONE) > 0) {
			mid = (right.add(left)).shiftRight(1);
			if(mid.multiply(mid).compareTo(x) > 0)
				right = mid;
			else
				left = mid;
		}
		return left;
	} 



	public static void main(String args[]) throws IOException, InterruptedException {

		long startTime = System.currentTimeMillis();

		// START GENERATING PRIMES
		ArrayList<Integer> primes = primeGenerator();
		System.out.println("Generated factorbase --> " + primes);
		// FINISH GENERATING PRIMES


		/* Example out of the range
		TableElement test = new TableElement(new BigInteger("184"));
		 */

		/* Example two numbers same rapresentation
		TableElement test = new TableElement(new BigInteger("395"));
		TableElement test2 = new TableElement(new BigInteger("261"));
		System.out.println(test.sameFactorBase(test2));
		 */

		// STARTING GENERATING THE TABLE
		ArrayList<TableElement> table = new ArrayList<TableElement>();


		for(int k=3;k<10000000 && table.size()<NUM_ELEMENTS_TABLE;k++) {

			for(int j=2;j<10 && table.size()<NUM_ELEMENTS_TABLE;j++) {

				// computing r
				BigInteger numAspirant;

				numAspirant=new BigInteger(Integer.toString(k)).multiply(BASE);
				numAspirant=squareRoot(numAspirant);
				numAspirant=numAspirant.add(new BigInteger(Integer.toString(j)));

				TableElement elemAspirant = new TableElement(numAspirant, primes);
				boolean notValid=false;

				if(!elemAspirant.out) {

					for(int i=0;i<table.size();i++) {

						if(table.get(i).sameFactorBase(elemAspirant)) {
							notValid=true;
							System.out.println("Not valid found");
							break;
						}

					}

					if(!notValid) {
						
						

						table.add(elemAspirant);
						System.out.println("ADDED --> " + elemAspirant.rSquared + "ELEMENT # --> " + table.size());
					}

				}

			}

		}



		// FINISHED GENERATING THE MATRIX NOW WE HAVE ALL THE ELEMENTS	



		// PREPARING FILE
		System.out.println("Starting writing the file");
		FileOutputStream input = new FileOutputStream("input.txt");
		PrintStream write = new PrintStream(input);

		write.println(NUM_ELEMENTS_TABLE + " " + NUM_PRIMES);

		for(TableElement c : table) {

			write.println(c.getString());

		}

		write.close();
		System.out.println("Finished to write the file");
		// END PREPARING FILE


		// RUNNING GAUSS BIN
		System.out.println("Starting GaussBin.exe and waiting its termination");
		Process process = Runtime.getRuntime ().exec ("GaussBin.exe input.txt output.txt");

		Thread.sleep(5000);
		// GAUSS BIN ENDS



		// STARTING READING THE SOLUTIONS

		FileReader f = new FileReader("output.txt");
		BufferedReader reader = new BufferedReader(f);

		Integer solNum = Integer.parseInt(reader.readLine());

		for(int h=0;h<solNum;h++) {


			//READING A SOLUTION

			String solution = reader.readLine();

			Integer solVector[] = new Integer[NUM_ELEMENTS_TABLE];


			// STARTING PARSING THE SOLUTION STRING

			for(int i=0;i<NUM_ELEMENTS_TABLE;i++) {

				solVector[i]=Integer.parseInt(Character.toString(solution.charAt(i*2)));

			}

			// STARTING CALCULATING LEFT
			BigInteger left = new BigInteger("1");

			for(int i=0;i<NUM_ELEMENTS_TABLE;i++) {

				if(solVector[i]==1) {

					left=left.multiply(table.get(i).r);

				}


			}
			left=left.mod(BASE);
			// FINISH CALCULATING LEFT

			// STARTING CALCULATING RIGHT

			BigInteger right = new BigInteger("1");
			Integer[] expon = new Integer[NUM_PRIMES];
			for(int m=0;m<NUM_PRIMES;m++) {
				expon[m]=0;
			}

			for(int i=0;i<NUM_ELEMENTS_TABLE;i++) {

				if(solVector[i]==1) {

					for(int m=0;m<NUM_PRIMES;m++) {

						expon[m]=expon[m]+table.get(i).expon[m];

					}

				}

			}

			for(int m=0;m<NUM_PRIMES;m++) {

				expon[m]=expon[m]/2;

			}

			for(int m=0;m<NUM_PRIMES;m++) {

				BigInteger n = new BigInteger(Integer.toString(primes.get(m))).pow(expon[m]);
				right=right.multiply(n);

			}
			right=right.mod(BASE);


			BigInteger value = right.subtract(left).abs().gcd(BASE);
			if((!value.equals(BigInteger.ONE)) && !value.equals(BASE)) {
				
				
				System.out.println();
				System.out.println("Solution found!" + "The factors of "+ BASE +" are " + value + " and " + BASE.divide(value));
				break;
			}

			
		}

		
	    long endTime = System.currentTimeMillis();

	    System.out.println("CPU-time " + (endTime - startTime));
	    
	    
	}




}
