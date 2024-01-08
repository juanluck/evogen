/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package geneura.samples.gp.multiplexer;
import geneura.org.Evaluator;
import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.IPrimitive;
import geneura.samples.gp.multiplexer.func.*;

/* 
 * Multiplexer.java
 * 
 * Created: Mon Nov  1 15:46:19 1999
 * By: Sean Luke
 */

/**
 * Multiplexer implements the family of <i>n</i>-Multiplexer problems.
 *
 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>data</tt><br>
 <font size=-1>classname, inherits or == ec.app.multiplexer.MultiplexerData</font></td>
 <td valign=top>(the class for the prototypical GPData object for the Multiplexer problem)</td></tr>
 <tr><td valign=top><i>base</i>.<tt>bits</tt><br>
 <font size=-1>1, 2, or 3</font></td>
 <td valign=top>(The number of address bits (1 == 3-multiplexer, 2 == 6-multiplexer, 3==11-multiplexer)</td></tr>
 </table>

 <p><b>Parameter bases</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>data</tt></td>
 <td>species (the GPData object)</td></tr>
 </table>
 *
 * @author Sean Luke
 * @modified Juan Luis Jiménez Laredo
 * @version 1.0 
 */

public class Multiplexer extends GPEvaluator
    {
    //public static final int NUMINPUTS = 20;
    //public static final String P_NUMBITS = "bits";

    public int bits;  // number of bits in the data
    public int amax; // maximum address value
    public int dmax; // maximum data value
    public int addressPart;  // the current address part
    public int dataPart;     // the current data part
    
    public Multiplexer() {
		setup();
	}
    
    public Object clone()
    {
        Multiplexer myobj =  new Multiplexer();
        myobj.setup();
        return myobj;
    }

    public void setup()
        {
    	bits = Configuration.multiplexer_input_bits;
        
        amax=1;
        for(int x=0;x<bits;x++) amax *=2;   // safer than Math.pow(...)

        dmax=1;
        for(int x=0;x<amax;x++) dmax *=2;   // safer than Math.pow(...)
        
        mPrimitives = new IPrimitive[15];
        
        mPrimitives[0] = new A0();
        mPrimitives[1] = new A1();
        mPrimitives[2] = new A2();
        mPrimitives[3] = new And();
        mPrimitives[4] = new D0();
        mPrimitives[5] = new D1();
        mPrimitives[6] = new D2();
        mPrimitives[7] = new D3();
        mPrimitives[8] = new D4();
        mPrimitives[9] = new D5();
        mPrimitives[10] = new D6();
        mPrimitives[11] = new D7();
        mPrimitives[12] = new If();
        mPrimitives[13] = new Not();
        mPrimitives[14] = new Or();
        
        }

    public void evaluate(Individual ind) {
    	super.evaluate(ind);
        int sum = 0;
            
        for(addressPart = 0; addressPart < amax; addressPart++)
            for(dataPart = 0; dataPart < dmax; dataPart++)
                {
                ((GPIndividual)ind).getTreeGenome().eval((GPIndividual)ind,this);
                
                int x = ((MultiplexerData)((GPIndividual)ind).problem_data).x;
                sum += 1- (                  /* "Not" */
                    ((dataPart >>> addressPart) & 1) /* extracts the address-th 
                                                        bit in data and moves 
                                                        it to position 0, 
                                                        clearing out all 
                                                        other bits */
                    ^                   /* "Is Different from" */		
                    		(x & 1));      /* A 1 if input.x is 
                                            non-zero, else 0. */
                }
            
        ((GPIndividual)ind).setFitness((amax*dmax)-sum);
    }
    

	public double hit_value() {
		return 0;
	}
    }
