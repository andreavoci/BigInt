package poo.progetti;

import java.util.Iterator;


public interface BigInt extends Comparable<BigInt>,Iterable<Integer>{
	
	
	//ritorna il valore del BigInt sottoforma di stringa di caratteri
	default String value() {
		StringBuilder sb = new StringBuilder();
		Iterator<Integer> it = this.iterator();
		while(it.hasNext()) {
			int c = it.next();
			sb.append(c);
		}
		return sb.toString();
	}
	
	//ritorna il numero di cifre di questo BigInt
	default int length() { 
		Iterator<Integer> it = this.iterator();
		int count = 0;
		while(it.hasNext()) {
			it.next();
			count++;
		}
		return count;
	}
	
	/*Questo metodo costruisce un BigInt a partire da un valore stringa, solleva un'eccezione se nella stringa risultano
	 * caratteri che non siano numeri.
	 * Creata appositamente per poter implementare nell'interfaccia i metodi che eseguono le operazioni in quanto � possibile 
	 * costruire una stringa di lunghezza variabile mentre con l'intero vi � il limite sul numero di caratteri
	 * 
	 */
	BigInt factory(String s);
	
	//costruisce un BigInt a partire da un valore int; Solleva un�eccezione se l�intero � negativo.
	BigInt factory( int x );
	
	
	default BigInt incr() {
		BigInt dummyOne = this.factory(1);
		return add(dummyOne);
	}
	
	//eccezione se this � zero
	default BigInt decr() {
		BigInt dummyZero = this.factory(0);
		if(this.equals(dummyZero)) throw new IllegalStateException();
		BigInt dummyOne = this.factory(1);
		return sub(dummyOne);
	}
	
	default BigInt add( BigInt x ) {
		StringBuilder sb = new StringBuilder();
		int s=0;
		
		Iterator<Integer> it1=this.iterator(),it2=x.iterator();
		//quando l'hasnext di entrambi � false, quindi entrambi i numeri sono stati analizzati si esce dal while
		while(it1.hasNext()||it2.hasNext()) { 
			if(it1.hasNext())
				s += it1.next();

			if(it2.hasNext())
				s += it2.next();
			sb.append(s%10);
			
			s /= 10;
		}
		
		if(s!=0) //se vi � un ulteriore riporto si aggiunge anche quello
			sb.append(s);
		
		return factory(sb.reverse().toString());
	}
	
	//ritorna un BigInt con la differenza tra this e d; atteso this>=d
	default BigInt sub( BigInt x ) {
		if(this.compareTo(x)<0) throw new IllegalArgumentException();
		
		StringBuilder sb = new StringBuilder();
		
		int p = 0;
		int d = 0;
		
		Iterator<Integer> it1=this.iterator(),it2=x.iterator();
		//quando l'hasnext di entrambi � false, quindi entrambi i numeri sono stati analizzati si esce dal while
		while(it1.hasNext()||it2.hasNext()) { 
			if(it1.hasNext())
				d = it1.next();

			if(it2.hasNext())
				d -= it2.next();
			d-=p; // sottrae il prestito che sar� 1 se la cifra di this � < di x,altrimenti 0 se > o =
			if(d<0) {
				p = 1;
				sb.append(d+10);
			}
			else {
				p=0;
				sb.append(d);
			}
		}
		
		
		return factory(sb.reverse().toString());
	}
	
	default BigInt mul( BigInt x ) {
		StringBuilder sb = new StringBuilder();
		int n=0; // cifra attuale di this che viene moltiplicata per tutte le cifre di x
		int m=1;
		int r=0;//riporto
		
		int count = 0; // contatore delle somme che vanno fatte per ogni livello della moltiplicazione
		BigInt sum = factory(0); //bigint dummy per contenre la somma iterativa 
		
		Iterator<Integer> it1=this.iterator(),it2; //it2 non � inizializzato in quanto verr� analizzato tutto per ogni cifra di this
		
		//quando l'hasnext di entrambi � false, quindi entrambi i numeri sono stati analizzati si esce dal while
		while(it1.hasNext()) { 
			n = it1.next(); // cifra attuale di this che viene moltiplicata per tutte le cifre di x
			m=1;
			r=0;
			it2 = x.iterator();
			
			while(it2.hasNext()) {
				m = n * it2.next();
				m+=r;
				sb.append(m%10);
				r = m/10;
				m = 1;
			}
			if(r!=0) //se vi � un ulteriore riporto si aggiunge anche quello
				sb.append(r);

			sb = sb.reverse();
			for(int i=0;i<count;i++) {
				sb.append(0);
			}
			count++;
			sum = sum.add(factory(sb.toString()));
			sb = new StringBuilder();
		}
		return sum;
	}
	

	//ritorna il quoziente della divisione intera tra this e d; atteso this>=d
	default BigInt div( BigInt x ){
		if(this.compareTo(x)<0) throw new IllegalArgumentException();
		
		StringBuilder sb = new StringBuilder();
		
		//Inversione cifre in quanto la divisione va sviluppata dalla cifra pi� significativa
		BigInt a = factory(this.value());
		BigInt pro = x;
		
		int count = 1;
		int n = 0;
		Iterator<Integer> it=a.iterator();
		StringBuilder dividendo = new StringBuilder();
		while(it.hasNext()) { 
			dividendo.append(it.next()); //porzione del dividendo che pu� essere usato nell'operazione

			BigInt sel = factory(dividendo.toString()); //bigint della cifra selezionata
			if(sel.compareTo(x)>=0) {
				pro = x;
				while(true) {
					pro = x.mul(factory(count+1));
					if(pro.compareTo(sel)<=0)
						count++;
					else
						break;
				}

				sb.append(count);
				BigInt resto = sel.sub(x.mul(factory(count)));
				count=1;
				dividendo = new StringBuilder();

				if(resto.value() != "0") {
					dividendo.append(resto.value());
					dividendo = dividendo.reverse();
				}
			}
		}
		return factory(sb.toString());	
	}
	
	//ritorna il resto della divisione intera tra this e d; atteso this>=d
	default BigInt rem( BigInt x ) { 
		if(this.compareTo(x)<0) throw new IllegalArgumentException();

		BigInt div = this.div(x);
		BigInt mol = div.mul(x);
		
		return this.sub(mol);
	}
	
	
	//calcola la potenza this^exponent
	default BigInt pow( int exponent ) {
		if(exponent == 0) {
			return this.factory(1);
		}
		if(exponent == 1) {
			return this;
		}

		BigInt res = this.mul(this);
		for(int i=2; i<exponent; i++) {
			res = res.mul(this);
		}
		return res;
	}
	
}//BigInt
