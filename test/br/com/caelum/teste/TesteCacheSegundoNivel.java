package br.com.caelum.teste;

import javax.persistence.EntityManager;

import br.com.caelum.financas.infra.JPAUtil;
import br.com.caelum.financas.modelo.Conta;

public class TesteCacheSegundoNivel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EntityManager primeiraEM = new JPAUtil().getEntityManager();
		
		primeiraEM.getTransaction().begin();
		Conta primeiraConta = primeiraEM.find(Conta.class, 9);
		
		primeiraEM.getTransaction().commit();
		primeiraEM.close();
		
		EntityManager segundaEM = new JPAUtil().getEntityManager();
//		segundaEM.getTransaction().begin();
		Conta segundaConta = segundaEM.find(Conta.class, 9);
		
//		segundaEM.getTransaction().commit();
		segundaEM.close();
		
		System.out.println("Titular da primeira conta: " + primeiraConta.getTitular());
		System.out.println("Titular da segunda conta: " + segundaConta.getTitular());
	}

}
