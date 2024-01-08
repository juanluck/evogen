/*
EvoGen, Evolutionary Geneura is a framework for simulating distributed evolutionary computation experiments
Copyright (C) 2008 Junta de Andalucia CICE project P06-TIC-02025

This file is part of EvoGen.

EvoGen is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

EvoGen is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with EvoGen; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

contact:  http://geneura.ugr.es, https://forja.rediris.es/svn/geneura/evogen
*/
package geneura.org.config;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LoadProperties extends Properties{

	public LoadProperties(String[] pars) {
		try
		{
			this.load(new FileInputStream("config.properties"));
			loadCommandLineDefs( pars );
			//System.err.println(
			//	"ConfigProperties: Command line defs loaded.");
		}
		catch( Exception e )
		{
			System.err.println("ConfigProperties: " + e );
		}
	}
	
	
	
	private void loadCommandLineDefs( String[] cl ) throws IOException {

		StringBuffer sb = new StringBuffer();
		for(int i=0; i<cl.length; ++i) sb.append( cl[i] ).append( "\n" );
		load( new ByteArrayInputStream(sb.toString().getBytes()) );
	}
	
	
	
	
}