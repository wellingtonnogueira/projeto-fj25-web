package br.com.caelum.teste;

import java.util.List;

import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.infra.JPAUtil;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Gerente;


public class TesteGerente {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		Gerente gerente = new Gerente();
		gerente.setNome("Gerente " + System.currentTimeMillis() % 10000);
		gerente.setTelefone("(11) 9 8765 1234");
		
		gerente.setRua("Rua do Gerente, sn");
		gerente.setCidade("São Paulo");
		gerente.setEstado("SP");
		
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();

		em.persist(gerente);
		
		ContaDAO dao = new ContaDAO(em);
		
		List<Conta> contas = dao.lista();		
		int id = (int)(Math.random() * contas.size());
		Conta conta = contas.get(id);
		conta.setGerente(gerente);
		
		Conta conta2 = new Conta();
		conta2.setNumero("234");
		conta2.setTitular("teste");
		conta2.setBanco("274");
		conta2.setAgencia("1222");

		em.persist(conta2);
		
		em.getTransaction().commit();
		
		em.close();
		
	}

}
