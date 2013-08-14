package br.com.caelum.teste;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

import br.com.caelum.financas.infra.JPAUtil;

public class IndexaTagsCadastradas {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		EntityManager em = new JPAUtil().getEntityManager();
		
		FullTextEntityManager fullTextEM = Search.getFullTextEntityManager(em);
		fullTextEM.createIndexer().startAndWait();
	}

}
