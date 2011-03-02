package com.ucieffe.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerUtils {

	private static EntityManagerFactory entityManagerFactory;
	
	public static EntityManager getEntityManagerInstance() {
		
		if (entityManagerFactory == null) {
			entityManagerFactory = Persistence
					.createEntityManagerFactory("wikipedia");
		}
		return entityManagerFactory.createEntityManager();
	}
}
