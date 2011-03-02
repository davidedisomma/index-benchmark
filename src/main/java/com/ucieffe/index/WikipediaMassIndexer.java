/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 */
package com.ucieffe.index;

import java.util.Date;

import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.impl.SimpleIndexingProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.impl.FullTextEntityManagerImpl;

import com.ucieffe.model.Text;
import com.ucieffe.util.EntityManagerUtils;

/**
 * Starts a batch operation to rebuild the Lucene index out of the database data.
 * 
 * @author Sanne Grinovero <sanne@hibernate.org>
 * @author Davide Di Somma <davide.disomma@gmail.com>
 */
public class WikipediaMassIndexer {

	public static void main(String[] args) {

		FullTextEntityManager ftEntityManager = new FullTextEntityManagerImpl( EntityManagerUtils.getEntityManagerInstance() );

		MassIndexerProgressMonitor monitor = new SimpleIndexingProgressMonitor( 1500 );
		MassIndexer massIndexer = ftEntityManager.createIndexer( Text.class );
		try {
			massIndexer
					.purgeAllOnStart( true )
					.optimizeAfterPurge( true )
					.optimizeOnFinish( true )
					.batchSizeToLoadObjects( 35 )
					.threadsForSubsequentFetching( 3 )
					.threadsToLoadObjects( 8 )
					.threadsForIndexWriter( 4 )
					.progressMonitor( monitor )
					.cacheMode( CacheMode.IGNORE )
					.startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println( "Ended at: " + new Date() );
		}
	}
}
