package poo.progetti;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import java.math.BigInteger;

public class BigIntLL extends AbstractBigInt{


	/*
	 * nella struttura dati le cifre vengono salvate in modo tale che la cifra meno significativa sia 
	 * in posizione zero e la cifra pi� significativa in posizione length-1, questo motivato da una 
	 * facilit� nelle operazioni di calcolo, cos� da effettuare le operazioni seguendo l'oridne dal 
	 * meno significativo al pi� significativo.
	 */
	private LinkedList<Integer> number = new LinkedList<>();
	
	
	public BigIntLL(int n) {
		if(n==0) this.number.add(0);
		while(n>0) {
			this.number.add(n%10);
			n /= 10;
		}
	}
	
	public BigIntLL(String s) {

		if(!s.matches("[0-9]*")) throw new IllegalArgumentException();
		for(int i=s.length()-1; i>=0; i--) {
			this.number.add(Character.getNumericValue(s.charAt(i)));
		}
	}

	@Override
	public BigInt factory(int x) {
		if(x<0) throw new IllegalArgumentException();
		return new BigIntLL(x);
	}
	
	@Override
	public BigInt factory(String s) {
		return new BigIntLL(s);
	}


	@Override
	public Iterator<Integer> iterator() {
		return number.iterator();
	}
	
	//rimozione degli 0 in coda (piu significativi) in quanto non hanno peso
	private void removeZero() {
		while(this.number.getLast()==0 && this.number.size()!=1) { 
			this.number.removeLast();
		}
	}
	
	@Override
	public int compareTo(BigInt o) {
		// stringa del valore dell' oggetto invertito in quanto il costruttore per mantenere l'ordine delle 
		//cifre significative lo va ad invertire a sua volta
		String val = new StringBuilder(o.value()).reverse().toString();
		BigIntLL bi = new BigIntLL(val);
		
		bi.removeZero();this.removeZero();
		
		if(this.length()>bi.length()) {
			return 1;
		}
		if(this.length()<bi.length()) {
			return -1;
		}
		//for dalla cifra pi� significativa alla meno significativa cosi alla prima disuguaglianza � chiaro l'ordinamento
		for(int i=this.length()-1;i>=0;i--) 
			if(this.number.get(i) != bi.number.get(i)) 
				return this.number.get(i)-bi.number.get(i);

		return 0;
	}
	
	public static void main(String[] args) {
		BigInt n0 = new BigIntLL(2);
		BigInt nres = n0.pow(128);
		System.out.println("2^128="+nres);
		
		BigInteger b=new BigInteger("2");
		BigInteger p=b.pow( 128 );
		System.out.println("2^128="+p);
	}
}
