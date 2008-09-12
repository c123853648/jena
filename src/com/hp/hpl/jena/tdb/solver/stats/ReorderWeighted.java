/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.tdb.solver.stats;

import java.util.List;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.sparql.core.BasicPattern;

public final class ReorderWeighted extends ReorderPatternBase
{
    private StatsMatcher stats ;

    public ReorderWeighted(StatsMatcher stats)
    {
        this.stats = stats ;
    }
    
    
    @Override
    public void reorder(Graph graph, List<Triple> triples, List<PatternTriple> components, BasicPattern bgp)
    {
        int N = components.size() ;

        for ( int i = 0 ; i < N ; i++ )
        {
            int j = minimum(components) ;
            if ( j < 0 )
                // No weight for any remaining triples 
                j = first(components) ;
            Triple triple = triples.get(j) ; 
            bgp.add(triple) ;
            update(triple, components) ;
            components.set(j, null) ;
        }
    }

    private int minimum(List<PatternTriple> triples)
    {
        int idx = -1 ;
        double w = Double.MAX_VALUE ;
        
        for ( int i = 0 ; i < triples.size() ; i++ ) 
        {
            if ( triples.get(i) == null )
                continue ;
            double w2 = match(triples.get(i)) ;
            if ( w2 < 0 )
            {
                // No match : use an empricial guess.
                // P != rdf;type
                //SP?
                //?PO
            }
            
            
            if ( w2 >= 0 && w > w2 )
            {
                w = w2 ;
                idx = i ;
            }
        }
        return idx ;
    }

    
    private double match(PatternTriple item)
    {
        return stats.match(item.subject, item.predicate, item.object) ;
    }

    
    private int first(List<PatternTriple> triples)
    {
        for ( int i = 0 ; i < triples.size() ; i++ ) 
        {
            if ( triples.get(i) != null )
                return i ;
        }
        return -2 ;
    }


}
/*
 * (c) Copyright 2008 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */