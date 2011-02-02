package com.ucieffe.index;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.batchindexing.MassIndexerProgressMonitor;
import org.hibernate.search.impl.SimpleIndexingProgressMonitor;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.impl.FullTextEntityManagerImpl;

import com.ucieffe.model.Text;

public class WikipediaMassIndexer {

	public static void main(String[] args) {

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory( "wikipedia" );
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		FullTextEntityManager ftEntityManager = new FullTextEntityManagerImpl( entityManager );

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
