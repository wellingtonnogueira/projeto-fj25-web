package br.com.caelum.financas.mb;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.infra.JPAUtil;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Gerente;

@ViewScoped
@ManagedBean
public class ContasBean {
	private Conta conta = new Conta();
	private List<Conta> contas = new ArrayList<>();

	public void grava() {
		System.out.println("Gravando a conta");
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		ContaDAO dao = new ContaDAO(em);
		
		if(conta.getId() == null) {
			Gerente gerenteTosco = new Gerente();
			
			gerenteTosco.setCidade("Sampa");
			gerenteTosco.setEstado("SP");
			gerenteTosco.setNome("Caboclo");
			gerenteTosco.setRua("R. Zero");
			gerenteTosco.setTelefone("01123453242");
			
			em.persist(gerenteTosco);
			
			conta.setGerente(gerenteTosco);
			
			dao.adiciona(conta);
		} else {
			dao.altera(conta);
		}
		contas = dao.lista();
		
		em.getTransaction().commit();
		
		limpaFormularioDoJSF();

		em.close();
		
	}

	public void remove() {
		System.out.println("Removendo a conta");
		EntityManager em = new JPAUtil().getEntityManager();
		em.getTransaction().begin();
		
		ContaDAO dao = new ContaDAO(em);
		
		Conta aRemover = dao.busca(this.conta.getId());
		
		em.remove(aRemover);
		
		em.getTransaction().commit();
		
		em.close();
		
		limpaFormularioDoJSF();
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public List<Conta> getContas() {
		System.out.println("Listando as contas");
		if(contas != null) {
			EntityManager em = new JPAUtil().getEntityManager();
			ContaDAO dao = new ContaDAO(em);
			contas = dao.lista();
			
			em.close();
		}

		return contas;
	}

	/**
	 * Esse método apenas limpa o formulário da forma com que o JSF espera.
	 * Invoque-o no momento em que precisar do formulário vazio.
	 */
	private void limpaFormularioDoJSF() {
		this.conta = new Conta();
	}
}
