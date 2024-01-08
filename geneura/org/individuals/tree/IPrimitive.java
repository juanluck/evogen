/* Copyright (c) 2002 The European Commission DREAM Project IST-1999-12679
 *
 * This file is part of JEO.
 * JEO is free software; you can redistribute it and/or modify it under the terms of GNU 
 * General Public License as published by the Free Sortware Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * JEO is distributed in the hope that it will be useful,but WITHOUT ANY WARRANTY; without even the implied 
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for mor details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation, Inc., 59 TEmple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 */
package geneura.org.individuals.tree;

import geneura.org.individuals.GPIndividual;

/**
 * An IPrimitive is a gene which may or may not take arguments and always returns a value.
 * This class gives the interface for such genes.
 * Note that the argument and return type may be of any class in theory,
 * but the current codebase only supports uniformly typed trees. That is, all primitives
 * in a tree must return the same type (currently we support double or boolean), and 
 * all primitives which take arguments must take only arguments of this type.
 * @see java.io.Serializable
 * @author  Mar�a Isabel Garc�a Arenas (Dpto: ATC. Universidad de Granada)
 */

public interface IPrimitive{

    /**
     * Gets the number of arguments required by this primitive.
     * @return Required number of arguments.
     */
    public int getNumArguments();

    /**
     * Is this primitive a terminal?
     * @return true iff getNumArguments() is 0 (a convenience method).
     */
    public boolean isTerminal();

    /**
     * Gets the type of argument required for the given child.
     * @param pIndex Index of argument of which we want to know the type.
     * @return The class of the required type.
     */
    //public Class getArgumentType(int pIndex);

    /**
     * What is the return type for this terminal?
     * @return The class of the return type.
     */
    //public Class getReturnType();

    /**
     * Gets a copy of this primitive for use in tree cloning. To save memory -- unless
     * there is good reason to do otherwise -- implementations should simply return
     * the primitive itself (e.g., using "return this;"). 
     * NOTE: Using references to the same object is safe because before modifying an
     * actual value, we use the IMutablePrimitive interface to getMutableClone().
     * @return a reference this primitive
     * @see jeo.genomes.primitives.IMutablePrimitive
     */
    public IPrimitive getCopy();

    /**
     * Gets a new instance of the given primitive for use in e.g., tree construction.
     * When required (e.g., for RandomDoubleConstant), the instances returned point
     * to separate objects in memory. But to save memory when not required, the 
     * preferred implementation for this method is to simply return a reference to the
     * original primitive.
     * @return A reference to an instance primitive. 
     */
    public IPrimitive getNewInstance();
    
    
    public void eval(GPIndividual individual, Object problem, ITreeNode cNode);
}