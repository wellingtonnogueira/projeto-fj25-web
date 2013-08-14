package br.com.caelum.financas.mb;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.modelo.Conta;

@ManagedBean
public class QtdeMovimentacoesDaContaBean {
	private Conta conta = new Conta();
	private int quantidade;
	@ManagedProperty(name="em",value="#{requestScope.em}")
	private EntityManager em;
	
	public void setEm(EntityManager em) {
		this.em = em;
	}

	public void lista() {
		System.out.println("Exibindo as quantidades de movimentacoes da conta");
		
		ContaDAO dao = new ContaDAO(em);
		conta = dao.busca(conta.getId());
		quantidade = conta.getMovimentacoes().size();
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}
}
