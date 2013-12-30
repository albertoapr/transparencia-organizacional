package br.unirio.transparencia.dao.organizacao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.VoidWork;


import br.unirio.transparencia.dao.escopo.EscopoDAO;
import br.unirio.transparencia.dao.escopo.EscopoDAOObjectify;
import br.unirio.transparencia.model.Avaliacao;
import br.unirio.transparencia.model.Escopo;
import br.unirio.transparencia.model.Organizacao;

/**
 * Implementa o contrato de persistência da entidade <code>Organizacao</code>.
 * 
 * <p>
 *   Nessa aplicação resolvemos a persistência utilizando o Objectify, um framework de persistência para o App Engine.<br/>
 *   A proposta do Objetify é denifir uma API mais alto-nível para manipular dados no <code>DataStore</code> do App Engine.
 * </p>
 * 

 * @see com.googlecode.objectify.ObjectifyService.
 * 
 * a
 */
public class OrganizacaoDAOObjectify implements Serializable, OrganizacaoDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Organizacao organizacao;

	@Override
	public Long save(Organizacao organizacao) {
		ofy().save().entity(organizacao).now();
		return organizacao.getId();
	}
	
	@Override
	public List<Organizacao> getAll() {
		return ofy().load().type(Organizacao.class).list();
	}
	
	@Override
	public Boolean remove(Organizacao _organizacao) {

		this.remover(_organizacao);
		
		return true;
		
		
	
	}
	
	
	public void remover(Organizacao _organizacao) {
		organizacao = _organizacao;
		Key<Organizacao> keyOrganizacao = Key.create(Organizacao.class, _organizacao.getId());
		final List<Key<Escopo>>  escopos = ofy().load().type(Escopo.class).filter("keyOrganizacao", keyOrganizacao).keys().list();	
		
		List<Key<Avaliacao>> listAvaliacoes = new ArrayList<Key<Avaliacao>>();
		for (Key<Escopo> keyEscopo:escopos)
			listAvaliacoes.addAll(ofy().load().type(Avaliacao.class).filter("keyEscopo",keyEscopo).keys().list());
		
		final List<Key<Avaliacao>> avaliacoes = listAvaliacoes ;
	   
		ofy().transact(new VoidWork() {
	        public void vrun() {
	        if (avaliacoes.size()>0)
	          ofy().delete().keys(avaliacoes).now(); //deleta as avaliações da organização
	        	
	        if (escopos!=null  && escopos.size()>0)	
	          ofy().delete().keys(escopos).now(); //deleta os escopos da organização
	       
	        ofy().delete().entity(organizacao).now(); //deleta a organização
	        }
	    });
	}
	
	
	
	
	
	@Override
	public Organizacao findById(Long id) {
		Key<Organizacao> k = Key.create(Organizacao.class, id);
		return ofy().load().key(k).get();
	}
	@Override
	public Organizacao findByKey(Key<Organizacao> k) {
		
		return ofy().load().key(k).get();
	}
	
}
