package br.unirio.transparencia.dao.organizacao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.VoidWork;


import br.unirio.transparencia.dao.escopo.EscopoDAO;
import br.unirio.transparencia.dao.escopo.EscopoDAOObjectify;
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
	    ofy().transact(new VoidWork() {
	        public void vrun() {
	            removerEscopos();
	            ofy().delete().entity(organizacao).now();
	        }
	    });
	}

	public void removerEscopos() {
	    ofy().transact(new VoidWork() {
	        public void vrun() {
	        	Escopo escopo;
	        	EscopoDAO dao = new EscopoDAOObjectify();
	        	for (Key<Escopo> key: organizacao.getKeysEscopos()){
	        		escopo = dao.findByKey(key);
	        		dao.remove(escopo);
	        	}
	        	
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
