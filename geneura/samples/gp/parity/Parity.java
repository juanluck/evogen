/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package geneura.samples.gp.parity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import geneura.org.GPEvaluator;
import geneura.org.config.Configuration;
import geneura.org.config.Logger;
import geneura.org.individuals.GPIndividual;
import geneura.org.individuals.Individual;
import geneura.org.individuals.tree.IPrimitive;
import geneura.samples.gp.parity.func.*;

/* 
 * Parity.java
 * 
 * Created: Mon Nov  1 15:46:19 1999
 * By: Sean Luke
 */

/**
 * Parity implements the family of <i>n</i>-[even|odd]-Parity problems up 
 * to 32-parity.  Read the README file in this package for information on
 * how to set up the parameter files to your liking -- it's a big family.
 *
 * <p>The Parity family evolves a boolean function on <i>n</i> sets of bits,
 * which returns true if the number of 1's is even (for even-parity) or odd
 * (for odd-parity), false otherwise. 
 *
 <p><b>Parameters</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>data</tt><br>
 <font size=-1>classname, inherits or == ec.app.parity.ParityData</font></td>
 <td valign=top>(the class for the prototypical GPData object for the Parity problem)</td></tr>
 <tr><td valign=top><i>base</i>.<tt>even</tt><br>
 <font size=-1> bool = <tt>true</tt> (default) or <tt>false</tt></font></td>
 <td valign=top>(is this even-parity (as opposed to odd-parity)?)</td></tr>
 <tr><td valign=top><i>base</i>.<tt>bits</tt><br>
 <font size=-1> 2 &gt;= int &lt;= 31</font></td>
 <td valign=top>(The number of data bits)</td></tr>
 </table>

 <p><b>Parameter bases</b><br>
 <table>
 <tr><td valign=top><i>base</i>.<tt>data</tt></td>
 <td>species (the GPData object)</td></tr>
 </table>
 *
 * @author Sean Luke
 * @version 1.0 
 */

public class Parity extends GPEvaluator
    {
    public static final String P_NUMBITS = "bits";
    public static final String P_EVEN = "even";

    public int totalSize;

    public int bits;  // data bits

    public Parity(){
    	setup();
    }

    public Object clone()
        {
        Parity myobj = (Parity) new Parity();
        myobj.setup();
        return myobj;
        }

    public void setup()
        {
        // can't use all 32 bits -- Java is signed.  Must use 31 bits.
        
        if (Configuration.parity_nbits<2){
        	Logger.append("", "Fatal error: the number of bits for Parity must be betweeb 2 and 31 inclusive");
            System.exit(-1);
        }
        
        totalSize = 1;
        for(int x=0;x<Configuration.parity_nbits;x++)
            totalSize *=2;   // safer than Math.pow()
        
        // For the result defining tree
        mPrimitives = new IPrimitive[Configuration.parity_nbits+4];
        
        mPrimitives[0] = new And();
        mPrimitives[1] = new Nand();
        mPrimitives[2] = new Nor();
        mPrimitives[3] = new Or();
        
        if(Configuration.parity_nbits>=2){
        	mPrimitives[4] = new D0();
        	mPrimitives[5] = new D1();
        }
        
        if(Configuration.parity_nbits>=3) mPrimitives[6] = new D2();
        if(Configuration.parity_nbits>=4) mPrimitives[7] = new D3();
        if(Configuration.parity_nbits>=5) mPrimitives[8] = new D4();
        if(Configuration.parity_nbits>=6) mPrimitives[9] = new D5();
        if(Configuration.parity_nbits>=7) mPrimitives[10] = new D6();
        if(Configuration.parity_nbits>=8) mPrimitives[11] = new D7();
        if(Configuration.parity_nbits>=9) mPrimitives[12] = new D8();
        if(Configuration.parity_nbits>=10) mPrimitives[13] = new D9();
        if(Configuration.parity_nbits>=11) mPrimitives[14] = new D10();
        if(Configuration.parity_nbits>=12) mPrimitives[15] = new D11();
        if(Configuration.parity_nbits>=13) mPrimitives[16] = new D12();
        if(Configuration.parity_nbits>=14) mPrimitives[17] = new D13();
        if(Configuration.parity_nbits>=15) mPrimitives[18] = new D14();
        if(Configuration.parity_nbits>=16) mPrimitives[19] = new D15();
        if(Configuration.parity_nbits>=17) mPrimitives[20] = new D16();
        if(Configuration.parity_nbits>=18) mPrimitives[21] = new D17();
        if(Configuration.parity_nbits>=19) mPrimitives[22] = new D18();
        if(Configuration.parity_nbits>=20) mPrimitives[23] = new D19();
        if(Configuration.parity_nbits>=21) mPrimitives[24] = new D20();
        if(Configuration.parity_nbits>=22) mPrimitives[25] = new D21();
        if(Configuration.parity_nbits>=23) mPrimitives[26] = new D22();
        if(Configuration.parity_nbits>=24) mPrimitives[27] = new D23();
        if(Configuration.parity_nbits>=25) mPrimitives[28] = new D24();
        if(Configuration.parity_nbits>=26) mPrimitives[29] = new D25();
        if(Configuration.parity_nbits>=27) mPrimitives[30] = new D26();
        if(Configuration.parity_nbits>=28) mPrimitives[31] = new D27();
        if(Configuration.parity_nbits>=29) mPrimitives[32] = new D28();
        if(Configuration.parity_nbits>=30) mPrimitives[33] = new D29();
        if(Configuration.parity_nbits>=31) mPrimitives[34] = new D30();
        if(Configuration.parity_nbits>=32) mPrimitives[35] = new D31();
     
       }


    public void evaluate(Individual ind)
        {
    	super.evaluate(ind);
        int sum = 0;
        
        for(bits=0;bits<totalSize;bits++)
            {
            int tb = 0;
            // first, is #bits even or odd?
            for(int b=0;b<Configuration.parity_nbits;b++)
                tb += (bits >>> b) & 1;
            tb &= 1;  // now tb is 1 if we're odd, 0 if we're even
            
            ((GPIndividual)ind).getTreeGenome().eval((GPIndividual)ind,this);
            
            int x = ((ParityData)((GPIndividual)ind).problem_data).x;

            if ((Configuration.parity_even && ((x & 1) != tb)) ||
                ((!Configuration.parity_even) && ((x & 1) == tb)))
                sum++;
            }
            
         
        ((GPIndividual)ind).setFitness(totalSize-sum);
        }
    
    public double hit_value() {
    	return 0;
    }
    }
