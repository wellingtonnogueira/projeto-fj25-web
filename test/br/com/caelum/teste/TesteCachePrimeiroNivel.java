package br.com.caelum.teste;

import javax.persistence.EntityManager;

import br.com.caelum.financas.infra.JPAUtil;
import br.com.caelum.financas.modelo.Conta;

public class TesteCachePrimeiroNivel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManager em = new JPAUtil().getEntityManager();
		
//		Conta c1 = em.getReference(Conta.class, 9);
//		Conta c2 = em.getReference(Conta.class, 9);
		Conta c1 = em.find(Conta.class, 9);
		Conta c2 = em.find(Conta.class, 9);
		
		System.out.println("Titular da primeira conta: " + c1.getTitular());
		System.out.println("Titular da segunda conta: " + c2.getTitular());
		
		em.close();
	}

}
