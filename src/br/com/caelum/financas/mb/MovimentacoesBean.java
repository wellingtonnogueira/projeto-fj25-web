package br.com.caelum.financas.mb;

import java.util.Calendar;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.persistence.EntityManager;

import br.com.caelum.financas.dao.ContaDAO;
import br.com.caelum.financas.dao.MovimentacaoDAO;
import br.com.caelum.financas.dao.TagDAO;
import br.com.caelum.financas.infra.JPAUtil;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.Tag;
import br.com.caelum.financas.modelo.TipoMovimentacao;


@ManagedBean
public class MovimentacoesBean {
	private List<Movimentacao> movimentacoes;
	private Movimentacao movimentacao = new Movimentacao();
	private Integer contaId;
	private String tags;
	
	@ManagedProperty(name="em",value="#{requestScope.em}")
	private EntityManager em;
	
	public void setEm(EntityManager em) {
		this.em = em;
	}
	
	public void grava() {
		System.out.println("Fazendo a gravacao da movimentacao");
		
		new MovimentacoesBean.ExecutorTransacao(em) {
			@Override
			protected void execute() {
				Conta conta = contaDao.busca(contaId);
				movimentacao.setConta(conta);
				
				gravaEAssociaAsTags(em);
				
				dao.adiciona(movimentacao);
				
				movimentacoes = dao.lista();
			}
			
		}.run(true);
		
		limpaFormularioDoJSF();
	}
	

	public void remove() {
		System.out.println("Removendo a movimentacao");
		new MovimentacoesBean.ExecutorTransacao(em) {
			@Override
			protected void execute() {
			
				movimentacao = dao.busca(movimentacao.getId());
			
				dao.remove(movimentacao);
			}
		}.run(true);
		limpaFormularioDoJSF();
	}

	public List<Movimentacao> getMovimentacoes() {
		System.out.println("Listando as movimentacoes");
		
		new MovimentacoesBean.ExecutorTransacao(em) {
			@Override
			protected void execute() {
				movimentacoes = dao.lista();
			}
		}.run(false);
		return movimentacoes;
	}
	
	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}
	

	public Movimentacao getMovimentacao() {
		if(movimentacao.getData()==null) {
			movimentacao.setData(Calendar.getInstance());
		}
		return movimentacao;
	}

	public void setMovimentacao(Movimentacao movimentacao) {
		this.movimentacao = movimentacao;
	}

	public Integer getContaId() {
		return contaId;
	}

	public void setContaId(Integer contaId) {
		this.contaId = contaId;
	}

	/**
	 * Esse método apenas limpa o formulário da forma com que o JSF espera.
	 * Invoque-o no momento em que precisar do formulário vazio.
	 */
	private void limpaFormularioDoJSF() {
//		this.contaId = null;
		this.contaId = new Integer(0);
		this.movimentacao = new Movimentacao();
		this.tags = null;
	}

	public TipoMovimentacao[] getTiposDeMovimentacao() {
		return TipoMovimentacao.values();
	}
	
	private void gravaEAssociaAsTags(EntityManager em) {
		String[] nomesDasTags = this.tags.split(" ");
		TagDAO tagDAO = new TagDAO(em);
		
		for (String nome : nomesDasTags) {
			Tag tag = tagDAO.adicionaOuBuscaTagComNome(nome);
			movimentacao.getTags().add(tag);
		}
	}
	
	private abstract class ExecutorTransacao {
		
		protected ContaDAO contaDao;
		protected MovimentacaoDAO dao;
		protected EntityManager em;
		
		public ExecutorTransacao(EntityManager em) {
			this.em = em;
		}
		
		protected abstract void execute();
		
		public void run(boolean executarCommit) {
			
			contaDao = new ContaDAO(em);
			dao = new MovimentacaoDAO(em);
			
			execute();
			
		}
	}

}
