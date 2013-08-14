package br.com.caelum.financas.revision;

import org.hibernate.envers.RevisionListener;

import br.com.caelum.financas.modelo.UsuarioLogado;

public class MeuRevisionListener implements RevisionListener{

	@Override
	public void newRevision(Object revisionEntity) {
		MinhaRevisionEntity minhaRevisionmntity = (MinhaRevisionEntity) revisionEntity;
		
		String nomeUsuario = new UsuarioLogado().getUsername();
		
		minhaRevisionmntity.setUsername(nomeUsuario);
	}

}
