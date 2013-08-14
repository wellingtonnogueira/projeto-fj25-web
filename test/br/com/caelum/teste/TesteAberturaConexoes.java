package br.com.caelum.teste;

import javax.persistence.EntityManager;

import br.com.caelum.financas.infra.JPAUtil;

public class TesteAberturaConexoes {

	/**
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args)  {
		
		try {
			
			System.out.println("inicio");
			
			for (int i = 0; i < 30; i++) {
				System.out.println("ini");
				EntityManager em = new JPAUtil().getEntityManager();
				em.getTransaction().begin();
				
				System.out.println("criado em numero " + i);
				
			}
			
			System.out.println("loop...");
			
			Thread.sleep(30 * 1000);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {
			System.out.println("fim");
		}
	}

}
