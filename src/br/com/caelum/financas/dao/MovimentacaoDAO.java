package br.com.caelum.financas.dao;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.util.Version;
import org.hibernate.classic.ValidationFailure;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;

import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;
import br.com.caelum.financas.modelo.TipoMovimentacao;
import br.com.caelum.financas.modelo.ValorPorMesEAno;

public class MovimentacaoDAO {

	private final DAO<Movimentacao> dao;
	private EntityManager em;

	public MovimentacaoDAO(EntityManager em) {
		this.em = em;
		dao = new DAO<Movimentacao>(em,Movimentacao.class);
	}

	public void adiciona(Movimentacao t) {
		dao.adiciona(t);
	}

	public Movimentacao busca(Integer id) {
		return dao.busca(id);
	}

	public List<Movimentacao> lista() {
		return dao.lista();
	}

	public void remove(Movimentacao t) {
		dao.remove(t);
	}
	
	public List<Movimentacao> listaTodasMovimentacoes(Conta conta) {
		String jpql = "select m from Movimentacao m where m.conta=:conta order by m.valor desc";
		
		Query query = em.createQuery(jpql);
		query.setParameter("conta", conta);
		
		return query.getResultList();
	}
	
	public List<Movimentacao> listaPorValorETipo(BigDecimal valor, TipoMovimentacao tipo) {
		String jpql = "select m from Movimentacao m where m.valor=:valor and m.tipoMovimentacao=:tipo";
		
		Query query = em.createQuery(jpql);
		query.setParameter("valor", valor);
		query.setParameter("tipo", tipo);
		
		return query.getResultList();
	}
	
	public BigDecimal calculaTotalMovimentado(Conta conta, TipoMovimentacao tipo) {
		String jpql = "select sum(m.valor) from Movimentacao m " +
				"where m.conta=:conta and m.tipoMovimentacao=:tipo";
		TypedQuery<BigDecimal> query = em.createQuery(jpql, BigDecimal.class);
		
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		
		return query.getSingleResult();
	}
	
	public List<Movimentacao> buscaTodasMovimentacoesDaConta(String titular) {

		String jpql = "select m from Movimentacao m where m.conta.titular like :titular";
		TypedQuery<Movimentacao> query = em.createQuery(jpql, Movimentacao.class);
		query.setParameter("titular", "%"+titular+"%");
		
		return query.getResultList();
	}
	
	public List<ValorPorMesEAno> listaMesesComMovimentacoes(Conta conta, TipoMovimentacao tipo) {
		String jpql = String.format("select new %s(month(m.data), year(m.data), sum(m.valor)) " +
				"from Movimentacao m " +
				"where m.conta=:conta and m.tipoMovimentacao=:tipo " +
				"group by year(m.data)||month(m.data) " +
				"order by sum(m.valor) desc", ValorPorMesEAno.class.getName());
		
		Query query = em.createQuery(jpql);
		query.setParameter("conta", conta);
		query.setParameter("tipo", tipo);
		
		return query.getResultList();
	}

	public List<Movimentacao> todasComCriteria() {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder.createQuery(Movimentacao.class);
		criteria.from(Movimentacao.class);
		
		return em.createQuery(criteria).getResultList();
	}
	
	public BigDecimal somaMovimentacoesDoTitular(String titular) {
		
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<BigDecimal> criteria = builder.createQuery(BigDecimal.class);
		Root<Movimentacao> root = criteria.from(Movimentacao.class);
		
		Expression<BigDecimal> sum = builder.sum(root.<BigDecimal>get("valor"));
		criteria.select( sum );
		Predicate like = builder.like(root.<Conta>get("conta").<String>get("titular"), titular);
		criteria.where( like );
		
		return em.createQuery(criteria).getSingleResult();
		
	}
	
	public List<Movimentacao> pesquisa(Conta conta, TipoMovimentacao tipoMovimentacao, Integer mes) {
		CriteriaBuilder builder = em.getCriteriaBuilder();
		CriteriaQuery<Movimentacao> criteria = builder.createQuery(Movimentacao.class);
		Root<Movimentacao> root = criteria.from(Movimentacao.class);
		
		Predicate conjunction = builder.conjunction();
		
		if(conta.getId() != null) {
			Predicate equalConta = builder.equal(root.<Conta>get("conta"), conta);
			builder.and(conjunction, equalConta);
		}
		
		if(mes != null && mes != 0) {
			Path<Calendar> pathData = root.<Calendar>get("data");
			Expression<Integer> expression = builder.function("month", Integer.class, pathData);

			conjunction = builder.and(conjunction, builder.equal(expression, mes));
		}
		
		if(tipoMovimentacao != null) {
			Path<TipoMovimentacao> pathTipoMovimentacao = root.<TipoMovimentacao>get("tipoMovimentacao");
			conjunction = builder.and(conjunction, builder.equal(pathTipoMovimentacao, tipoMovimentacao));
		}
		
		criteria.where(conjunction);
		
		return em.createQuery(criteria).getResultList();
	}

	public List<Movimentacao> buscaMovimentacoesBaseadoNasTags(String texto) {

		FullTextEntityManager fullTextEM = Search.getFullTextEntityManager(em);
		QueryParser queryParser = new QueryParser(Version.LUCENE_29, "tags.nome", new BrazilianAnalyzer(Version.LUCENE_29));
		
		try {
			org.apache.lucene.search.Query query = queryParser.parse(texto);
			
			FullTextQuery textQuery = fullTextEM.createFullTextQuery(query, Movimentacao.class);
			
			return textQuery.getResultList();
			
		} catch (ParseException e) {
			throw new IllegalArgumentException(e);
		}
		
	}
}
