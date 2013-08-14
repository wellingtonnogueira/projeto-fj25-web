package br.com.caelum.teste;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import br.com.caelum.financas.infra.ValidatorUtil;
import br.com.caelum.financas.modelo.Movimentacao;

public class TestaValidacaoValorMovimentacao {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Movimentacao m = new Movimentacao();
//		m.setValor(BigDecimal.ZERO);
		m.setValor(BigDecimal.valueOf(1, 3));
		
		Validator validator = new ValidatorUtil().getValidator();
		Set<ConstraintViolation<Movimentacao>> erros = validator.validate(m);
		
		for (ConstraintViolation<Movimentacao> erro : erros) {
			System.out.println(erro.getMessage());
			System.out.println("\t" + erro.getPropertyPath());
		}
		
	}

}
