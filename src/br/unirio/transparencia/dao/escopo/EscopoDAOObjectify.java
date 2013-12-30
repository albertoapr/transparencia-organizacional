package br.unirio.transparencia.dao.escopo;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import br.unirio.transparencia.dao.escopo.EscopoDAO;
import br.unirio.transparencia.model.Avaliacao;
import br.unirio.transparencia.model.Escopo;
import br.unirio.transparencia.model.Organizacao;
import br.unirio.transparencia.model.Profissional;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.ObjectifyService;
import com.googlecode.objectify.VoidWork;


/**
 * Implementa o contrato de persistência da entidade <code>Escopo</code>.
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
public class EscopoDAOObjectify implements Serializable, EscopoDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Escopo escopo;

	@Override
	public Long save(Escopo _escopo) {
		//setando as chaves de referência para tabelas externas
	    Key<Organizacao> keyOrganizacao =Key.create(Organizacao.class, _escopo.getOrganizacao().getId());
		_escopo.setKeyOrganizacao(keyOrganizacao);//aqui setamos a chave da organizacao no escopo
		ofy().save().entity(_escopo).now();
		
		_escopo.getOrganizacao().addEscopo(_escopo);
		ofy().save().entity(_escopo.getOrganizacao()).now(); //Aqui salvamos a organizacao com o escopo incluído
		return _escopo.getId();
	}
	
	@Override
	public List<Escopo> getAll(Organizacao organizacao) {
		Key<Organizacao> keyOrg = null;
		if (organizacao.getId()!=null)
		  keyOrg = Key.create(Organizacao.class, organizacao.getId());
		if (keyOrg!=null)
		  return ofy().load().type(Escopo.class).filter("keyOrganizacao", keyOrg).list();
		else
			return null;
	}
	
	@Override
	public Boolean remove(Escopo _escopo) {
		this.escopo = _escopo;
		 Key<Escopo> keyEscopo = Key.create(Escopo.class, _escopo.getId());
		 final List<Key<Avaliacao>>  avaliacoes = ofy().load().type(Avaliacao.class).filter("keyEscopo", keyEscopo).keys().list();	
		  
		
		//iniciamos uma transação para remover o escopo, na transação removemos todas as avaliações realizadas no escopo
		//e depois removemos o escopo
	
		
		ofy().transact(new VoidWork(){
			@Override
			public void vrun() 
			{
			   ofy().delete().keys(avaliacoes).now();
		       ofy().delete().entity(escopo).now();				
			}
		});
		
		
	
		
		return true;
	}
	
	
	
	
	
	@Override
	public Escopo findById(Long id) {
		Key<Escopo> k = Key.create(Escopo.class, id);
		return ofy().load().key(k).get();
	}
	@Override
	public Escopo findByKey(Key<Escopo> k) {
		
		return ofy().load().key(k).get();
	}

	@Override
	public List<Escopo> getAll() {

		
		  return ofy().load().type(Escopo.class).list();
	
	}


}
