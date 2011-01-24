package com.ucieffe.index;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.CacheMode;
import org.hibernate.search.MassIndexer;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.impl.FullTextEntityManagerImpl;

import com.ucieffe.model.Text;

public class WikipediaMassIndexer {

	public static void main(String[] args) {

		EntityManagerFactory entityManagerFactory = Persistence
				.createEntityManagerFactory("wikipedia");
		EntityManager entityManager = entityManagerFactory
				.createEntityManager();
		// Text text = entityManager.find(Text.class, 35422446);
		// System.out.println(text.getOldText());
		// System.out.println("Start date:" + new Date());

		FullTextEntityManager ftEntityManager = new FullTextEntityManagerImpl(
				entityManager);
		MassIndexer massIndexer = ftEntityManager.createIndexer(Text.class);
		try {
			massIndexer
					.purgeAllOnStart(true)
					.optimizeAfterPurge(true)
					.optimizeOnFinish(true)
					.batchSizeToLoadObjects(30)
					.threadsForSubsequentFetching(4)
					.threadsToLoadObjects(8)
					.threadsForIndexWriter( 6 )
					.cacheMode(CacheMode.IGNORE)
					.startAndWait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			System.out.println("Stop date:" + new Date());
		}
	}
}
