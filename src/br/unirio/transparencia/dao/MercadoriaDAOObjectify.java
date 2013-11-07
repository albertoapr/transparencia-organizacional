package br.unirio.transparencia.dao;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.Serializable;
import java.util.List;

import com.googlecode.objectify.Key;


import br.unirio.transparencia.model.Mercadoria;

/**
 * Implementa o contrato de persistência da entidade <code>Mercadoria</code>.
 * 
 * <p>
 *   Nessa aplicação resolvemos a persistência utilizando o Objectify, um framework de persistência para o App Engine.<br/>
 *   A proposta do Objetify é denifir uma API mais alto-nível para manipular dados no <code>DataStore</code> do App Engine.
 * </p>
 * 
 * @see br.com.yaw.sjpac.dao.MercadoriaDAO
 * @see com.googlecode.objectify.ObjectifyService.
 * 
 * @author YaW Tecnologia
 */
public class MercadoriaDAOObjectify implements Serializable, MercadoriaDAO {

	@Override
	public Long save(Mercadoria mercadoria) {
		ofy().save().entity(mercadoria).now();
		return mercadoria.getId();
	}
	
	@Override
	public List<Mercadoria> getAll() {
		return ofy().load().type(Mercadoria.class).list();
	}
	
	@Override
	public Boolean remove(Mercadoria mercadoria) {
		ofy().delete().entity(mercadoria).now();
		return true;
	}
	
	@Override
	public Mercadoria findById(Long id) {
		Key<Mercadoria> k = Key.create(Mercadoria.class, id);
		return ofy().load().key(k).get();
	}
	
}
