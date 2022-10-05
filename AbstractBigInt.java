package poo.progetti;

import java.util.Iterator;

import poo.agendina.Nominativo;


public abstract class AbstractBigInt implements BigInt {

	/*
	 * nella struttura dati le cifre vengono salvate in modo tale che la cifra meno significativa sia 
	 * in posizione zero e la cifra pi� significativa in posizione length-1, questo motivato da una 
	 * facilit� nelle operazioni di calcolo, cos� da effettuare le operazioni seguendo l'oridne dal 
	 * meno significativo al pi� significativo.
	 * Di conseguenza il to string restituisce il risultato del metodo reverse dello stringbuilder
	 * che restituisce il contenuto dello stringbuilder con l'ordine invertito.
	 */
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		Iterator<Integer> it = this.iterator();
		while(it.hasNext())
			sb.append(it.next());
		
		sb=sb.reverse();
		while(sb.charAt(0)=='0'&&sb.length()!=1) {
			sb.replace(0, 1, "");
		}
		return sb.toString(); 
	}
	
	
	@Override
	public boolean equals(Object o) {
		if(o==null || !(o instanceof BigInt)) return false;
		if(o==this) return true;
		BigInt b = (BigInt)o;
		if(b.length() != this.length()) return false;
		Iterator<Integer> it1 = this.iterator();
		Iterator<Integer> it2 = b.iterator();
		
		while(it1.hasNext())
			if(it1.next()!=it2.next())
				return false;
		return true;
	}
	
	

	@Override
	public int hashCode() {
		final int M = 31;
		int h=0;
		Iterator<Integer> it = this.iterator();
		while(it.hasNext())
			h=h*M+it.next().hashCode();
		return h;
	}
	
}
