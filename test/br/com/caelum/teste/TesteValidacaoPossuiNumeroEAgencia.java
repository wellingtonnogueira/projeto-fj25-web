package br.com.caelum.teste;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import br.com.caelum.financas.infra.ValidatorUtil;
import br.com.caelum.financas.modelo.Conta;
import br.com.caelum.financas.modelo.Movimentacao;

public class TesteValidacaoPossuiNumeroEAgencia {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Validator validator = new ValidatorUtil().getValidator();
		
		Conta conta = new Conta();
		conta.setAgencia("Teste");
		
		Set<ConstraintViolation<Conta>> erros = validator.validate(conta);
		for (ConstraintViolation<Conta> erro : erros) {
			System.out.println(erro.getMessage());
			System.out.println("\t" + erro.getPropertyPath());
		}
		
		System.out.println("Movimentacao");
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setConta(conta);
		
		Set<ConstraintViolation<Movimentacao>> errosMovimentacao = validator.validate(movimentacao);
		
		for (ConstraintViolation<Movimentacao> erro : errosMovimentacao) {
			System.out.println(erro.getMessage());
			System.out.println("\t" + erro.getPropertyPath());
		}
		
	}

}
