package bigint;

/**
 * This class encapsulates a BigInteger, i.e. a positive or negative integer with 
 * any number of digits, which overcomes the computer storage length limitation of 
 * an integer.
 * 
 */
public class BigInteger {

	/**
	 * True if this is a negative integer
	 */
	boolean negative;
	
	/**
	 * Number of digits in this integer
	 */
	int numDigits;
	
	/**
	 * Reference to the first node of this integer's linked list representation
	 * NOTE: The linked list stores the Least Significant Digit in the FIRST node.
	 * For instance, the integer 235 would be stored as:
	 *    5 --> 3  --> 2
	 *    
	 * Insignificant digits are not stored. So the integer 00235 will be stored as:
	 *    5 --> 3 --> 2  (No zeros after the last 2)        
	 */
	DigitNode front;
	
	/**
	 * Initializes this integer to a positive number with zero digits, in other
	 * words this is the 0 (zero) valued integer.
	 */
	public BigInteger() {
		negative = false;
		numDigits = 0;
		front = null;
	}
	
	/**
	 * Parses an input integer string into a corresponding BigInteger instance.
	 * A correctly formatted integer would have an optional sign as the first 
	 * character (no sign means positive), and at least one digit character
	 * (including zero). 
	 * Examples of correct format, with corresponding values
	 *      Format     Value
	 *       +0            0
	 *       -0            0
	 *       +123        123
	 *       1023       1023
	 *       0012         12  
	 *       0             0
	 *       -123       -123
	 *       -001         -1
	 *       +000          0
	 *       
	 * Leading and trailing spaces are ignored. So "  +123  " will still parse 
	 * correctly, as +123, after ignoring leading and trailing spaces in the input
	 * string.
	 * 
	 * Spaces between digits are not ignored. So "12  345" will not parse as
	 * an integer - the input is incorrectly formatted.
	 * 
	 * An integer with value 0 will correspond to a null (empty) list - see the BigInteger
	 * constructor
	 * 
	 * @param integer Integer string that is to be parsed
	 * @return BigInteger instance that stores the input integer.
	 * @throws IllegalArgumentException If input is incorrectly formatted
	 */
	public static BigInteger parse(String integer)
	throws IllegalArgumentException {
		BigInteger num = new BigInteger();
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		while (integer.charAt(0) == ' ')
			integer = integer.substring(1);
		while (integer.charAt(integer.length() - 1) == ' ')
			integer = integer.substring(0, integer.length() - 1);
		if (integer.charAt(0) == '+')
			integer = integer.substring(1);
		if (integer.charAt(0) == '-') {
			num.negative = true;
			integer = integer.substring(1);
		}
		while (integer.length() > 0 && integer.charAt(0) == '0') 
			integer = "" + integer.substring(1);
		
		for (int i = 0; i < integer.length(); i++) {
			if (Character.isDigit(integer.charAt(i))) {
				num.front = new DigitNode(Character.getNumericValue(integer.charAt(i)), num.front);
				num.numDigits++;
			}
			else
				throw new IllegalArgumentException();
		}
		return num; 
	}
	
	/**
	 * Adds the first and second big integers, and returns the result in a NEW BigInteger object. 
	 * DOES NOT MODIFY the input big integers.
	 * 
	 * NOTE that either or both of the input big integers could be negative.
	 * (Which means this method can effectively subtract as well.)
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return Result big integer
	 */
	public static BigInteger add(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		BigInteger ptr1 = first;
		BigInteger ptr2 = second;
		BigInteger sum = new BigInteger();
		int temp = 0;
		boolean overTen = false, underZero = false, opposite, larger = false;
		
		if (ptr1.compareValue(ptr2) > 0)
			larger = true;
		
		if (ptr1.negative && ptr2.negative)
			sum.negative = true;
		else if ((ptr1.negative && !ptr2.negative) && larger)
			sum.negative = true;
		else if ((ptr2.negative && !ptr1.negative) && !larger)
			sum.negative = true;
		
		if ((first.negative && !second.negative) || (!first.negative && second.negative))
			opposite = true;
		else
			opposite = false;
		
		if (opposite) {
			while (ptr1.front != null && ptr2.front != null) {
				if (larger)
					temp = ptr1.front.digit - ptr2.front.digit;
				else
					temp = ptr2.front.digit - ptr1.front.digit;
				
				if (underZero) {
					temp--;
					underZero = false;
				}
				
				if (temp < 0) {
					temp += 10;
					underZero = true;
				}
				
				sum.front = new DigitNode(temp, sum.front);
				System.out.println(sum.toString());
				sum.numDigits++;
				ptr1.front = ptr1.front.next;
				ptr2.front = ptr2.front.next;
			}
		}
		else {
			while (ptr1.front != null && ptr2.front != null) {
				temp = ptr1.front.digit + ptr2.front.digit;
				
				if (overTen) {
					temp++;
					overTen = false;
				}
				
				if (temp > 9)
					overTen = true;
				else
					overTen = false;
				
				sum.front = new DigitNode(temp % 10, sum.front);
				System.out.println(sum.toString());
				sum.numDigits++;
				ptr1.front = ptr1.front.next;
				ptr2.front = ptr2.front.next;
			}
		}
		
		while (ptr1.front != null) {
			if (underZero) {
				if (ptr1.front.digit <= 0)
					sum.front = new DigitNode(ptr1.front.digit + 9, sum.front);
				else {
					sum.front = new DigitNode(ptr1.front.digit - 1, sum.front);
					underZero = false;
				}
			}
			else if (overTen) {
				if (ptr1.front.digit >= 9)
					sum.front = new DigitNode(0, sum.front);
				else {
					sum.front = new DigitNode(ptr1.front.digit + 1, sum.front);
					overTen = false;
				}
			}
			else
				sum.front = new DigitNode(ptr1.front.digit, sum.front);
			
			System.out.println(sum.toString());
			sum.numDigits++;
			ptr1.front = ptr1.front.next;
		}
		while (ptr2.front != null) {
			if (underZero) {
				if (ptr2.front.digit <= 0)
					sum.front = new DigitNode(ptr1.front.digit + 9, sum.front);
				else {
					sum.front = new DigitNode(ptr2.front.digit - 1, sum.front);
					underZero = false;
				}
			}
			else if (overTen) {
				if (ptr2.front.digit == 9)
					sum.front = new DigitNode(0, sum.front);
				else {
					sum.front = new DigitNode(ptr2.front.digit + 1, sum.front);
					overTen = false;
				}
			}
			else
				sum.front = new DigitNode(ptr2.front.digit, sum.front);
			System.out.println(sum.toString());
			sum.numDigits++;
			ptr2.front = ptr2.front.next;
		}
		
		if (overTen) {
			sum.front = new DigitNode(1, sum.front);
			sum.numDigits++;
		}
		
		if (opposite) {
			while (sum.front.digit == 0) {
				sum.front = sum.front.next;
				sum.numDigits--;
			}
		}
		
		System.out.println(sum.toString());
		//reversing list
		DigitNode prev = null;
		DigitNode curr = sum.front;
		DigitNode next = null;
		while (curr != null) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		sum.front = prev;
		
		return sum;
	}
	
	/**
	 * Returns the BigInteger obtained by multiplying the first big integer
	 * with the second big integer
	 * 
	 * This method DOES NOT MODIFY either of the input big integers
	 * 
	 * @param first First big integer
	 * @param second Second big integer
	 * @return A new BigInteger which is the product of the first and second big integers
	 */
	public static BigInteger multiply(BigInteger first, BigInteger second) {
		// following line is a placeholder - compiler needs a return
		// modify it according to need
		BigInteger ptr1 = first;
		BigInteger ptr2temp = second;
		int[] ptr2 = new int[second.numDigits];
		for (int i = 0; i < ptr2.length; i++) {
			ptr2[i] = ptr2temp.front.digit;
			ptr2temp.front = ptr2temp.front.next;
		}
		
		BigInteger prod = new BigInteger();
		BigInteger temp = new BigInteger();
		DigitNode curr = null, prev = null, next = null;
		int added = 0, over = 0, round = 0;
		
		while (ptr1.front != null ) {
			over = 0;
			for (int i = 0; i < ptr2.length; i++) {
				added = (ptr1.front.digit * ptr2[i]) + (over % 10);
				over = (over / 10) + (added / 10);
				
				temp.front = new DigitNode(added % 10, temp.front);
				temp.numDigits++;
				System.out.println("/");
			}
			while (over > 0) {
				temp.front = new DigitNode(over % 10, temp.front);
				over /= 10;
			}
			
			System.out.println(temp.toString());
			
			prev = null;
			next = null;
			curr = temp.front;
			while (curr != null) {
				next = curr.next;
				curr.next = prev;
				prev = curr;
				curr = next;
			}
			temp.front = prev;
			
			System.out.println(temp.toString());
			
			for (int i = 0; i < round; i++)
				temp.front = new DigitNode(0, temp.front);
			
			round++;
			prod = add(prod, temp);
			temp.delete();
			ptr1.front = ptr1.front.next;
		}
		
		if ((first.negative && !second.negative) || (!first.negative && second.negative))
			prod.negative = true;
		
		return prod;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	private int compareValue (BigInteger comparison) {
		if (this.numDigits > comparison.numDigits)
			return 1;
		else if (this.numDigits < comparison.numDigits)
			return -1;
		
		int greater = 0;
		
		BigInteger reverse1 = this;
		BigInteger reverse2 = comparison;
		
		DigitNode prev = null;
		DigitNode curr = reverse1.front;
		DigitNode next = null;
		
		while (curr != null) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		reverse1.front = prev;
		
		prev = null;
		curr = reverse2.front;
		next = null;
		
		while (curr != null) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		reverse2.front = prev;
		
		while (reverse1.front != null && reverse2.front != null) {
			if (reverse1.front.digit > reverse2.front.digit) {
				greater = 1;
				break;
			}
			else if (reverse2.front.digit > reverse1.front.digit) {
				greater = -1;
				break;
			}
			
			reverse1.front = reverse1.front.next;
			reverse2.front = reverse2.front.next;
		}
		
		prev = null;
		curr = reverse1.front;
		next = null;
		
		while (curr != null) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		reverse1.front = prev;
		
		prev = null;
		curr = reverse2.front;
		next = null;
		
		while (curr != null) {
			next = curr.next;
			curr.next = prev;
			prev = curr;
			curr = next;
		}
		reverse2.front = prev;
		
		return greater;
	}
	
	private void delete() {
		while (this.front != null) {
			this.front = this.front.next;
		}
		this.numDigits = 0;
		this.negative = false;
		return;
	}
	
	public String toString() {
		if (front == null) {
			return "0";
		}
		String retval = front.digit + "";
		for (DigitNode curr = front.next; curr != null; curr = curr.next) {
				retval = curr.digit + retval;
		}
		
		if (negative) {
			retval = '-' + retval;
		}
		return retval;
	}
	
}
